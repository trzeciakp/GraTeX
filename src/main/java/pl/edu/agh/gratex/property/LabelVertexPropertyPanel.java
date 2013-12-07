package pl.edu.agh.gratex.property;

import pl.edu.agh.gratex.model.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.LabelPosition;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


public class LabelVertexPropertyPanel extends AbstractPropertyPanel {
    private static final long serialVersionUID = -8920073345520413341L;

    private LabelVertexPropertyModel model;
    private JLabel labelText;
    private JTextField textField;
    private JLabel lblColor;
    private JComboBox<Color> comboBoxFontColor;
    private JLabel lblPosition;
    private JComboBox<Option> comboBoxPosition;
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
        model = new LabelVertexPropertyModel((LabelVertexPropertyModel) pm);
        textField.setText(model.text);
        comboBoxFontColor.setSelectedItem(model.fontColor);
        comboBoxPosition.setSelectedIndex(model.position + 1);
        if (model.spacing == -1)
            spinnerDistance.setValue(" ");
        else
            spinnerDistance.setValue(model.spacing + " px");

        changedByUser = true;
    }

    public LabelVertexPropertyPanel() {
        model = new LabelVertexPropertyModel();
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
                if (!textField.getText().equals(model.text)) {
                    model.text = textField.getText();
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
                        if (!newValue.equals(model.fontColor)) {
                            model.fontColor = newValue;
                            changed();
                        }
                    } else {
                        comboBoxFontColor.setSelectedItem(model.fontColor);
                    }
                }
                repaint();
            }
        });
        add(comboBoxFontColor);
        /**************************************************************************/

        /**************************** POSITION COMBOBOX ***************************/
        lblPosition = new JLabel("Position:");
        lblPosition.setHorizontalAlignment(SwingConstants.LEFT);
        lblPosition.setBounds(26, 92, 64, 14);
        add(lblPosition);

        Option[] positions = new Option[]{new Option(PropertyModel.EMPTY, " "), new Option(LabelPosition.N.getValue(), "N"),
                new Option(LabelPosition.NE.getValue(), "NE"), new Option(LabelPosition.E.getValue(), "E"), new Option(LabelPosition.SE.getValue(), "SE"),
                new Option(LabelPosition.S.getValue(), "S"), new Option(LabelPosition.SW.getValue(), "SW"), new Option(LabelPosition.W.getValue(), "W"),
                new Option(LabelPosition.NW.getValue(), "NW")};

        comboBoxPosition = new JComboBox<Option>(positions);
        comboBoxPosition.setBounds(101, 89, 80, 20);
        comboBoxPosition.setMaximumRowCount(9);
        comboBoxPosition.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int newValue = ((Option) comboBoxPosition.getSelectedItem()).getValue();
                if ((newValue != -1) && (model.position != newValue) || (!changedByUser)) {
                    model.position = newValue;
                    changed();
                } else {
                    comboBoxPosition.setSelectedIndex(model.position + 1);
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
                    if (model.spacing != newValue) {
                        model.spacing = newValue;
                        changed();
                    }
                } catch (NumberFormatException e) {
                    if (changedByUser) {
                        if (model.spacing != -1)
                            spinnerDistance.setValue(model.spacing + " px");
                    } else {
                        spinnerDistance.setValue(" ");
                        model.spacing = -1;
                    }
                }
            }
        });
        add(spinnerDistance);

        /**************************************************************************/

        /************************ USTAWIANIE BOUNDS *******************************/

        int spacing = 35;
        labels = new Vector<JComponent>();
        labels.add(labelText);
        labels.add(lblColor);
        labels.add(lblPosition);
        labels.add(lblDistance);

        for (int i = 0; i < labels.size(); i++)
            labels.get(i).setBounds(6, 22 + i * spacing, 84, 30);
        components = new Vector<JComponent>();
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
