package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.controller.operation.GenericOperation;
import pl.edu.agh.gratex.controller.operation.OperationController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.DummySubgraph;
import pl.edu.agh.gratex.model.graph.GraphUtils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;


public class MouseControllerImpl implements MouseController, ToolListener, ModeListener {

    private EnumMap<ModeType, GraphElementMouseController> controllers = new EnumMap<>(ModeType.class);
    private ModeType mode = ModeType.VERTEX;
    private ToolType tool = ToolType.ADD;
    private SelectionController selectionController;
    private OperationController operationController;

    private boolean mousePressed;
    private boolean isElementMoving;
    private DummySubgraph dummySubgraph;
    private GeneralController generalController;
    private int mousePressX;
    private int mousePressY;
    private int mouseY;
    private int mouseX;

    public MouseControllerImpl(GeneralController generalController, ModeController modeController, ToolController toolController,
                               SelectionController selectionController, OperationController operationController, GraphElementControllersFactory factory) {
        this.generalController = generalController;
        this.selectionController = selectionController;
        this.operationController = operationController;
        modeController.addModeListener(this);
        toolController.addToolListener(this);
        for (ModeType modeType : ModeType.values()) {
            controllers.put(modeType, factory.createGraphElementMouseController(modeType));
        }
    }

    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        cancelCurrentOperation();
        mode = currentMode;
        operationController.reportOperationEvent(null);
        controllers.get(mode).setMouseLocation(mouseX, mouseY);
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
                selectionController.clearSelection();
                controllers.get(mode).addNewElement(mouseX, mouseY);
                break;
            case REMOVE:
                controllers.get(mode).removeElement(mouseX, mouseY);
                break;
            case SELECT:
                if (dummySubgraph != null) {
                    if (dummySubgraph.fitsIntoPosition()) {
                        new CreationRemovalOperation(generalController, dummySubgraph.getElements(), OperationType.DUPLICATION,
                                StringLiterals.INFO_SUBGRAPH_DUPLICATE, true);
                    } else {
                        operationController.reportOperationEvent(new GenericOperation(StringLiterals.INFO_SUBGRAPH_CANNOT_PASTE));
                    }
                    dummySubgraph = null;
                } else if (!mousePressed) {
                    selectionController.addToSelection(controllers.get(mode).getElementFromPosition(mouseX, mouseY), e.isControlDown());
                }
                break;
        }
    }

    @Override
    public void processMousePressing(MouseEvent e) {
        mousePressed = true;
        mousePressX = e.getX();
        mousePressY = e.getY();
        GraphElement element = controllers.get(mode).getElementFromPosition(mouseX, mouseY);
        boolean isProperTool = (tool == ToolType.ADD || tool == ToolType.SELECT);
        if (isProperTool && element != null && selectionController.selectionContains(element)) {
            controllers.get(mode).moveSelection(mouseX, mouseY);
            isElementMoving = true;
        }
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
            controllers.get(mode).finishMoving();
            selectionController.repeatSelection();
        }

        operationController.reportOperationEvent(null);
    }

    @Override
    public void processMouseMoving(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        controllers.get(mode).setMouseLocation(mouseX, mouseY);
        operationController.reportOperationEvent(null);
    }

    @Override
    public void processMouseDragging(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        controllers.get(mode).setMouseLocation(mouseX, mouseY);
        if (isElementMoving) {
            controllers.get(mode).moveSelection(mouseX, mouseY);
        }
        operationController.reportOperationEvent(null);
    }

    @Override
    public void processShiftPressing(boolean flag) {
        controllers.get(mode).setShiftDown(flag);
        operationController.reportOperationEvent(null);
    }

    @Override
    public void processCtrlPressing(boolean flag) {
        controllers.get(mode).setCtrlDown(flag);
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
    public List<GraphElement> getCurrentlyAddedElements() {
        if (tool == ToolType.ADD) {
            return controllers.get(mode).getCurrentlyAddedElements();
        } else {
            return new LinkedList<>();
        }
    }

    @Override
    public List<GraphElement> getCopiedSubgraph() {
        if (dummySubgraph != null) {
            dummySubgraph.calculatePositions(mouseX, mouseY);
            if (dummySubgraph.fitsIntoPosition()) {
                return dummySubgraph.getElements();
            }
        }
        return new LinkedList<>();
    }

    @Override
    public Point locationOfCannotCopyIcon() {
        if (dummySubgraph != null) {
            if (!dummySubgraph.fitsIntoPosition()) {
                return new Point(mouseX, mouseY);
            }
        }
        return null;
    }

    @Override
    public void cancelCurrentOperation() {
        controllers.get(mode).reset();
        isElementMoving = false;
        mousePressed = false;
        dummySubgraph = null;
    }

    @Override
    public void duplicateSubgraph() {
        dummySubgraph = new DummySubgraph(generalController.getGraph(), selectionController.getSelection(), generalController.getParseController());
        operationController.reportOperationEvent(new GenericOperation(StringLiterals.INFO_SUBGRAPH_WHERE_TO_PASTE));
    }
}
