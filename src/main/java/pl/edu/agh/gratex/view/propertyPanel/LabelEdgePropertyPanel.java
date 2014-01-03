package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.LabelHorizontalPlacement;
import pl.edu.agh.gratex.model.properties.LabelTopPlacement;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


@SuppressWarnings("serial")
public class LabelEdgePropertyPanel extends AbstractPropertyPanel {

    private LabelEdgePropertyModel model;
    private JLabel labelText;
    private JTextField textField;
    private JLabel lblColor;
    private JComboBox<Color> comboBoxFontColor;
    private JLabel lblPosition;
    private JSpinner spinnerPosition;
    private JComboBox<Option> comboBoxPosition;
    private JLabel lblDistance;
    private JSpinner spinnerDistance;
    private static int MIN_SIZE = 0;
    private static int MAX_SIZE = 99;
    private JLabel lblPlace;
    private JComboBox<Option> comboBoxPlace;
    private JLabel lblRotation;
    private JComboBox<Option> comboBoxRotation;

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
        if (model.getLoop() < PropertyModel.YES) {
            if (model.getPosition() == -1)
                spinnerPosition.setValue(" ");
            else
                spinnerPosition.setValue(model.getPosition() + " %");
        } else if (model.getLoop() == PropertyModel.YES) {
            lblPlace.setEnabled(false);
            comboBoxPlace.setEnabled(false);
            lblRotation.setEnabled(false);
            comboBoxRotation.setEnabled(false);
            spinnerPosition.setVisible(false);
            comboBoxPosition.setVisible(true);
            comboBoxPosition.setSelectedIndex((model.getPosition() - 25) / 25);
        }

        if (model.getSpacing() == -1)
            spinnerDistance.setValue(" ");
        else
            spinnerDistance.setValue(model.getSpacing() + " px");
        comboBoxPlace.setSelectedIndex(model.getTopPlacement() + 1);
        comboBoxRotation.setSelectedIndex(model.getHorizontalPlacement() + 1);

        changedByUser = true;
    }

    public LabelEdgePropertyPanel() {
        model = new LabelEdgePropertyModel();
        initialize();
        setModel(model);
    }

    private void initialize() {
        setLayout(null);
        /**************************** TEXT TEXTFIELD ******************************/
        labelText = new JLabel("Text:");
        labelText.setHorizontalAlignment(SwingConstants.LEFT);
        labelText.setBounds(26, 42, 64, 14);
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
        textField.setBounds(102, 35, 122, 28);
        add(textField);
        textField.setColumns(10);
        /**************************************************************************/

        /**************************** COLOR COMBOBOX ******************************/
        lblColor = new JLabel("Text color:");
        lblColor.setHorizontalAlignment(SwingConstants.LEFT);
        lblColor.setBounds(6, 67, 84, 14);
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
        lblPosition = new JLabel("Position:");
        lblPosition.setHorizontalAlignment(SwingConstants.LEFT);
        lblPosition.setBounds(26, 92, 64, 14);
        add(lblPosition);

        MIN_SIZE = 0;
        MAX_SIZE = 99;
        String[] positions = new String[MAX_SIZE - MIN_SIZE + 2];
        positions[0] = new String(" ");
        for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++)
            positions[i - MIN_SIZE + 1] = new String(i + " %");

        spinnerPosition = new JSpinner();
        spinnerPosition.setModel(new SpinnerListModel(positions));
        spinnerPosition.setBounds(101, 38, 50, 22);
        spinnerPosition.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                String value = (String) spinnerPosition.getValue();
                value = value.substring(0, value.indexOf(" "));
                try {
                    int newValue = Integer.parseInt(value);
                    if (model.getPosition() != newValue) {
                        model.setPosition(newValue);
                        changed();
                    }
                } catch (NumberFormatException e) {
                    if (changedByUser) {
                        if (model.getPosition() != -1)
                            spinnerPosition.setValue(model.getPosition() + " px");
                    } else {
                        spinnerPosition.setValue(" ");
                        model.setPosition(-1);
                    }
                }
            }
        });
        add(spinnerPosition);

        Option[] loopPositions = new Option[]{new Option(25, "25 %"), new Option(50, "50 %"), new Option(75, "75 %")};
        comboBoxPosition = new JComboBox<Option>(loopPositions);
        comboBoxPosition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int newValue = ((Option) comboBoxPosition.getSelectedItem()).getValue();
                if ((newValue != -1) && (model.getPosition() != newValue) || (!changedByUser)) {
                    model.setPosition(newValue);
                    changed();
                } else {
                    comboBoxPosition.setSelectedIndex(model.getPosition() + 1);
                }
            }
        });
        add(comboBoxPosition);

        /**************************************************************************/

        /**************************** DISTANCE SPINNER ****************************/
        lblDistance = new JLabel("Distance:");
        lblDistance.setHorizontalAlignment(SwingConstants.LEFT);
        lblDistance.setBounds(6, 117, 84, 14);
        add(lblDistance);

        MIN_SIZE = 0;
        MAX_SIZE = 99;
        String[] distances = new String[MAX_SIZE - MIN_SIZE + 2];
        distances[0] = new String(" ");
        for (int i = MIN_SIZE; i < MAX_SIZE + 1; i++)
            distances[i - MIN_SIZE + 1] = new String(i + " px");

        spinnerDistance = new JSpinner();
        spinnerDistance.setModel(new SpinnerListModel(distances));
        spinnerDistance.setBounds(101, 38, 50, 22);
        spinnerDistance.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                String value = (String) spinnerDistance.getValue();
                value = value.substring(0, value.indexOf(" "));
                try {
                    int newValue = Integer.parseInt(value);
                    if (model.getSpacing() != newValue) {
                        model.setSpacing(newValue);
                        changed();
                    }
                } catch (NumberFormatException e) {
                    if (changedByUser) {
                        if (model.getSpacing() != -1)
                            spinnerDistance.setValue(model.getSpacing() + " px");
                    } else {
                        spinnerDistance.setValue(" ");
                        model.setSpacing(-1);
                    }
                }
            }
        });
        add(spinnerDistance);
        /**************************************************************************/

        /**************************** PLACEMENT COMBOBOX **************************/
        lblPlace = new JLabel("Placement:");
        lblPlace.setHorizontalAlignment(SwingConstants.LEFT);
        lblPlace.setBounds(15, 142, 75, 14);
        add(lblPlace);

        Option[] labelTypes = new Option[]{new Option(PropertyModel.EMPTY, " "), new Option(LabelTopPlacement.BELOW.getValue(), "below"),
                new Option(LabelTopPlacement.ABOVE.getValue(), "above")};
        comboBoxPlace = new JComboBox<Option>(labelTypes);
        comboBoxPlace.setBounds(101, 139, 81, 20);
        comboBoxPlace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int newValue = ((Option) comboBoxPlace.getSelectedItem()).getValue();
                if ((newValue != -1) && (model.getTopPlacement() != newValue) || (!changedByUser)) {
                    model.setTopPlacement(newValue);
                    changed();
                } else {
                    comboBoxPlace.setSelectedIndex(model.getTopPlacement() + 1);
                }
            }
        });
        add(comboBoxPlace);
        /**************************************************************************/

        /**************************** ROTATION COMBOBOX ***************************/
        lblRotation = new JLabel("Rotation");
        lblRotation.setHorizontalAlignment(SwingConstants.LEFT);
        lblRotation.setBounds(6, 167, 84, 14);
        add(lblRotation);

        labelTypes[1] = new Option(LabelHorizontalPlacement.TANGENT.getValue(), "tangent");
        labelTypes[2] = new Option(LabelHorizontalPlacement.LEVEL.getValue(), "level");
        comboBoxRotation = new JComboBox<Option>(labelTypes);
        comboBoxRotation.setBounds(101, 164, 80, 20);
        comboBoxRotation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int newValue = ((Option) comboBoxRotation.getSelectedItem()).getValue();
                if ((newValue != -1) && (model.getHorizontalPlacement() != newValue) || (!changedByUser)) {
                    model.setHorizontalPlacement(newValue);
                    changed();
                } else {
                    comboBoxRotation.setSelectedIndex(model.getHorizontalPlacement() + 1);
                }
            }
        });
        add(comboBoxRotation);
        /**************************************************************************/

        /************************ USTAWIANIE BOUNDS *******************************/

        int spacing = 35;
        labels = new Vector<>();
        labels.add(labelText);
        labels.add(lblColor);
        labels.add(lblPosition);
        labels.add(lblDistance);
        labels.add(lblPlace);
        labels.add(lblRotation);

        for (int i = 0; i < labels.size(); i++)
            labels.get(i).setBounds(6, 22 + i * spacing, 84, 30);
        components = new Vector<>();
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
