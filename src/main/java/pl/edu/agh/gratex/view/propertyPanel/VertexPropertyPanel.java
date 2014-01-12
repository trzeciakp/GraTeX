package pl.edu.agh.gratex.view.propertyPanel;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.graph.GraphNumeration;
import pl.edu.agh.gratex.model.properties.IsLabelInside;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;

import javax.swing.*;
import javax.swing.JSpinner.ListEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class VertexPropertyPanel extends AbstractPropertyPanel {

    GeneralController generalController;

    private VertexPropertyModel model;
    private JLabel lblVertexType;
    private JComboBox<ShapeType> comboBoxVertexType;
    private JLabel lblColor;
    private JComboBox<Color> comboBoxVertexColor;
    private JLabel lblLineType;
    private JComboBox<LineType> comboBoxLineType;
    private JLabel lblLineSize;
    private JSpinner spinnerLineSize;
    private JLabel lblVertexSize;
    private JSpinner spinnerVertexSize;
    private JLabel lblLineColor;
    private JComboBox<Color> comboBoxLineColor;
    private JLabel lblLabelInside;
    private JComboBox<IsLabelInside> comboBoxLabelInside;
    private JLabel lblNumber;
    private JSpinner spinnerNumber;
    private JLabel lblFontColor;
    private JComboBox<Color> comboBoxFontColor;
    private AbstractSpinnerModel[] listModels;

    private static int MIN_SIZE;
    private static int MAX_SIZE;

    private void changed() {
        if (changedByUser) {
            propertyEditorParent.valueChanged(model);
        }
    }

    public void updateNumeration() {
        if (generalController.getGraph().getGraphNumeration().isNumerationDigital()) {
            spinnerNumber.setModel(listModels[1]);
            ((JSpinner.DefaultEditor) spinnerNumber.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
        } else {
            spinnerNumber.setModel(listModels[0]);
        }
    }

    public VertexPropertyPanel(GeneralController generalController) {
        this.generalController = generalController;

        model = new VertexPropertyModel();
        setModel(model);
    }

    public VertexPropertyModel getModel() {
        return model;
    }

    public void setModel(PropertyModel pm) {

        model = (VertexPropertyModel) pm.getCopy();
        changedByUser = false;
        lblNumber.setEnabled(true);
        spinnerNumber.setEnabled(true);
        if (model.getLabelInsideENUM() == IsLabelInside.NO) {
            lblNumber.setEnabled(false);
            spinnerNumber.setEnabled(false);
            lblFontColor.setEnabled(false);
            comboBoxFontColor.setEnabled(false);
        } else if (model.getLabelInsideENUM() == IsLabelInside.YES) {
            lblFontColor.setEnabled(true);
            comboBoxFontColor.setEnabled(true);
        }
        updateNumeration();
        if (model.getNumber() == PropertyModel.EMPTY) {
            lblNumber.setEnabled(false);
            spinnerNumber.setEnabled(false);
            spinnerNumber.setValue(StringLiterals.EMPTY_VALUE);
        } else if (generalController.getGraph().getGraphNumeration().isNumerationDigital()) {
            spinnerNumber.setValue(model.getNumber());
        } else {
            spinnerNumber.setValue(GraphNumeration.digitalToAlphabetical(model.getNumber()));
        }
        comboBoxVertexType.setSelectedItem(model.getShape());
        if (model.getRadius() == PropertyModel.EMPTY) {
            spinnerVertexSize.setValue(StringLiterals.EMPTY_VALUE);
        } else {
            spinnerVertexSize.setValue(model.getRadius() + StringLiterals.PX_SUFFIX);
        }
        comboBoxVertexColor.setSelectedItem(model.getVertexColor());
        comboBoxFontColor.setSelectedItem(model.getFontColor());
        comboBoxLineType.setSelectedItem(model.getLineType());
        if (model.getLineType() == LineType.NONE || model.getLineType().isEmpty()) {
            spinnerLineSize.setEnabled(false);
            lblLineSize.setEnabled(false);
            lblLineColor.setEnabled(false);
            comboBoxLineColor.setEnabled(false);
        } else {
            spinnerLineSize.setEnabled(true);
            lblLineSize.setEnabled(true);
            lblLineColor.setEnabled(true);
            comboBoxLineColor.setEnabled(true);
        }
        if (model.getLineWidth() == PropertyModel.EMPTY || model.getLineWidth() == 0) {
            spinnerLineSize.setValue(StringLiterals.EMPTY_VALUE);
        } else {
            spinnerLineSize.setValue(model.getLineWidth() + StringLiterals.PX_SUFFIX);
        }
        comboBoxLineColor.setSelectedItem(model.getLineColor());
        comboBoxLabelInside.setSelectedItem(model.getLabelInsideENUM());

        changedByUser = true;
    }

    @Override
    public void disableUnnecessaryFields() {
        super.disableUnnecessaryFields();
        spinnerNumber.setEnabled(false);
    }

    protected void initialize() {
        setLayout(null);

        /**************************** VERTEX TYPE COMBOBOX **************************/
        lblVertexType = createJLabel(StringLiterals.VERTEX_SHAPE_TYPE);

        comboBoxVertexType = new JComboBox<>(ShapeType.values());
        comboBoxVertexType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ShapeType newValue = ((ShapeType) comboBoxVertexType.getSelectedItem());
                if ((!newValue.isEmpty()) && (model.getShape() != newValue) || (!changedByUser)) {
                    model.setShape(newValue);
                    changed();
                } else {
                    comboBoxVertexType.setSelectedItem(model.getShape());
                }
            }
        });
        add(comboBoxVertexType);
        /**************************************************************************/

        /**************************** VERTEX SIZE SPINNER *************************/
        lblVertexSize = createJLabel(StringLiterals.VERTEX_SIZE);

        MIN_SIZE = 10;
        MAX_SIZE = 99;
        String[] vertexSizes = new String[MAX_SIZE - MIN_SIZE + 2];
        vertexSizes[0] = StringLiterals.EMPTY_VALUE;
        for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++) {
            vertexSizes[i - MIN_SIZE + 1] = i + StringLiterals.PX_SUFFIX;
        }

        spinnerVertexSize = new JSpinner();
        spinnerVertexSize.setModel(new SpinnerListModel(vertexSizes));
        spinnerVertexSize.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                String value = (String) spinnerVertexSize.getValue();
                value = value.substring(0, value.indexOf(" "));
                try {
                    int newValue = Integer.parseInt(value);
                    if (model.getRadius() != newValue) {
                        model.setRadius(newValue);
                        changed();
                    }
                } catch (NumberFormatException e) {
                    if (changedByUser) {
                        if (model.getRadius() != PropertyModel.EMPTY)
                            spinnerVertexSize.setValue(model.getRadius() + StringLiterals.PX_SUFFIX);
                    } else {
                        spinnerVertexSize.setValue(StringLiterals.EMPTY_VALUE);
                        model.setRadius(PropertyModel.EMPTY);
                    }
                }
            }
        });
        add(spinnerVertexSize);
        /**************************************************************************/

        /**************************** VERTEX COLOR COMBOBOX ***********************/
        lblColor = createJLabel(StringLiterals.VERTEX_COLOR);

        comboBoxVertexColor = new ColorComboBox();
        comboBoxVertexColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (changedByUser) {
                    Color newValue = (Color) comboBoxVertexColor.getSelectedItem();
                    if (newValue != null) {
                        if (!newValue.equals(model.getVertexColor())) {
                            model.setVertexColor(newValue);
                            changed();
                        }
                    } else {
                        comboBoxVertexColor.setSelectedItem(model.getVertexColor());
                    }
                }
                repaint();
            }
        });
        add(comboBoxVertexColor);
        /**************************************************************************/

        /**************************** LINE TYPE COMBOBOX **************************/
        lblLineType = createJLabel(StringLiterals.VERTEX_LINE_TYPE);

        comboBoxLineType = new JComboBox<>(LineType.values());
        comboBoxLineType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                LineType newValue = ((LineType) comboBoxLineType.getSelectedItem());
                LineType oldLineType = model.getLineType();
                if ((newValue != LineType.EMPTY) && (oldLineType != newValue) || (!changedByUser)) {
                    model.setLineType(newValue);
                    /*if(newValue == LineType.NONE) {
                        model.setLineWidth(0);
                    } else if (oldLineType == LineType.NONE) {
                        model.setLineWidth(1);
                    }*/
                    changed();
                } else {
                    comboBoxLineType.setSelectedItem(oldLineType);
                }
            }
        });
        add(comboBoxLineType);
        /**************************************************************************/

        /**************************** LINE WIDTH SPINNER **************************/
        lblLineSize = createJLabel(StringLiterals.VERTEX_LINE_WIDTH);

        MIN_SIZE = 1;
        MAX_SIZE = 10;
        String[] lineSizes = new String[MAX_SIZE - MIN_SIZE + 2];
        lineSizes[0] = StringLiterals.EMPTY_VALUE;
        for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++)
            lineSizes[i - MIN_SIZE + 1] = i + StringLiterals.PX_SUFFIX;

        spinnerLineSize = new JSpinner();
        spinnerLineSize.setModel(new SpinnerListModel(lineSizes));
        spinnerLineSize.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                String value = (String) spinnerLineSize.getValue();
                value = value.substring(0, value.indexOf(" "));
                try {
                    int newValue = Integer.parseInt(value);
                    if (model.getLineWidth() != newValue) {
                        model.setLineWidth(newValue);
                        changed();
                    }
                } catch (NumberFormatException e) {
                    if (changedByUser) {
                        if (model.getLineWidth() != PropertyModel.EMPTY)
                            spinnerLineSize.setValue(model.getLineWidth() + StringLiterals.PX_SUFFIX);
                    } else {
                        spinnerLineSize.setValue(StringLiterals.EMPTY_VALUE);
                        model.setLineWidth(PropertyModel.EMPTY);
                    }
                }
            }
        });

        add(spinnerLineSize);
        /**************************************************************************/

        /**************************** LINE COLOR COMBOBOX *************************/
        lblLineColor = createJLabel(StringLiterals.VERTEX_LINE_COLOR);

        comboBoxLineColor = new ColorComboBox();
        comboBoxLineColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (changedByUser) {
                    Color newValue = (Color) comboBoxLineColor.getSelectedItem();
                    if (newValue != null) {
                        if (!newValue.equals(model.getLineColor())) {
                            model.setLineColor(newValue);
                            changed();
                        }
                    } else {
                        comboBoxLineColor.setSelectedItem(model.getLineColor());
                    }
                }
                repaint();
            }
        });
        add(comboBoxLineColor);
        /**************************************************************************/

        /**************************** LABEL INSIDE COMBOBOX ***********************/
        lblLabelInside = createJLabel(StringLiterals.VERTEX_LABEL_INSIDE);

        comboBoxLabelInside = new JComboBox<>(IsLabelInside.values());
        comboBoxLabelInside.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                IsLabelInside newValue = ((IsLabelInside) comboBoxLabelInside.getSelectedItem());
                if ((!newValue.isEmpty() && (model.getLabelInsideENUM() != newValue) || (!changedByUser))) {
                    model.setLabelInside(newValue);
                    changed();
                } else {
                    comboBoxLabelInside.setSelectedItem(model.getLabelInsideENUM());
                }
            }
        });
        add(comboBoxLabelInside);
        /**************************************************************************/

        /**************************** COLOR COMBOBOX ******************************/
        lblFontColor = createJLabel(StringLiterals.VERTEX_FONT_COLOR);

        comboBoxFontColor = new ColorComboBox();
        comboBoxFontColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (changedByUser) {
                    Color newValue = (Color) comboBoxFontColor.getSelectedItem();
                    if (newValue != null) {
                        if (!newValue.equals(model.getFontColor())) {
                            model.setFontColor(newValue);
                            changed();
                        }
                    } else {
                        comboBoxFontColor.setSelectedItem(model.getFontColor());
                    }
                }
                repaint();
            }
        });
        add(comboBoxFontColor);
        /**************************************************************************/

        /**************************** NUMBER SPINNER ******************************/
        lblNumber = createJLabel(StringLiterals.VERTEX_NUMBER);

        String[] alpha = new String[Const.MAX_VERTEX_NUMBER];
        for (int i = 0; i < Const.MAX_VERTEX_NUMBER; i++) {
            alpha[i] = GraphNumeration.digitalToAlphabetical(i);
        }
        listModels = new AbstractSpinnerModel[2];
        listModels[0] = new SpinnerListModel(alpha);
        listModels[1] = new SpinnerNumberModel(1, 1, Const.MAX_VERTEX_NUMBER - 1, 1);

        spinnerNumber = new JSpinner() {
            public void setEnabled(boolean flag) {
                super.setEnabled(flag);
                if (!flag) {
                    setModel(new SpinnerListModel(new String[]{StringLiterals.EMPTY_VALUE}));
                }
            }
        };

        //TODO
        if (generalController.getGraph().getGraphNumeration().isNumerationDigital()) {
            spinnerNumber.setModel(listModels[1]);
            ((JSpinner.DefaultEditor) spinnerNumber.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
        } else {
            spinnerNumber.setModel(listModels[0]);
            ((ListEditor) spinnerNumber.getEditor()).getTextField().setFormatterFactory(new DefaultFormatterFactory(new MyListFormatter()));
        }

        spinnerNumber.setModel(listModels[1]);
        ((JSpinner.DefaultEditor) spinnerNumber.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
        spinnerNumber.addChangeListener(new ChangeListener() {
            private int previous = 0;

            private int getValue() {
                int value;
                if (generalController.getGraph().getGraphNumeration().isNumerationDigital())
                    value = (Integer) spinnerNumber.getValue();
                else {
                    String s = (String) spinnerNumber.getValue();
                    int podst = 1;
                    value = 0;
                    for (int i = s.length(); i > 0; i--) {
                        value += (s.charAt(i - 1) - 'A' + 1) * podst;
                        podst *= 26;
                    }
                }
                return value;
            }

            private void setValue(int arg) {
                if (generalController.getGraph().getGraphNumeration().isNumerationDigital()) {
                    spinnerNumber.setValue(arg);
                } else {
                    spinnerNumber.setValue(GraphNumeration.digitalToAlphabetical(arg));
                }
            }

            public void stateChanged(ChangeEvent arg0) {
                if (changedByUser) {
                    int value = getValue();
                    if (value != previous) {
                        while ((value > 0) && (value < Const.MAX_VERTEX_NUMBER)) {
                            if (!generalController.getGraph().getGraphNumeration().isUsed(value)) {

                                model.setNumber(value);
                                previous = value;
                                setValue(value);
                                changed();
                                break;
                            }
                            value += Integer.signum((value - previous));
                        }
                        if ((value == 0) || (value == Const.MAX_VERTEX_NUMBER)) {
                            setValue(previous);
                        }
                    }

                } else {
                    previous = getValue();
                }

            }
        });
        add(spinnerNumber);
        /**************************************************************************/

        /************************ USTAWIANIE BOUNDS *******************************/

        components.add(comboBoxVertexType);
        components.add(spinnerVertexSize);
        components.add(comboBoxVertexColor);
        components.add(comboBoxLineType);
        components.add(spinnerLineSize);
        components.add(comboBoxLineColor);
        components.add(comboBoxLabelInside);
        components.add(comboBoxFontColor);
        components.add(spinnerNumber);
    }
}
