package pl.edu.agh.gratex.property;

import pl.edu.agh.gratex.controller.ModeController;
import pl.edu.agh.gratex.controller.ModeListener;
import pl.edu.agh.gratex.graph.GraphElementType;
import pl.edu.agh.gratex.model.PropertyModel;

import javax.swing.*;
import java.util.EnumMap;

public class PanelPropertyEditor extends JPanel implements ModeListener {
    private static final long serialVersionUID = -4423821822436250916L;

    //private int mode = 1;
    private GraphElementType mode = GraphElementType.VERTEX;
    private JLabel label_title;
    private EnumMap<GraphElementType, AbstractPropertyPanel> panelsMap = new EnumMap<GraphElementType, AbstractPropertyPanel>(GraphElementType.class);

    public PanelPropertyEditor(ModeController modeController) {
        modeController.addModeListener(this);
        initialize();
        setEnabled(false);
    }

    public void valueChanged(PropertyModel model) {
        pl.edu.agh.gratex.gui.ControlManager.updateSelectedItemsModel(model);
    }

    //TODO maybe move from here
    private AbstractPropertyPanel createPropertyPanel(GraphElementType graphElementType) {
        switch (graphElementType) {
            case VERTEX:
                return new VertexPropertyPanel();
            case EDGE:
                return new EdgePropertyPanel();
            case LABEL_VERTEX:
                return new LabelVertexPropertyPanel();
            case LABEL_EDGE:
                return new LabelEdgePropertyPanel();
            default:
                return null;
        }
    }

    private void initialize() {
        setLayout(null);
        label_title = new JLabel("Property editor");
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        label_title.setBounds(10, 11, 180, 14);
        add(label_title);

        for(GraphElementType graphElementType :GraphElementType.values()) {
            AbstractPropertyPanel propertyPanel = createPropertyPanel(graphElementType);
            panelsMap.put(graphElementType, propertyPanel);
            propertyPanel.setVisible(false);
            propertyPanel.setBounds(10, 30, 180, 340);
            add(propertyPanel);
        }
        panelsMap.get(mode).setVisible(true);
    }

    public void setModel(PropertyModel pm) {
        panelsMap.get(mode).setModel(pm);
    }

    public void setEnabled(boolean flag) {
        panelsMap.get(mode).setEnabled(flag);
        if (flag) {
            panelsMap.get(mode).components.get(0).requestFocus();
        }
    }

    public void setMode(int m) {
        panelsMap.get(mode).setVisible(false);
        mode = GraphElementType.values()[m-1];
        panelsMap.get(mode).setVisible(true);
    }

    public PropertyModel getModel() {
        return panelsMap.get(mode).getModel();
    }

    public void giveFocusToTextField() {
        panelsMap.get(mode).components.get(0).requestFocus();
        ((JTextField) panelsMap.get(mode).components.get(0)).selectAll();
    }

    public void disableLabelEdition() {
        disableLabelEdition(GraphElementType.LABEL_EDGE);
        disableLabelEdition(GraphElementType.LABEL_VERTEX);
    }

    private void disableLabelEdition(GraphElementType type) {
        panelsMap.get(type).components.get(0).setEnabled(false);
        panelsMap.get(type).components.get(0).setFocusable(false);
    }

    @Override
    public void fireModeChanged(GraphElementType previousMode, GraphElementType currentMode) {
        panelsMap.get(previousMode).setVisible(false);
        panelsMap.get(currentMode).setVisible(true);
        mode = currentMode;
    }
}
