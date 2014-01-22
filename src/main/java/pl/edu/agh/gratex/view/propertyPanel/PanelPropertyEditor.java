package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.controller.operation.*;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@SuppressWarnings("serial")
public class PanelPropertyEditor extends JScrollPane implements ModeListener, OperationListener, SelectionListener {
    GeneralController generalController;

    //private int mode = 1;
    private ModeType mode = ModeType.VERTEX;
    private JLabel label_title;
    private EnumMap<ModeType, AbstractPropertyPanel> panelsMap = new EnumMap<>(ModeType.class);
    private List<? extends GraphElement> selection;

    public PanelPropertyEditor(GeneralController generalController, ModeController modeController, OperationController operationController, SelectionController selectionController) {
        this.generalController = generalController;
        modeController.addModeListener(this);

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
        panelsMap.get(mode).setModel(model);
        operation.finish(true);
    }

    private AbstractPropertyPanel createPropertyPanel(ModeType modeType, PanelPropertyEditor parent) {
        AbstractPropertyPanel result;
        switch (modeType) {
            case VERTEX:
                result = new VertexPropertyPanel(generalController);
				break;
            case EDGE:
                result = new EdgePropertyPanel();
				break;
            case LABEL_VERTEX:
                result = new LabelVertexPropertyPanel();
				break;
            case LABEL_EDGE:
                result = new LabelEdgePropertyPanel();
				break;
            case HYPEREDGE:
                result = new HyperedgePropertyPanel();
				break;
            case BOUNDARY:
                result = new BoundaryPropertyPanel();
				break;
            case LINK_BOUNDARY:
                result = new LinkBoundaryPropertyPanel();
				break;
            default:
                result = null;
				break;
        }
        result.setParent(parent);
        return result;
    }

    private void initialize() {
        setBorder(new TitledBorder("Property editor"));
        //setBorder(new BevelBorder(1));
//        label_title = new JLabel("Property editor");
//        label_title.setHorizontalAlignment(SwingConstants.CENTER);
//        label_title.setBounds(10, 11, 180, 14);
//        add(label_title);


        //scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        for (ModeType modeType : ModeType.values()) {
            AbstractPropertyPanel propertyPanel = createPropertyPanel(modeType, this);
            panelsMap.put(modeType, propertyPanel);
            propertyPanel.setVisible(true);
            propertyPanel.setPreferredSize(new Dimension(165, propertyPanel.getHeight()));
            //propertyPanel.setBounds(10, 30, 180, propertyPanel.getHeight());
            //scrollPane.add(propertyPanel);
        }
        setViewportView(panelsMap.get(mode));
        //setBorder(BorderFactory.createEmptyBorder());
        //scrollPane.
        //setViewportBorder(null);
        //panelsMap.get(mode).setVisible(true);
    }

    public void setModel(PropertyModel pm) {
        panelsMap.get(mode).setModel(pm);
    }

    public void setEnabled(boolean flag) {
        panelsMap.get(mode).setEnabled(flag);
        /*if (flag) {
            panelsMap.get(mode).focusFirstElement();
        }*/
    }

    public PropertyModel getModel() {
        return panelsMap.get(mode).getModel();
    }

    public void disableUnnecessaryFields() {
        for (AbstractPropertyPanel abstractPropertyPanel : panelsMap.values()) {
            abstractPropertyPanel.disableUnnecessaryFields();
        }

        //disableLabelEdition(ModeType.LABEL_EDGE);
        //disableLabelEdition(ModeType.LABEL_VERTEX);
    }

    private void disableLabelEdition(ModeType type) {
        panelsMap.get(type).components.get(0).setEnabled(false);
        panelsMap.get(type).components.get(0).setFocusable(false);
    }

    // ===================================
    // ModeListener implementation
    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        //panelsMap.get(previousMode).setVisible(false);
        //panelsMap.get(currentMode).setVisible(true);
        setViewportView(panelsMap.get(currentMode));

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
                panelsMap.get(mode).components.get(0).requestFocus();
                ((JTextField) panelsMap.get(mode).components.get(0)).selectAll();
            }
        }
    }

    @Override
    public void selectionChanged(List<? extends GraphElement> selectedElements) {
        selection = selectedElements;
        if (selectedElements.size() == 0) {
            setEnabled(false);
            setModel(PropertyModel.andOpertarorList(mode.getRelatedElementType(), new ArrayList<GraphElement>()));
        } else {
            setEnabled(true);
            setModel(PropertyModel.andOpertarorList(mode.getRelatedElementType(), selectedElements));
        }
    }
}
