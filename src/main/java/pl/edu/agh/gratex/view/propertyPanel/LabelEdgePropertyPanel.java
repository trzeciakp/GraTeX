package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class LabelEdgePropertyPanel extends AbstractPropertyPanel {

    private LabelEdgePropertyModel model;
    private JLabel labelText;
    private JTextField textField;
    private JLabel lblColor;
    private JComboBox<Color> comboBoxFontColor;
    private JLabel lblPosition;
    private JSpinner spinnerPosition;
    private JComboBox<LoopPosition> comboBoxPosition;
    private JLabel lblDistance;
    private JSpinner spinnerDistance;
    private static int MIN_SIZE = 0;
    private static int MAX_SIZE = 99;
    private JLabel lblPlace;
    private JComboBox<LabelTopPlacement> comboBoxPlace;
    private JLabel lblRotation;
    private JComboBox<LabelRotation> comboBoxRotation;

    private void changed() {
        if (changedByUser)
            ((PanelPropertyEditor) getParent()).valueChanged(model);
    }

    public LabelEdgePropertyModel getModel() {
        return model;
    }

    public void setModel(PropertyModel pm) {
        changedByUser = false;
        model = (LabelEdgePropertyModel) pm.getCopy();
        textField.setText(model.getText());

        spinnerPosition.setVisible(true);
        comboBoxPosition.setVisible(false);
        comboBoxFontColor.setSelectedItem(model.getFontColor());
        if (model.getLoop().isEmpty() || model.getLoop() == IsLoop.NO) {
            if (model.getPosition() == PropertyModel.EMPTY) {
                spinnerPosition.setValue(StringLiterals.EMPTY_VALUE);
            } else {
                spinnerPosition.setValue(model.getPosition() + StringLiterals.PERCENT_SUFFIX);
            }
        } else if (model.getLoop() == IsLoop.YES) {
            lblPlace.setEnabled(false);
            comboBoxPlace.setEnabled(false);
            lblRotation.setEnabled(false);
            comboBoxRotation.setEnabled(false);
            spinnerPosition.setVisible(false);
            comboBoxPosition.setVisible(true);
            comboBoxPosition.setSelectedItem(LoopPosition.getByValue(model.getPosition()));
        }

        if (model.getSpacing() == PropertyModel.EMPTY) {
            spinnerDistance.setValue(StringLiterals.EMPTY_VALUE);
        } else {
            spinnerDistance.setValue(model.getSpacing() + StringLiterals.PX_SUFFIX);
        }
        comboBoxPlace.setSelectedItem(model.getTopPlacement());
        comboBoxRotation.setSelectedItem(model.getHorizontalPlacement());

        changedByUser = true;
    }

    @Override
    public void disableUnnecessaryFields() {
        super.disableUnnecessaryFields();
        comboBoxPlace.setEnabled(false);
        comboBoxPosition.setEnabled(false);
        textField.setEnabled(false);
        textField.setFocusable(false);
    }

    public LabelEdgePropertyPanel() {
        model = new LabelEdgePropertyModel();
        initialize();
        setModel(model);
    }

    private void initialize() {
        setLayout(null);
        /**************************** TEXT TEXTFIELD ******************************/
        labelText = new JLabel(StringLiterals.LABEL_EDGE_TEXT);
        labelText.setHorizontalAlignment(SwingConstants.LEFT);
        add(labelText);

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

        /**************************** COLOR COMBOBOX ******************************/
        lblColor = new JLabel(StringLiterals.LABEL_EDGE_TEXT_COLOR);
        lblColor.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblColor);

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

        /**************************** POSITION SPINNER ****************************/
        lblPosition = new JLabel(StringLiterals.LABEL_EDGE_POSITION);
        lblPosition.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblPosition);

        MIN_SIZE = 0;
        MAX_SIZE = 99;
        String[] positions = new String[MAX_SIZE - MIN_SIZE + 2];
        positions[0] = StringLiterals.EMPTY_VALUE;
        for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++) {
            positions[i - MIN_SIZE + 1] = i + StringLiterals.PERCENT_SUFFIX;
        }

        spinnerPosition = new JSpinner();
        spinnerPosition.setModel(new SpinnerListModel(positions));
        spinnerPosition.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                String value = (String) spinnerPosition.getValue();
                value = value.substring(0, value.indexOf(StringLiterals.EMPTY_VALUE));
                try {
                    int newValue = Integer.parseInt(value);
                    if (model.getPosition() != newValue) {
                        model.setPosition(newValue);
                        changed();
                    }
                } catch (NumberFormatException e) {
                    if (changedByUser) {
                        if (model.getPosition() != PropertyModel.EMPTY) {
                            spinnerPosition.setValue(model.getPosition() + StringLiterals.PX_SUFFIX);
                        }
                    } else {
                        spinnerPosition.setValue(StringLiterals.EMPTY_VALUE);
                        model.setPosition(PropertyModel.EMPTY);
                    }
                }
            }
        });
        add(spinnerPosition);

        comboBoxPosition = new JComboBox<>(LoopPosition.values());
        comboBoxPosition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                LoopPosition newValue = ((LoopPosition) comboBoxPosition.getSelectedItem());
                if ((!newValue.isEmpty() && (model.getPosition() != newValue.getValue()) || (!changedByUser))) {
                    model.setPosition(newValue.getValue());
                    changed();
                } else {
                    comboBoxPosition.setSelectedItem(LoopPosition.getByValue(model.getPosition()));
                }
            }
        });
        add(comboBoxPosition);

        /**************************************************************************/

        /**************************** DISTANCE SPINNER ****************************/
        lblDistance = new JLabel(StringLiterals.LABEL_EDGE_DISTANCE);
        lblDistance.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblDistance);

        MIN_SIZE = 0;
        MAX_SIZE = 99;
        String[] distances = new String[MAX_SIZE - MIN_SIZE + 2];
        distances[0] = StringLiterals.EMPTY_VALUE;
        for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++)
            distances[i - MIN_SIZE + 1] = i + StringLiterals.PX_SUFFIX;

        spinnerDistance = new JSpinner();
        spinnerDistance.setModel(new SpinnerListModel(distances));
        spinnerDistance.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                String value = (String) spinnerDistance.getValue();
                value = value.substring(0, value.indexOf(StringLiterals.EMPTY_VALUE));
                try {
                    int newValue = Integer.parseInt(value);
                    if (model.getSpacing() != newValue) {
                        model.setSpacing(newValue);
                        changed();
                    }
                } catch (NumberFormatException e) {
                    if (changedByUser) {
                        if (model.getSpacing() != PropertyModel.EMPTY)
                            spinnerDistance.setValue(model.getSpacing() + StringLiterals.PX_SUFFIX);
                    } else {
                        spinnerDistance.setValue(StringLiterals.EMPTY_VALUE);
                        model.setSpacing(PropertyModel.EMPTY);
                    }
                }
            }
        });
        add(spinnerDistance);
        /**************************************************************************/

        /**************************** PLACEMENT COMBOBOX **************************/
        lblPlace = new JLabel(StringLiterals.LABEL_EDGE_PLACEMENT);
        lblPlace.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblPlace);

        comboBoxPlace = new JComboBox<>(LabelTopPlacement.values());
        comboBoxPlace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                LabelTopPlacement newValue = ((LabelTopPlacement) comboBoxPlace.getSelectedItem());
                if ((!newValue.isEmpty()) && (model.getTopPlacement() != newValue) || (!changedByUser)) {
                    model.setTopPlacement(newValue);
                    changed();
                } else {
                    comboBoxPlace.setSelectedItem(model.getTopPlacement());
                }
            }
        });
        add(comboBoxPlace);
        /**************************************************************************/

        /**************************** ROTATION COMBOBOX ***************************/
        lblRotation = new JLabel(StringLiterals.LABEL_EDGE_ROTATION);
        lblRotation.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblRotation);

        comboBoxRotation = new JComboBox<LabelRotation>(LabelRotation.values());
        comboBoxRotation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                LabelRotation newValue = ((LabelRotation) comboBoxRotation.getSelectedItem());
                if ((!newValue.isEmpty()) && (model.getHorizontalPlacement() != newValue) || (!changedByUser)) {
                    model.setHorizontalPlacement(newValue);
                    changed();
                } else {
                    comboBoxRotation.setSelectedItem(model.getHorizontalPlacement());
                }
            }
        });
        add(comboBoxRotation);
        /**************************************************************************/

        /************************ USTAWIANIE BOUNDS *******************************/

        int spacing = 35;
        labels = new ArrayList<>();
        labels.add(labelText);
        labels.add(lblColor);
        labels.add(lblPosition);
        labels.add(lblDistance);
        labels.add(lblPlace);
        labels.add(lblRotation);

        for (int i = 0; i < labels.size(); i++)
            labels.get(i).setBounds(6, 22 + i * spacing, 84, 30);
        components = new ArrayList<>();
        components.add(textField);
        components.add(comboBoxFontColor);
        components.add(spinnerPosition);
        components.add(spinnerDistance);
        components.add(comboBoxPlace);
        components.add(comboBoxRotation);

        for (int i = 0; i < components.size(); i++)
            if (components.get(i) instanceof JTextField) {
                components.get(i).setBounds(90, 24 + i * spacing, 80, 26);
            } else {
                components.get(i).setBounds(90, 24 + i * spacing, 80, 25);
            }
        comboBoxPosition.setBounds(spinnerPosition.getBounds());
        comboBoxPosition.setVisible(false);
        components.add(comboBoxPosition);
    }
}
