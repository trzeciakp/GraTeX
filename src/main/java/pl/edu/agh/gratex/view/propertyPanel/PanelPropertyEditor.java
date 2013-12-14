package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.model.PropertyModel;

import javax.swing.*;
import java.util.EnumMap;

@SuppressWarnings("serial")
public class PanelPropertyEditor extends JPanel implements ModeListener, OperationListener {
    GeneralController generalController;

    //private int mode = 1;
    private ModeType mode = ModeType.VERTEX;
    private JLabel label_title;
    private EnumMap<ModeType, AbstractPropertyPanel> panelsMap = new EnumMap<>(ModeType.class);

    public PanelPropertyEditor(GeneralController generalController, ModeController modeController, OperationController operationController) {
        this.generalController = generalController;
        modeController.addModeListener(this);

        // TODO To jest potrzebne, bo z GraphTemplateEditora tworzymy anonimowy PanelPropertyEditor i wtedy nie chcemy, zeby byl on listenerem
        if (operationController != null) {
            operationController.addOperationListener(this);
        }

        initialize();
        setEnabled(false);
    }

    public void valueChanged(PropertyModel model) {
        // TODO to bedzie jakies wywolanie general controllera albo innego wariata
        pl.edu.agh.gratex.view.ControlManager.updateSelectedItemsModel(model);
    }

    //TODO maybe move from here
    private AbstractPropertyPanel createPropertyPanel(ModeType modeType) {
        switch (modeType) {
            case VERTEX:
                return new VertexPropertyPanel(generalController);
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

        for (ModeType modeType : ModeType.values()) {
            AbstractPropertyPanel propertyPanel = createPropertyPanel(modeType);
            panelsMap.put(modeType, propertyPanel);
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
        mode = ModeType.values()[m - 1];
        panelsMap.get(mode).setVisible(true);
    }

    public PropertyModel getModel() {
        return panelsMap.get(mode).getModel();
    }

    public void disableLabelEdition() {
        disableLabelEdition(ModeType.LABEL_EDGE);
        disableLabelEdition(ModeType.LABEL_VERTEX);
    }

    private void disableLabelEdition(ModeType type) {
        panelsMap.get(type).components.get(0).setEnabled(false);
        panelsMap.get(type).components.get(0).setFocusable(false);
    }

    // ===================================
    // ModeListener implementation
    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        panelsMap.get(previousMode).setVisible(false);
        panelsMap.get(currentMode).setVisible(true);
        mode = currentMode;
    }

    @Override
    public int modeUpdatePriority() {
        return 0;
    }

    // ===================================
    // OperationListener implementation
    @Override
    public void startOperationEvent(String info) {
    }

    @Override
    public void finishOperationEvent(Operation operation) {
        if (operation.getOperationType() == OperationType.ADD_LABEL_EDGE || operation.getOperationType() == OperationType.ADD_LABEL_VERTEX) {
            // TODO Na razie to nie dziala, bo zaraz po tym jest robiony updatePropertyChangeOperation i sie nadpisuje. Ale bedzie dzialac.
            panelsMap.get(mode).components.get(0).requestFocus();
            ((JTextField) panelsMap.get(mode).components.get(0)).selectAll();
        }
    }

    @Override
    public void genericOperationEvent(String info) {
    }
}
