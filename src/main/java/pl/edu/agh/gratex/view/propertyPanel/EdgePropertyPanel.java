package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


@SuppressWarnings("serial")
public class EdgePropertyPanel extends AbstractPropertyPanel {

    private EdgePropertyModel model;
    private JLabel lblLineType;
    private JComboBox<LineType> comboBoxLineType;
    private JLabel lblLineSize;
    private JSpinner spinnerLineSize;
    private JLabel lblLineColor;
    private JComboBox<Color> comboBoxLineColor;
    private JLabel lblDirect;
    private JComboBox<IsDirected> comboBoxDirect;
    private JLabel lblArrowType;
    private JComboBox<ArrowType> comboBoxArrowType;
    private JLabel lblAngle;
    private JSpinner spinnerAngle;
    private JComboBox<LoopDirection> comboBoxAngle;
    private static int RANGE;
    private static int STEP;
    private static int MIN_SIZE;
    private static int MAX_SIZE;

    private void changed() {
        if (changedByUser)
            ((PanelPropertyEditor) getParent()).valueChanged(model);
    }

    public EdgePropertyModel getModel() {
        return model;
    }

    public void setModel(PropertyModel pm) {
        changedByUser = false;
        model = (EdgePropertyModel) pm.getCopy();
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
        spinnerAngle.setVisible(true);
        comboBoxAngle.setVisible(false);
        if (model.getLoop() == IsLoop.YES) {
            spinnerAngle.setVisible(false);
            comboBoxAngle.setVisible(true);
            if (model.getRelativeEdgeAngle() >= 0) {
                comboBoxAngle.setSelectedIndex(model.getRelativeEdgeAngle() / 90 + 1);
            } else {
                comboBoxAngle.setSelectedIndex(0);
            }
        } else if (model.getRelativeEdgeAngle() == PropertyModel.EMPTY) {
            spinnerAngle.setValue(StringLiterals.EMPTY_VALUE);
        } else if (model.getRelativeEdgeAngle() > 180) {
            spinnerAngle.setValue(model.getRelativeEdgeAngle() - 360 + StringLiterals.DEG_SUFFIX);
        } else {
            spinnerAngle.setValue(model.getRelativeEdgeAngle() + StringLiterals.DEG_SUFFIX);
        }
        if (model.getLoop() == IsLoop.EMPTY) {
            spinnerAngle.setEnabled(false);
            lblAngle.setEnabled(false);
        }
        changedByUser = true;
    }

    @Override
    public void disableUnnecessaryFields() {
        super.disableUnnecessaryFields();
        spinnerAngle.setEnabled(false);
    }

    public EdgePropertyPanel() {
        model = new EdgePropertyModel();
        initialize();
        setModel(model);
    }

    private void initialize() {
        setLayout(null);

        /**************************** LINE TYPE COMBOBOX **************************/
        lblLineType = new JLabel(StringLiterals.EDGE_LINE_TYPE);
        lblLineType.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblLineType);

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
        lblLineSize = new JLabel(StringLiterals.EDGE_LINE_WIDTH);
        lblLineSize.setHorizontalAlignment(SwingConstants.LEFT);
        lblLineSize.setBounds(6, 117, 64, 14);
        add(lblLineSize);

        MIN_SIZE = 1;
        MAX_SIZE = 10;
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
        lblLineColor = new JLabel(StringLiterals.EDGE_LINE_COLOR);
        lblLineColor.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblLineColor);

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
        lblDirect = new JLabel(StringLiterals.EDGE_DIRECTED);
        lblDirect.setHorizontalAlignment(SwingConstants.LEFT);
        lblDirect.setBounds(6, 117, 84, 14);
        add(lblDirect);

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

        /**************************** LABEL INSIDE COMBOBOX ***********************/
        lblArrowType = new JLabel(StringLiterals.EDGE_ARROW_TYPE);
        lblArrowType.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblArrowType);

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

        /**************************** ANGLE SPINNER *******************************/
        lblAngle = new JLabel(StringLiterals.EDGE_ANGLE);
        lblAngle.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblAngle);

        RANGE = 60;
        STEP = 5;
        String[] angles = new String[RANGE / STEP * 2 + 2];
        angles[0] = StringLiterals.EMPTY_VALUE;
        for (int i = 0; i < RANGE / STEP * 2 + 1; i++) {
            angles[i + 1] = (i * STEP - RANGE) + StringLiterals.DEG_SUFFIX;
        }

        spinnerAngle = new JSpinner();
        spinnerAngle.setModel(new SpinnerListModel(angles));
        spinnerAngle.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                String value = (String) spinnerAngle.getValue();
                value = value.substring(0, value.indexOf(" "));
                try {
                    int tmp = Integer.parseInt(value);
                    if (tmp < 0)
                        tmp += 360;
                    if (tmp != model.getRelativeEdgeAngle()) {
                        model.setRelativeEdgeAngle(tmp);
                        changed();
                    }
                } catch (NumberFormatException e) {
                    if (changedByUser) {
                        if (model.getRelativeEdgeAngle() != PropertyModel.EMPTY)
                            spinnerAngle.setValue((model.getRelativeEdgeAngle() >= 0 ? model.getRelativeEdgeAngle() : model.getRelativeEdgeAngle() + 360) + StringLiterals.DEG_SUFFIX);
                    } else {
                        spinnerAngle.setValue(StringLiterals.EMPTY_VALUE);
                        model.setRelativeEdgeAngle(PropertyModel.EMPTY);
                    }
                }

            }
        });
        add(spinnerAngle);

        comboBoxAngle = new JComboBox<>(LoopDirection.values());
        comboBoxAngle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                LoopDirection newValue = ((LoopDirection) comboBoxAngle.getSelectedItem());
                if ((!newValue.isEmpty()) && (model.getRelativeEdgeAngle() != newValue.getAngle()) || (!changedByUser)) {
                    model.setRelativeEdgeAngle(newValue.getAngle());
                    changed();
                } else {
                    comboBoxAngle.setSelectedItem(LoopDirection.getByAngle(model.getRelativeEdgeAngle()));
                }
            }
        });
        add(comboBoxAngle);
        /**************************************************************************/

        /************************ USTAWIANIE BOUNDS *******************************/

        int spacing = 35;
        labels = new ArrayList<>();
        labels.add(lblLineType);
        labels.add(lblLineSize);
        labels.add(lblLineColor);
        labels.add(lblDirect);
        labels.add(lblArrowType);
        labels.add(lblAngle);

        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).setBounds(6, 22 + i * spacing, 84, 30);
        }

        components = new ArrayList<>();
        components.add(comboBoxLineType);
        components.add(spinnerLineSize);
        components.add(comboBoxLineColor);
        components.add(comboBoxDirect);
        components.add(comboBoxArrowType);
        components.add(spinnerAngle);

        for (int i = 0; i < components.size(); i++) {
            components.get(i).setBounds(90, 24 + i * spacing, 80, 25);
        }
        comboBoxAngle.setBounds(spinnerAngle.getBounds());
        comboBoxAngle.setVisible(false);
    }

}
