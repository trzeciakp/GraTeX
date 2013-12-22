package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.controller.operation.*;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@SuppressWarnings("serial")
public class PanelPropertyEditor extends JPanel implements ModeListener, OperationListener, SelectionListener {
    GeneralController generalController;

    //private int mode = 1;
    private ModeType mode = ModeType.VERTEX;
    private JLabel label_title;
    private EnumMap<ModeType, AbstractPropertyPanel> panelsMap = new EnumMap<>(ModeType.class);
    private List<? extends GraphElement> selection;

    public PanelPropertyEditor(GeneralController generalController, ModeController modeController, OperationController operationController, SelectionController selectionController) {
        this.generalController = generalController;
        modeController.addModeListener(this);

        // TODO To jest potrzebne, bo z GraphTemplateEditora tworzymy anonimowy PanelPropertyEditor i wtedy nie chcemy, zeby byl on listenerem
        if (operationController != null) {
            operationController.addOperationListener(this);
        }

        if (selectionController != null) {
            selectionController.addListener(this);
        }

        initialize();
        setEnabled(false);
    }

    public void valueChanged(PropertyModel model) {
        AlterationOperation operation = new AlterationOperation(generalController, selection, OperationType.PROPERTY_CHANGE, StringLiterals.INFO_PROPERTY_CHANGE);
        for (GraphElement graphElement : selection) {
            graphElement.setModel(model);
        }
        operation.finish();
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
    public void operationEvent(Operation operation) {
        if (operation != null) {
            if (operation.getOperationType() == OperationType.ADD_LABEL_EDGE || operation.getOperationType() == OperationType.ADD_LABEL_VERTEX) {
                // TODO Na razie to nie dziala, bo zaraz po tym jest robiony updatePropertyChangeOperation i sie nadpisuje. Ale bedzie dzialac.
                panelsMap.get(mode).components.get(0).requestFocus();
                ((JTextField) panelsMap.get(mode).components.get(0)).selectAll();
            }
        }
    }

    @Override
    public void selectionChanged(List<? extends GraphElement> collection) {
        selection = collection;
        if (collection.size() == 0) {
            setEnabled(false);
            setModel(PropertyModel.andOpertarorList(mode.getRelatedElementType(), new ArrayList<GraphElement>()));
        } else {
            setEnabled(true);
            setModel(PropertyModel.andOpertarorList(mode.getRelatedElementType(), collection));
        }
    }
}
