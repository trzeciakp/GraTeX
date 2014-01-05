package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.boundary.BoundaryPropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class BoundaryPropertyPanel extends AbstractPropertyPanel {
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 10;
    private ColorComboBox comboBoxFillColor;
    private BoundaryPropertyModel model;
    private JComboBox<LineType> comboBoxLineType;
    private JSpinner spinnerLineSize;
    private JLabel lblLineColor;
    private ColorComboBox comboBoxLineColor;
    private JLabel lblLineWidth;

    @Override
    public PropertyModel getModel() {
        return model;
    }

    @Override
    public void setModel(PropertyModel pm) {
        model = (BoundaryPropertyModel) pm;
        changedByUser = false;
        comboBoxFillColor.setSelectedItem(model.getFillColor());
        comboBoxLineColor.setSelectedItem(model.getLineColor());
        int lineWidth = model.getLineWidth();
        if(lineWidth == 0 || lineWidth == PropertyModel.EMPTY) {
            spinnerLineSize.setValue(StringLiterals.EMPTY_VALUE);
        } else {
            spinnerLineSize.setValue(lineWidth + StringLiterals.PX_SUFFIX);
        }
        comboBoxLineType.setSelectedItem(model.getLineType());
        if(model.getLineType() == LineType.NONE || model.getLineType().isEmpty()) {
            lblLineColor.setEnabled(false);
            lblLineWidth.setEnabled(false);
            spinnerLineSize.setEnabled(false);
            comboBoxLineColor.setEnabled(false);
        } else {
            lblLineColor.setEnabled(true);
            lblLineWidth.setEnabled(true);
            spinnerLineSize.setEnabled(true);
            comboBoxLineColor.setEnabled(true);
        }


        changedByUser = true;
    }

    private void changed() {
        if (changedByUser) {
            ((PanelPropertyEditor) getParent()).valueChanged(model);
        }
    }

    @Override
    protected void initialize() {
        setLayout(null);

        /**************************** BOUNDARY FILL COLOR COMBOBOX ***********************/
        createJLabel(StringLiterals.BOUNDARY_FILL_COLOR);

        comboBoxFillColor = new ColorComboBox();
        comboBoxFillColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (changedByUser) {
                    Color newValue = (Color) comboBoxFillColor.getSelectedItem();
                    if (newValue != null) {
                        if (!newValue.equals(model.getFillColor())) {
                            model.setFillColor(newValue);
                            changed();
                        }
                    } else {
                        comboBoxFillColor.setSelectedItem(model.getFillColor());
                    }
                }
                repaint();
            }
        });
        add(comboBoxFillColor);
        /**************************************************************************/

        /**************************** LINE TYPE COMBOBOX **************************/
        createJLabel(StringLiterals.BOUNDARY_LINE_TYPE);

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
        lblLineWidth = createJLabel(StringLiterals.VERTEX_LINE_WIDTH);

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

        components.add(comboBoxFillColor);
        components.add(comboBoxLineType);
        components.add(spinnerLineSize);
        components.add(comboBoxLineColor);
    }
}
