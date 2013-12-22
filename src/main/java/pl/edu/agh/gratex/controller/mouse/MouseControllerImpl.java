package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.controller.operation.OperationController;
import pl.edu.agh.gratex.editor.CopyPasteOperation;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.GraphUtils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EnumMap;

/**
 *
 */
public class MouseControllerImpl implements MouseController, ToolListener, ModeListener {

    private EnumMap<ModeType, GraphElementMouseController> controllers = new EnumMap<>(ModeType.class);
    private ModeType mode = ModeType.VERTEX;
    private ToolType tool = ToolType.ADD;
    private SelectionController selectionController;
    private OperationController operationController;

    private boolean mousePressed;
    private boolean isElementMoving;
    private CopyPasteOperation currentCopyPasteOperation;
    private GeneralController generalController;
    private int mousePressX;
    private int mousePressY;
    private int mouseY;
    private int mouseX;

    public MouseControllerImpl(GeneralController generalController, ModeController modeController, ToolController toolController, SelectionController selectionController, OperationController operationController) {
        this.generalController = generalController;
        this.selectionController = selectionController;
        this.operationController = operationController;
        modeController.addModeListener(this);
        toolController.addToolListener(this);
        controllers.put(ModeType.VERTEX, new VertexMouseControllerImpl(generalController));
        controllers.put(ModeType.EDGE, new EdgeMouseControllerImpl(generalController));
        controllers.put(ModeType.LABEL_VERTEX, new LabelVertexMouseControllerImpl(generalController));
        controllers.put(ModeType.LABEL_EDGE, new LabelEdgeMouseControllerImpl(generalController));
    }

    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        cancelCurrentOperation();
        mode = currentMode;

        operationController.reportOperationEvent(null);
    }

    @Override
    public int modeUpdatePriority() {
        return 0;
    }

    @Override
    public void toolChanged(ToolType previousTool, ToolType currentTool) {
        cancelCurrentOperation();
        tool = currentTool;

        operationController.reportOperationEvent(null);
    }

    @Override
    public int toolUpdatePriority() {
        return 0;
    }


    @Override
    public void processMouseClicking(MouseEvent e) {
        switch (tool) {
            case ADD:
                controllers.get(mode).addNewElement(e);
                break;
            case REMOVE:
                controllers.get(mode).removeElement(e);
                break;
            case SELECT:
                if (!mousePressed) {
                    selectionController.addToSelection(controllers.get(mode).getElementFromPosition(e), e.isControlDown());
                }
                break;
        }
    }

    @Override
    public void processMousePressing(MouseEvent e) {
        mousePressed = true;
        mousePressX = e.getX();
        mousePressY = e.getY();
        GraphElement element = controllers.get(mode).getElementFromPosition(e);
        boolean isProperTool = (tool == ToolType.ADD || tool == ToolType.SELECT);
        if (isProperTool && element != null && selectionController.selectionContains(element)) {
            controllers.get(mode).moveSelection(e);
            isElementMoving = true;
        }

        //operationController.reportOperationEvent(null);
    }

    @Override
    public void processMouseReleasing(MouseEvent e) {
        Rectangle selectionArea = getSelectionArea();
        mousePressed = false;
        if (selectionArea != null) {
            if (selectionArea.width + selectionArea.height > 2) {
                if (tool == ToolType.REMOVE) {
                    selectionController.addToSelection(GraphUtils.getIntersectingElements(generalController.getGraph(), mode, selectionArea), false);
                    generalController.deleteSelection();
                } else if (tool == ToolType.SELECT) {
                    selectionController.addToSelection(GraphUtils.getIntersectingElements(generalController.getGraph(), mode, selectionArea), e.isControlDown());
                }
            }
        } else {
            isElementMoving = false;
            controllers.get(mode).finishMovingElement();
        }

        operationController.reportOperationEvent(null);
    }

    @Override
    public void processMouseMoving(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        controllers.get(mode).processMouseMoving(tool, e);

        operationController.reportOperationEvent(null);
    }

    @Override
    public void processMouseDragging(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        controllers.get(mode).processMouseDragging(tool, e);
        if (isElementMoving) {
            controllers.get(mode).moveSelection(e);
        }

        operationController.reportOperationEvent(null);
    }

    @Override
    public void processShiftPressing(boolean flag) {
        controllers.get(mode).processShiftPressing(flag);
        operationController.reportOperationEvent(null);
    }

    @Override
    public Rectangle getSelectionArea() {
        boolean isProperTool = (tool == ToolType.REMOVE || tool == ToolType.SELECT);
        if (!isElementMoving && isProperTool) {
            if (mousePressed && (mouseX != mousePressX || mouseY != mousePressY)) {
                int x = Math.min(mousePressX, mouseX);
                int width = Math.abs(mouseX - mousePressX);
                int y = Math.min(mousePressY, mouseY);
                int height = Math.abs(mouseY - mousePressY);
                return new Rectangle(x, y, width, height);
            }
        }

        return null;
    }

    @Override
    public void paintCurrentlyAddedElement(Graphics2D g) {
        if (tool == ToolType.ADD) {
            controllers.get(mode).paintCurrentlyAddedElement(g);
        }

        operationController.reportOperationEvent(null);
    }

    @Override
    public void paintCopiedSubgraph(Graphics2D g) {

    }

    @Override
    public boolean isEdgeCurrentlyAdded(Edge edge) {
        //TODO
        return ((EdgeMouseControllerImpl) controllers.get(ModeType.EDGE)).getCurrentlyAddedEdge() == edge;
    }

    @Override
    public void cancelCurrentOperation() {
        controllers.get(mode).reset();
        isElementMoving = false;
        mousePressed = false;
        if (currentCopyPasteOperation != null) {
            currentCopyPasteOperation = currentCopyPasteOperation.getCopy();
        }
    }


    //===========================================
    // To be removed

    @Override
    public void copyToClipboard() {
        currentCopyPasteOperation = new CopyPasteOperation(generalController, generalController.getSelectionController().getSelection());
        //generalController.updateMenuBarAndActions();
        generalController.publishInfo(StringLiterals.INFO_SUBGRAPH_COPY);
        generalController.getClipboardController().setPastingEnabled(true);
    }

    @Override
    public void pasteFromClipboard() {
        if (!currentCopyPasteOperation.pasting) {
            generalController.getModeController().setMode(ModeType.VERTEX);
            generalController.getToolController().setTool(ToolType.SELECT);
            currentCopyPasteOperation.startPasting();
            generalController.publishInfo(StringLiterals.INFO_SUBGRAPH_WHERE_TO_PASTE);
        }
    }
}
