package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.hyperedge.HyperedgePropertyModel;
import pl.edu.agh.gratex.model.properties.JointDisplay;
import pl.edu.agh.gratex.model.properties.JointLabelPosition;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 *
 */
public class HyperedgePropertyPanel extends AbstractPropertyPanel {


    private HyperedgePropertyModel model = new HyperedgePropertyModel();
    private ColorComboBox comboBoxLineColor;
    private JSpinner spinnerLineSize;
    private int MIN_SIZE;
    private int MAX_SIZE;
    private JComboBox<LineType> comboBoxLineType;
    private ColorComboBox comboBoxJointColor;
    private JSpinner spinnerJointSize;
    private JComboBox<ShapeType> comboBoxJointShapeType;
    private JLabel lblJointShapeType;
    private JLabel lblJointSize;
    private JLabel lblJointColor;
    private JLabel lblLineType;
    private JLabel lblLineSize;
    private JLabel lblLineColor;
    private JComboBox<JointDisplay> comboBoxIsJointDisplayType;
    private JLabel lblIsJointDisplay;
    private ColorComboBox comboBoxJointLineColor;
    private JComboBox<LineType> comboBoxJointLineType;
    private JSpinner spinnerJointLineSize;
    private JTextField textField;
    private ColorComboBox comboBoxFontColor;
    private JComboBox<JointLabelPosition> comboBoxPosition;
    private JLabel lblJointLineType;
    private JLabel lblJointLineSize;
    private JLabel lblJointLineColor;
    private JLabel lblLabelPosition;
    private JLabel lblLabelText;
    private JLabel lblLabelColor;

    private void changed() {
        if (changedByUser) {
            propertyEditorParent.valueChanged(model);
        }
    }


    @Override
    public PropertyModel getModel() {
        return model;
    }

    @Override
    public void setModel(PropertyModel pm) {
        model = (HyperedgePropertyModel) pm.getCopy();

        changedByUser = false;
        comboBoxIsJointDisplayType.setSelectedItem(model.getIsJointDisplay());
        comboBoxJointShapeType.setSelectedItem(model.getJointShape());
        if (model.getJointSize() == PropertyModel.EMPTY || model.getJointSize() == 1) {
            spinnerJointSize.setValue(StringLiterals.EMPTY_VALUE);
        } else {
            spinnerJointSize.setValue(model.getJointSize() + StringLiterals.PX_SUFFIX);
        }
        comboBoxJointColor.setSelectedItem(model.getJointColor());
        comboBoxJointLineType.setSelectedItem(model.getJointLineType());
        if(model.getJointLineWidth() == PropertyModel.EMPTY) {
            spinnerJointLineSize.setValue(StringLiterals.EMPTY_VALUE);
        } else {
            spinnerJointLineSize.setValue(model.getJointLineWidth()+StringLiterals.PX_SUFFIX);
        }
        comboBoxJointLineColor.setSelectedItem(model.getJointLineColor());

        comboBoxLineType.setSelectedItem(model.getLineType());
        if (model.getLineWidth() == PropertyModel.EMPTY) {
            spinnerLineSize.setValue(StringLiterals.EMPTY_VALUE);
        } else {
            spinnerLineSize.setValue(model.getLineWidth() + StringLiterals.PX_SUFFIX);
        }
        comboBoxLineColor.setSelectedItem(model.getLineColor());
        comboBoxPosition.setSelectedItem(model.getJointLabelPosition());
        textField.setText(model.getText());
        comboBoxFontColor.setSelectedItem(model.getJointLabelColor());
        setJointPropertyEnabled(!model.getIsJointDisplay().isEmpty() && model.getIsJointDisplay() != JointDisplay.HIDDEN);
        setLabelPropertyEnabled(!model.getJointLabelPosition().isEmpty() && model.getJointLabelPosition() != JointLabelPosition.HIDDEN);
        changedByUser = true;
    }

    private void setLabelPropertyEnabled(boolean b) {
        lblLabelColor.setEnabled(b);
        lblLabelText.setEnabled(b);
        textField.setEnabled(b);
        comboBoxFontColor.setEnabled(b);
    }

    private void setJointPropertyEnabled(boolean b) {
        comboBoxJointColor.setEnabled(b);
        comboBoxJointShapeType.setEnabled(b);
        lblJointColor.setEnabled(b);
        lblJointShapeType.setEnabled(b);
        spinnerJointSize.setEnabled(b);
        lblJointSize.setEnabled(b);
        comboBoxJointLineType.setEnabled(b);
        lblJointLineType.setEnabled(b);
        LineType jointLineType = model.getJointLineType();
        setJoinLineTypePropertyEnabled(b && !jointLineType.isEmpty() && jointLineType != LineType.NONE);
    }

    private void setJoinLineTypePropertyEnabled(boolean b) {
        spinnerJointLineSize.setEnabled(b);
        lblJointLineSize.setEnabled(b);
        comboBoxJointLineColor.setEnabled(b);
        lblJointLineColor.setEnabled(b);
    }

    @Override
    protected void initialize() {
        setLayout(null);

        /**************************** JOINT DISPLAY COMBOBOX **************************/

        comboBoxIsJointDisplayType = new JComboBox<>(JointDisplay.values());
        comboBoxIsJointDisplayType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JointDisplay newValue = ((JointDisplay) comboBoxIsJointDisplayType.getSelectedItem());
                JointDisplay oldValue = model.getIsJointDisplay();
                if ((!newValue.isEmpty()) && (oldValue != newValue) || (!changedByUser)) {
                    model.setIsJointDisplay(newValue);
                    if(newValue == JointDisplay.HIDDEN) {
                        model.setJointSize(1);
                        model.setJointShape(ShapeType.CIRCLE);
                        model.setJointColor(model.getLineColor());
                    } else if(oldValue == JointDisplay.HIDDEN && newValue == JointDisplay.VISIBLE) {
                        model.setJointSize(3);
                    }
                    changed();
                } else {
                    comboBoxIsJointDisplayType.setSelectedItem(oldValue);
                }
            }
        });
        add(comboBoxIsJointDisplayType);
        /**************************************************************************/

        /**************************** JOINT TYPE COMBOBOX **************************/

        comboBoxJointShapeType = new JComboBox<>(ShapeType.values());
        comboBoxJointShapeType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ShapeType newValue = ((ShapeType) comboBoxJointShapeType.getSelectedItem());
                if ((!newValue.isEmpty()) && (model.getJointShape() != newValue) || (!changedByUser)) {
                    model.setJointShape(newValue);
                    changed();
                } else {
                    comboBoxJointShapeType.setSelectedItem(model.getJointShape());
                }
            }
        });
        add(comboBoxJointShapeType);
        /**************************************************************************/

        /**************************** JOINT SIZE SPINNER *************************/

        MIN_SIZE = 3;
        MAX_SIZE = 20;
        String[] vertexSizes = new String[MAX_SIZE - MIN_SIZE + 2];
        vertexSizes[0] = StringLiterals.EMPTY_VALUE;
        for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++) {
            vertexSizes[i - MIN_SIZE + 1] = i + StringLiterals.PX_SUFFIX;
        }

        spinnerJointSize = new JSpinner();
        spinnerJointSize.setModel(new SpinnerListModel(vertexSizes));
        spinnerJointSize.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                String value = (String) spinnerJointSize.getValue();
                value = value.substring(0, value.indexOf(" "));
                int radius = model.getJointSize();
                try {
                    int newValue = Integer.parseInt(value);
                    if (radius != newValue) {
                        model.setJointSize(newValue);
                        changed();
                    }
                } catch (NumberFormatException e) {
                    if (changedByUser) {
                        if (radius != PropertyModel.EMPTY) {
                            spinnerJointSize.setValue(radius + StringLiterals.PX_SUFFIX);
                        }
                    } else {
                        spinnerJointSize.setValue(StringLiterals.EMPTY_VALUE);
                        model.setJointSize(PropertyModel.EMPTY);
                    }
                }
            }
        });
        add(spinnerJointSize);
        /**************************************************************************/

        /**************************** JOINT LINE TYPE COMBOBOX **************************/

        comboBoxJointLineType = new JComboBox<LineType>(LineType.values());
        comboBoxJointLineType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                LineType newValue = (LineType) comboBoxJointLineType.getSelectedItem();
                if(!newValue.isEmpty() && model.getJointLineType() != newValue || !changedByUser) {
                    model.setJointLineType(newValue);
                    changed();
                } else {
                    comboBoxJointLineType.setSelectedItem(model.getJointLineType());
                }
            }
        });
        add(comboBoxJointLineType);

        /**************************************************************************/


        /**************************** JOINT LINE WIDTH SPINNER **************************/

        MIN_SIZE = 1;
        MAX_SIZE = 10;
        String[] lineSizes = new String[MAX_SIZE - MIN_SIZE + 2];
        lineSizes[0] = StringLiterals.EMPTY_VALUE;
        for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++) {
            lineSizes[i - MIN_SIZE + 1] = i + StringLiterals.PX_SUFFIX;
        }

        spinnerJointLineSize = new JSpinner();
        spinnerJointLineSize.setModel(new SpinnerListModel(lineSizes));
        spinnerJointLineSize.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                String value = (String) spinnerJointLineSize.getValue();
                value = value.substring(0, value.indexOf(" "));
                try {
                    int newValue = Integer.parseInt(value);
                    if (model.getJointLineWidth() != newValue) {
                        model.setJointLineWidth(newValue);
                        changed();
                    }
                } catch (NumberFormatException e) {
                    if (changedByUser) {
                        if (model.getJointSize() != PropertyModel.EMPTY) {
                            spinnerJointLineSize.setValue(model.getJointLineWidth() + StringLiterals.PX_SUFFIX);
                        }
                    } else {
                        spinnerJointLineSize.setValue(StringLiterals.EMPTY_VALUE);
                        model.setJointLineWidth(PropertyModel.EMPTY);
                    }
                }
            }
        });

        add(spinnerJointLineSize);
        /**************************************************************************/

        /**************************** JOINT COLOR COMBOBOX ***********************/

        comboBoxJointColor = new ColorComboBox();
        comboBoxJointColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (changedByUser) {
                    Color newValue = (Color) comboBoxJointColor.getSelectedItem();
                    if (newValue != null) {
                        if (!newValue.equals(model.getJointColor())) {
                            model.setJointColor(newValue);
                            changed();
                        }
                    } else {
                        comboBoxJointColor.setSelectedItem(model.getJointColor());
                    }
                }
                repaint();
            }
        });
        add(comboBoxJointColor);
        /**************************************************************************/


        /**************************** JOINT LINE COLOR COMBOBOX ***********************/

        comboBoxJointLineColor = new ColorComboBox();
        comboBoxJointLineColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (changedByUser) {
                    Color newValue = (Color) comboBoxJointLineColor.getSelectedItem();
                    if (newValue != null) {
                        if (!newValue.equals(model.getJointLineColor())) {
                            model.setJointLineColor(newValue);
                            changed();
                        }
                    } else {
                        comboBoxJointLineColor.setSelectedItem(model.getJointLineColor());
                    }
                }
                repaint();
            }
        });
        add(comboBoxJointLineColor);
        /**************************************************************************/


        /**************************** LINE TYPE COMBOBOX **************************/

        java.util.List<LineType> lineTypes = new ArrayList<>();
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

        MIN_SIZE = 1;
        MAX_SIZE = 10;
        /*String[] lineSizes = new String[MAX_SIZE - MIN_SIZE + 2];
        lineSizes[0] = StringLiterals.EMPTY_VALUE;
        for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++) {
            lineSizes[i - MIN_SIZE + 1] = i + StringLiterals.PX_SUFFIX;
        } */

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
                            if(model.getIsJointDisplay() == JointDisplay.HIDDEN) {
                                model.setJointColor(newValue);
                            }
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
                        if (!newValue.equals(model.getJointLabelColor())) {
                            model.setJointLabelColor(newValue);
                            changed();
                        }
                    } else {
                        comboBoxFontColor.setSelectedItem(model.getJointLabelColor());
                    }
                }
                repaint();
            }
        });
        add(comboBoxFontColor);
        /**************************************************************************/

        /**************************** LABEL POSITION COMBOBOX ****************************/
        comboBoxPosition = new JComboBox<>(JointLabelPosition.values());
        comboBoxPosition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JointLabelPosition newValue = ((JointLabelPosition) comboBoxPosition.getSelectedItem());
                if ((!newValue.isEmpty() && model.getJointLabelPosition() != newValue || (!changedByUser))) {
                    model.setJointLabelPosition(newValue);
                    changed();
                } else {
                    comboBoxPosition.setSelectedItem(model.getJointLabelPosition());
                }
            }
        });
        add(comboBoxPosition);

        /**************************************************************************/

        lblLineType = createJLabel(StringLiterals.HYPEREDGE_LINE_TYPE);
        lblLineSize = createJLabel(StringLiterals.HYPEREDGE_LINE_WIDTH);
        lblLineColor = createJLabel(StringLiterals.HYPEREDGE_LINE_COLOR);
        lblIsJointDisplay = createJLabel(StringLiterals.HYPEREDGE_JOINT_DISPLAY);
        lblJointShapeType = createJLabel(StringLiterals.HYPEREDGE_JOINT_SHAPE_TYPE);
        lblJointSize = createJLabel(StringLiterals.HYPEREDGE_JOINT_SIZE);
        lblJointColor = createJLabel(StringLiterals.HYPEREDGE_JOINT_COLOR);
        lblJointLineType = createJLabel(StringLiterals.HYPEREDGE_JOINT_LINE_TYPE);
        lblJointLineSize = createJLabel(StringLiterals.HYPEREDGE_JOINT_LINE_SIZE);
        lblJointLineColor = createJLabel(StringLiterals.HYPEREDGE_JOINT_LINE_COLOR);
        lblLabelPosition = createJLabel(StringLiterals.HYPEREDGE_LABEL_POSITION);
        lblLabelText = createJLabel(StringLiterals.HYPEREDGE_LABEL_TEXT);
        lblLabelColor = createJLabel(StringLiterals.HYPEREDGE_LABEL_COLOR);

        components.add(comboBoxLineType);
        components.add(spinnerLineSize);
        components.add(comboBoxLineColor);
        components.add(comboBoxIsJointDisplayType);
        components.add(comboBoxJointShapeType);
        components.add(spinnerJointSize);
        components.add(comboBoxJointColor);
        components.add(comboBoxJointLineType);
        components.add(spinnerJointLineSize);
        components.add(comboBoxJointLineColor);
        components.add(comboBoxPosition);
        components.add(textField);
        components.add(comboBoxFontColor);
    }
}
