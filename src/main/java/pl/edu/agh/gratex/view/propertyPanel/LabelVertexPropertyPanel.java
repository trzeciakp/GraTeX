package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.properties.LabelPosition;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;


@SuppressWarnings("serial")
public class LabelVertexPropertyPanel extends AbstractPropertyPanel {


    private LabelVertexPropertyModel model;
    private JLabel labelText;
    private JTextField textField;
    private JLabel lblColor;
    private JComboBox<Color> comboBoxFontColor;
    private JLabel lblPosition;
    private JComboBox<LabelPosition> comboBoxPosition;
    private JLabel lblDistance;
    private JSpinner spinnerDistance;
    private static int MIN_SIZE = 0;
    private static int MAX_SIZE = 99;

    private void changed() {
        if (changedByUser)
            ((PanelPropertyEditor) getParent()).valueChanged(model);
    }

    public LabelVertexPropertyModel getModel() {
        return model;
    }

    public void setModel(PropertyModel pm) {
        changedByUser = false;
        model = (LabelVertexPropertyModel) pm.getCopy();
        textField.setText(model.getText());
        comboBoxFontColor.setSelectedItem(model.getFontColor());
        comboBoxPosition.setSelectedItem(model.getLabelPosition());
        if (model.getSpacing() == PropertyModel.EMPTY) {
            spinnerDistance.setValue(StringLiterals.EMPTY_VALUE);
        } else {
            spinnerDistance.setValue(model.getSpacing() + StringLiterals.PX_SUFFIX);
        }

        changedByUser = true;
    }

    @Override
    public void disableUnnecessaryFields() {
        super.disableUnnecessaryFields();
        comboBoxPosition.setEnabled(false);
        textField.setEnabled(false);
        textField.setFocusable(false);
    }

    @Override
    public void focusFirstElement() {
        super.focusFirstElement();
    }

    public LabelVertexPropertyPanel() {
        model = new LabelVertexPropertyModel();
        initialize();
        setModel(model);
    }

    private void initialize() {
        setLayout(null);
        /**************************** TEXT TEXTFIELD ******************************/
        labelText = new JLabel(StringLiterals.LABEL_VERTEX_TEXT);
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
        lblColor = new JLabel(StringLiterals.LABEL_VERTEX_TEXT_COLOR);
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

        /**************************** POSITION COMBOBOX ***************************/
        lblPosition = new JLabel(StringLiterals.LABEL_VERTEX_POSITION);
        lblPosition.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblPosition);

        comboBoxPosition = new JComboBox<LabelPosition>(LabelPosition.values());
        comboBoxPosition.setMaximumRowCount(9);
        comboBoxPosition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                LabelPosition newValue = ((LabelPosition) comboBoxPosition.getSelectedItem());
                if ((!newValue.isEmpty()) && (model.getLabelPosition() != newValue) || (!changedByUser)) {
                    model.setLabelPosition(newValue);
                    changed();
                } else {
                    comboBoxPosition.setSelectedItem(model.getLabelPosition());
                }
            }
        });
        add(comboBoxPosition);
        /**************************************************************************/

        /**************************** DISTANCE SPINNER ****************************/
        lblDistance = new JLabel(StringLiterals.LABEL_VERTEX_DISTANCE);
        lblDistance.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblDistance);

        MIN_SIZE = 0;
        MAX_SIZE = 99;
        //TODO
        String[] distances = new String[MAX_SIZE - MIN_SIZE + 2];
        distances[0] = StringLiterals.EMPTY_VALUE;
        for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++) {
            distances[i - MIN_SIZE + 1] = i + StringLiterals.PX_SUFFIX;
        }

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
                        if (model.getSpacing() != PropertyModel.EMPTY) {
                            spinnerDistance.setValue(model.getSpacing() + StringLiterals.PX_SUFFIX);
                        }
                    } else {
                        spinnerDistance.setValue(StringLiterals.EMPTY_VALUE);
                        model.setSpacing(PropertyModel.EMPTY);
                    }
                }
            }
        });
        add(spinnerDistance);

        /**************************************************************************/

        /************************ USTAWIANIE BOUNDS *******************************/

        int spacing = 35;
        labels = new ArrayList<>();
        labels.add(labelText);
        labels.add(lblColor);
        labels.add(lblPosition);
        labels.add(lblDistance);

        for (int i = 0; i < labels.size(); i++)
            labels.get(i).setBounds(6, 22 + i * spacing, 84, 30);
        components = new ArrayList<>();
        components.add(textField);
        components.add(comboBoxFontColor);
        components.add(comboBoxPosition);
        components.add(spinnerDistance);

        for (int i = 0; i < components.size(); i++)
            if (components.get(i) instanceof JTextField) {
                components.get(i).setBounds(90, 24 + i * spacing, 80, 26);
            } else {
                components.get(i).setBounds(90, 24 + i * spacing, 80, 25);
            }
    }
}
