package pl.edu.agh.gratex.view.propertyPanel;


import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundaryPropertyModel;
import pl.edu.agh.gratex.model.properties.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class LinkBoundaryPropertyPanel extends AbstractPropertyPanel {

    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 10;
    private LinkBoundaryPropertyModel model;
    private JComboBox<LineType> comboBoxLineType;
    private JSpinner spinnerLineSize;
    private ColorComboBox comboBoxLineColor;
    private JComboBox<IsDirected> comboBoxDirect;
    private JComboBox<ArrowType> comboBoxArrowType;
    private JLabel lblLineType;
    private JLabel lblLineSize;
    private JLabel lblLineColor;
    private JLabel lblDirect;
    private JLabel lblArrowType;
    private JLabel lblLabelPosition;
    private JLabel lblLabelText;
    private JLabel lblLabelColor;
    private JTextField textField;
    private ColorComboBox comboBoxFontColor;
    private JComboBox<LinkLabelPosition> comboBoxPosition;

    private void changed() {
        if (changedByUser)
            propertyEditorParent.valueChanged(model);
    }

    public LinkBoundaryPropertyModel getModel() {
        return model;
    }

    public void setModel(PropertyModel pm) {
        changedByUser = false;
        model = (LinkBoundaryPropertyModel) pm.getCopy();
        if (model.getDirected() == IsDirected.YES) {
            lblArrowType.setEnabled(true);
            comboBoxArrowType.setEnabled(true);
        } else {
            lblArrowType.setEnabled(false);
            comboBoxArrowType.setEnabled(false);
        }
        comboBoxLineType.setSelectedItem(model.getLineType());
        if (model.getLineWidth() == PropertyModel.EMPTY) {
            spinnerLineSize.setValue(StringLiterals.EMPTY_VALUE);
        } else {
            spinnerLineSize.setValue(model.getLineWidth() + StringLiterals.PX_SUFFIX);
        }
        comboBoxLineColor.setSelectedItem(model.getLineColor());
        comboBoxDirect.setSelectedItem(model.getDirected());
        comboBoxArrowType.setSelectedItem(model.getArrowType());
        LinkLabelPosition labelPosition = model.getLabelPosition();
        setLabelPropertiesEnabled(labelPosition != LinkLabelPosition.HIDDEN);
        comboBoxPosition.setSelectedItem(labelPosition);
        comboBoxFontColor.setSelectedItem(model.getLabelColor());
        textField.setText(model.getText());
        changedByUser = true;
    }

    private void setLabelPropertiesEnabled(boolean b) {
        lblLabelColor.setEnabled(b);
        lblLabelText.setEnabled(b);
        comboBoxFontColor.setEnabled(b);
        textField.setEnabled(b);

    }

    @Override
    protected void initialize() {
        setLayout(null);

        /**************************** LINE TYPE COMBOBOX **************************/

        List<LineType> lineTypes = new ArrayList<>();
        for (LineType lineType : LineType.values()) {
            if (lineType != LineType.NONE) {
                lineTypes.add(lineType);
            }
        }
        comboBoxLineType = new JComboBox<LineType>(lineTypes.toArray(new LineType[0]));
        comboBoxLineType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                LineType newValue = (LineType) comboBoxLineType.getSelectedItem();
                if(!newValue.isEmpty() && model.getLineType() != newValue || !changedByUser) {
                    model.setLineType(newValue);
                    changed();
                } else {
                    comboBoxLineType.setSelectedItem(model.getLineType());
                }
            }
        });
        add(comboBoxLineType);
        /**************************************************************************/

        /**************************** LINE WIDTH SPINNER **************************/

        String[] lineSizes = new String[MAX_SIZE - MIN_SIZE + 2];
        lineSizes[0] = StringLiterals.EMPTY_VALUE;
        for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++) {
            lineSizes[i - MIN_SIZE + 1] = i + StringLiterals.PX_SUFFIX;
        }

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
                        if (model.getLineWidth() != PropertyModel.EMPTY) {
                            spinnerLineSize.setValue(model.getLineWidth() + StringLiterals.PX_SUFFIX);
                        }
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

        /**************************** DIRECTED COMBOBOX **********************/

        comboBoxDirect = new JComboBox<IsDirected>(IsDirected.values());
        comboBoxDirect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                IsDirected newValue = ((IsDirected) comboBoxDirect.getSelectedItem());
                if ((!newValue.isEmpty()) && (model.getDirected() != newValue) || (!changedByUser)) {
                    model.setDirected(newValue);
                    changed();
                } else {
                    comboBoxDirect.setSelectedItem(model.getDirected());
                }
            }
        });
        add(comboBoxDirect);
        /**************************************************************************/

        /**************************** ARROW TYPE COMBOBOX ***********************/

        comboBoxArrowType = new JComboBox<>(ArrowType.values());
        comboBoxArrowType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ArrowType newValue = ((ArrowType) comboBoxArrowType.getSelectedItem());
                if ((!newValue.isEmpty()) && (model.getArrowType() != newValue) || (!changedByUser)) {
                    model.setArrowType(newValue);
                    changed();
                } else {
                    comboBoxArrowType.setSelectedItem(model.getArrowType());
                }
            }
        });
        add(comboBoxArrowType);
        /**************************************************************************/



        /**************************** LABEL TEXT TEXTFIELD ******************************/
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!textField.getText().equals(model.getText())) {
                    model.setText(textField.getText());
                    changed();
                }
            }
        });
        add(textField);
        textField.setColumns(10);
        /**************************************************************************/

        /****************************  LABEL COLOR COMBOBOX ******************************/
        comboBoxFontColor = new ColorComboBox();
        comboBoxFontColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (changedByUser) {
                    Color newValue = (Color) comboBoxFontColor.getSelectedItem();
                    if (newValue != null) {
                        if (!newValue.equals(model.getLabelColor())) {
                            model.setLabelColor(newValue);
                            changed();
                        }
                    } else {
                        comboBoxFontColor.setSelectedItem(model.getLabelColor());
                    }
                }
                repaint();
            }
        });
        add(comboBoxFontColor);
        /**************************************************************************/

        /**************************** LABEL POSITION COMBOBOX ****************************/
        comboBoxPosition = new JComboBox<>(LinkLabelPosition.values());
        comboBoxPosition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                LinkLabelPosition newValue = ((LinkLabelPosition) comboBoxPosition.getSelectedItem());
                if ((!newValue.isEmpty() && model.getLabelPosition() != newValue || (!changedByUser))) {
                    model.setLabelPosition(newValue);
                    changed();
                } else {
                    comboBoxPosition.setSelectedItem(model.getLabelPosition());
                }
            }
        });
        add(comboBoxPosition);

        /**************************************************************************/

        lblLineType = createJLabel(StringLiterals.EDGE_LINE_TYPE);
        lblLineSize = createJLabel(StringLiterals.EDGE_LINE_WIDTH);
        lblLineColor = createJLabel(StringLiterals.EDGE_LINE_COLOR);
        lblDirect = createJLabel(StringLiterals.EDGE_DIRECTED);
        lblArrowType = createJLabel(StringLiterals.EDGE_ARROW_TYPE);
        lblLabelPosition = createJLabel(StringLiterals.LINK_LABEL_POSITION);
        lblLabelText = createJLabel(StringLiterals.LINK_LABEL_TEXT);
        lblLabelColor = createJLabel(StringLiterals.LINK_LABEL_COLOR);


        components.add(comboBoxLineType);
        components.add(spinnerLineSize);
        components.add(comboBoxLineColor);
        components.add(comboBoxDirect);
        components.add(comboBoxArrowType);
        components.add(comboBoxPosition);
        components.add(textField);
        components.add(comboBoxFontColor);
    }
}
