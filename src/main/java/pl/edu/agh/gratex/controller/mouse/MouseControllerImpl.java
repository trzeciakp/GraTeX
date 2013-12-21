package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.controller.operation.OperationController;
import pl.edu.agh.gratex.editor.CopyPasteOperation;
import pl.edu.agh.gratex.editor.DragOperation;
import pl.edu.agh.gratex.editor.RemoveOperation;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.labelV.LabelVUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
import pl.edu.agh.gratex.view.ControlManager;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class MouseControllerImpl implements MouseController, ModeListener, ToolListener, Serializable {
    private GeneralController generalController;
    private OperationController operationController;
    private ModeType mode = ModeType.VERTEX;
    private ToolType tool = ToolType.ADD;

    private DragOperation currentDragOperation = null;
    private CopyPasteOperation currentCopyPasteOperation = null;
    private Edge currentlyAddedEdge = null;
    private Vertex edgeDragDummy = null;
    private boolean changingEdgeAngle;
    private GraphElement currentlyMovedElement = null;

    private boolean shiftDown;
    private boolean mousePressed;
    private int mousePressX;
    private int mousePressY;
    private int mouseX;
    private int mouseY;

    public MouseControllerImpl(GeneralController generalController, OperationController operationController, ModeController modeController, ToolController toolController) {
        this.generalController = generalController;
        this.operationController = operationController;
        modeController.addModeListener(this);
        toolController.addToolListener(this);
    }

    //===========================================
    // Listeners implementation

    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        mode = currentMode;
    }

    @Override
    public int modeUpdatePriority() {
        return 0;
    }

    @Override
    public void toolChanged(ToolType previousTool, ToolType currentTool) {
        tool = currentTool;
    }

    @Override
    public int toolUpdatePriority() {
        return 0;
    }


    //===========================================
    // MouseController implementation

    @Override
    public void cancelCurrentOperation() {
        if (currentCopyPasteOperation != null) {
            currentCopyPasteOperation = currentCopyPasteOperation.getCopy();
        }
        currentDragOperation = null;
        currentlyAddedEdge = null;
        currentlyMovedElement = null;

        operationController.reportGenericOperation(null);
    }

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
/*
    @Override
    public boolean clipboardNotEmpty() {
        return currentCopyPasteOperation != null;
    }*/

    @Override
    public void paintCopiedSubgraph(Graphics2D g) {
        if (currentCopyPasteOperation != null) {
            if (currentCopyPasteOperation.pasting) {
                currentCopyPasteOperation.targetX = mouseX;
                currentCopyPasteOperation.targetY = mouseY;
                currentCopyPasteOperation.calculatePosition();
                if (currentCopyPasteOperation.fitsIntoPosition()) {
                    currentCopyPasteOperation.drawDummySubgraph(g);
                }
            }
        }
    }

    @Override
    public void processShiftPressing(boolean flag) {
        shiftDown = flag;
        if (currentlyAddedEdge != null) {
            currentlyAddedEdge.setDirected(flag);
        }
        if (currentlyMovedElement != null) {
            if (currentlyMovedElement instanceof Edge) {
                ((Edge) currentlyMovedElement).setDirected(flag);
            }
            if (currentlyMovedElement instanceof LabelE) {
                ((LabelE) currentlyMovedElement).setHorizontalPlacement(!flag);
            }
        }
        operationController.reportGenericOperation(null);
    }

    @Override
    public boolean isEdgeCurrentlyAdded(Edge edge) {
        return edge == currentlyAddedEdge;
    }

    @Override
    public Rectangle getSelectionArea() {
        if (tool != ToolType.ADD && currentlyMovedElement == null) {
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
    public void finishMovingElement() {
        if (currentlyMovedElement != null) {
            if (currentlyMovedElement instanceof Vertex) {
                currentDragOperation.setEndPos(((Vertex) currentlyMovedElement).getPosX(), ((Vertex) currentlyMovedElement).getPosY());
                if (currentDragOperation.changeMade()) {
                    ControlManager.operations.addNewOperation(currentDragOperation);
                    generalController.publishInfo(ControlManager.operations.redo());
                }
            }
            if (currentlyMovedElement instanceof Edge) {
                Edge edge = (Edge) currentlyMovedElement;
                if (changingEdgeAngle) {
                    currentDragOperation.setEdgeEndState(edge);
                    if (currentDragOperation.changeMade()) {
                        ControlManager.operations.addNewOperation(currentDragOperation);
                        generalController.publishInfo(ControlManager.operations.redo());
                    }
                    changingEdgeAngle = false;
                } else {
                    if (currentDragOperation.draggingA()) {
                        if (edge.getVertexA() != currentDragOperation.getDisjointedVertex()) {
                            if (edge.getVertexA() != edgeDragDummy) {
                                currentDragOperation.setEdgeEndState(edge);
                                ControlManager.operations.addNewOperation(currentDragOperation);
                                generalController.publishInfo(ControlManager.operations.redo());
                            } else {
                                currentDragOperation.restoreEdgeStartState();
                            }
                        }
                    } else {
                        if (edge.getVertexB() != currentDragOperation.getDisjointedVertex()) {
                            if (edge.getVertexB() != edgeDragDummy) {
                                currentDragOperation.setEdgeEndState(edge);
                                ControlManager.operations.addNewOperation(currentDragOperation);
                                generalController.publishInfo(ControlManager.operations.redo());
                            } else {
                                currentDragOperation.restoreEdgeStartState();
                            }
                        }
                    }
                }
            } else if (currentlyMovedElement instanceof LabelV) {
                currentDragOperation.setEndAngle(((LabelV) currentlyMovedElement).getPosition());
                if (currentDragOperation.changeMade()) {
                    ControlManager.operations.addNewOperation(currentDragOperation);
                    generalController.publishInfo(ControlManager.operations.redo());
                }
            } else if (currentlyMovedElement instanceof LabelE) {
                currentDragOperation.setLabelEEndState((LabelE) currentlyMovedElement);
                if (currentDragOperation.changeMade()) {
                    ControlManager.operations.addNewOperation(currentDragOperation);
                    generalController.publishInfo(ControlManager.operations.redo());
                }
            }

            //ControlManager.updatePropertyChangeOperationStatus(true);
        }
    }


    @Override
    public void processMouseClicking(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (tool != ToolType.SELECT) {
            //generalController.getSelectionController().clearSelection();
            //ControlManager.updatePropertyChangeOperationStatus(false);
        }

        boolean consumed = false;
        if (currentCopyPasteOperation != null) {
            if (currentCopyPasteOperation.pasting) {
                consumed = true;
                if (currentCopyPasteOperation.fitsIntoPosition()) {
                    ControlManager.operations.addNewOperation(currentCopyPasteOperation);
                    generalController.publishInfo(ControlManager.operations.redo());
                    generalController.getSelectionController().addToSelection(currentCopyPasteOperation.vertices, false);
                    //ControlManager.updatePropertyChangeOperationStatus(true);
                    currentCopyPasteOperation = currentCopyPasteOperation.getCopy();
                    //generalController.updateMenuBarAndActions();
                } else {
                    generalController.publishInfo(StringLiterals.INFO_CANNOT_PASTE_SUBGRAPH);
                }
            }
        }

        if (!consumed) {
            if (mode == ModeType.VERTEX) {
                if (tool == ToolType.ADD) {
                    Vertex vertex = new Vertex(generalController.getGraph());
                    vertex.setModel(generalController.getGraph().getVertexDefaultModel());
                    vertex.setPosX(x);
                    vertex.setPosY(y);
                    if (generalController.getGraph().gridOn) {
                        VertexUtils.adjustToGrid(vertex);
                    }

                    if (VertexUtils.fitsIntoPage(vertex)) {
                        if (!GraphUtils.checkVertexCollision(generalController.getGraph(), vertex)) {
                            VertexUtils.updateNumber(vertex, generalController.getGraph().getGraphNumeration().getNextFreeNumber());
                            new CreationRemovalOperation(generalController, vertex, OperationType.ADD_VERTEX, StringLiterals.INFO_VERTEX_ADD, true);
                            // TODO Tutaj proba zastapienia dodawania tym nowym
                            //ControlManager.operations.addNewOperation(new AddOperation(generalController, vertex));
                            //generalController.publishInfo(ControlManager.operations.redo());
                            //generalController.getSelectionController().addToSelection(vertex, false);
                        } else {
                            generalController.publishInfo(StringLiterals.INFO_CANNOT_CREATE_VERTEX_COLLISION);
                        }
                    } else {
                        generalController.publishInfo(StringLiterals.INFO_CANNOT_CREATE_VERTEX_BOUNDARY);
                    }
                } else if (tool == ToolType.REMOVE) {
                    Vertex temp = GraphUtils.getVertexFromPosition(generalController.getGraph(), x, y);
                    if (temp != null) {
                        ControlManager.operations.addNewOperation(new RemoveOperation(generalController, temp));
                        generalController.publishInfo(ControlManager.operations.redo());
                    } else {
                        generalController.publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (tool == ToolType.SELECT) {
                    if (!mousePressed) {
                        generalController.getSelectionController().addToSelection(GraphUtils.getVertexFromPosition(generalController.getGraph(), x, y), e.isControlDown());
                    }
                }
            } else if (mode == ModeType.EDGE) {
                if (tool == ToolType.ADD) {
                    Vertex temp = GraphUtils.getVertexFromPosition(generalController.getGraph(), x, y);
                    if (temp == null) {
                        if (currentlyAddedEdge == null) {
                            generalController.publishInfo(StringLiterals.INFO_CHOOSE_EDGE_START);
                        } else {
                            currentlyAddedEdge = null;
                            generalController.publishInfo(StringLiterals.INFO_EDGE_ADDING_CANCELLED);
                        }
                    } else {
                        if (currentlyAddedEdge == null) {
                            currentlyAddedEdge = new Edge(generalController.getGraph());
                            currentlyAddedEdge.setModel(generalController.getGraph().getEdgeDefaultModel());
                            currentlyAddedEdge.setVertexA(temp);
                            generalController.publishInfo(StringLiterals.INFO_CHOOSE_EDGE_END);
                        } else {
                            currentlyAddedEdge.setVertexB(temp);
                            currentlyAddedEdge.setDirected(e.isShiftDown());
                            new CreationRemovalOperation(generalController, currentlyAddedEdge, OperationType.ADD_EDGE, StringLiterals.INFO_EDGE_ADD, true);
                            currentlyAddedEdge = null;
                            // TODO Tutaj proba zastapienia dodawania tym nowym
                            //ControlManager.operations.addNewOperation(new AddOperation(generalController, currentlyAddedEdge));
                            //generalController.publishInfo(ControlManager.operations.redo());
                            //generalController.getSelectionController().addToSelection(currentlyAddedEdge, false);
                        }
                    }
                } else if (tool == ToolType.REMOVE) {
                    Edge temp = GraphUtils.getEdgeFromPosition(generalController.getGraph(), x, y);
                    if (temp != null) {
                        ControlManager.operations.addNewOperation(new RemoveOperation(generalController, temp));
                        generalController.publishInfo(ControlManager.operations.redo());
                    } else {
                        generalController.publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (tool == ToolType.SELECT) {
                    if (!mousePressed) {
                        generalController.getSelectionController().addToSelection(GraphUtils.getEdgeFromPosition(generalController.getGraph(), x, y), e.isControlDown());
                    }
                }
            } else if (mode == ModeType.LABEL_VERTEX) {
                if (tool == ToolType.ADD) {
                    Vertex temp = GraphUtils.getVertexFromPosition(generalController.getGraph(), x, y);
                    if (temp != null) {
                        if (temp.getLabel() == null) {
                            LabelV labelV = new LabelV(temp, generalController.getGraph());
                            labelV.setModel(generalController.getGraph().getLabelVDefaultModel());
                            LabelVUtils.updatePosition(labelV);
                            // TODO Tutaj proba zastapienia dodawania tym nowym
                            new CreationRemovalOperation(generalController, labelV, OperationType.ADD_LABEL_VERTEX, StringLiterals.INFO_LABEL_V_ADD, true);
                            //ControlManager.operations.addNewOperation(new AddOperation(generalController, labelV));
                            //ControlManager.operations.redo();
                            //generalController.getSelectionController().addToSelection(labelV, false);

                        } else {
                            generalController.publishInfo(StringLiterals.INFO_CANNOT_CREATE_LABEL_V_EXISTS);
                        }
                    } else {
                        generalController.publishInfo(StringLiterals.INFO_CHOOSE_VERTEX_FOR_LABEL);
                    }
                } else if (tool == ToolType.REMOVE) {
                    LabelV temp = GraphUtils.getLabelVFromPosition(generalController.getGraph(), x, y);
                    if (temp != null) {
                        ControlManager.operations.addNewOperation(new RemoveOperation(generalController, temp));
                        generalController.publishInfo(ControlManager.operations.redo());
                    } else {
                        generalController.publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (tool == ToolType.SELECT) {
                    if (!mousePressed) {
                        generalController.getSelectionController().addToSelection(GraphUtils.getLabelVFromPosition(generalController.getGraph(), x, y), e.isControlDown());
                    }
                }
            } else {
                if (tool == ToolType.ADD) {
                    Edge temp = GraphUtils.getEdgeFromPosition(generalController.getGraph(), x, y);
                    if (temp != null) {
                        if (temp.getLabel() == null) {
                            LabelE labelE = new LabelE(temp, generalController.getGraph());
                            labelE.setModel(generalController.getGraph().getLabelEDefaultModel());
                            labelE.setHorizontalPlacement(e.isShiftDown());
                            // TODO Tutaj proba zastapienia dodawania tym nowym
                            new CreationRemovalOperation(generalController, labelE, OperationType.ADD_LABEL_EDGE, StringLiterals.INFO_LABEL_E_ADD, true);
                            //ControlManager.operations.addNewOperation(new AddOperation(generalController, labelE));
                            //ControlManager.operations.redo();
                            //generalController.getSelectionController().addToSelection(labelE, false);
                        } else {
                            generalController.publishInfo(StringLiterals.INFO_CANNOT_CREATE_LABEL_E_EXISTS);
                        }
                    } else {
                        generalController.publishInfo(StringLiterals.INFO_CHOOSE_EDGE_FOR_LABEL);
                    }
                } else if (tool == ToolType.REMOVE) {
                    LabelE temp = GraphUtils.getLabelEFromPosition(generalController.getGraph(), x, y);
                    if (temp != null) {
                        ControlManager.operations.addNewOperation(new RemoveOperation(generalController, temp));
                        generalController.publishInfo(ControlManager.operations.redo());
                    } else {
                        generalController.publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (tool == ToolType.SELECT) {
                    if (!mousePressed) {
                        generalController.getSelectionController().addToSelection(GraphUtils.getLabelEFromPosition(generalController.getGraph(), x, y), e.isControlDown());
                    }
                }
            }
            //ControlManager.updatePropertyChangeOperationStatus(true);
        }

        operationController.reportGenericOperation(null);
    }

    @Override
    public void processMousePressing(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        mousePressed = true;
        mousePressX = x;
        mousePressY = y;

        if (tool != ToolType.REMOVE) {
            if (mode == ModeType.VERTEX) {
                if (GraphUtils.getVertexFromPosition(generalController.getGraph(), x, y) != null) {
                    if (generalController.getSelectionController().selectionContains(GraphUtils.getVertexFromPosition(generalController.getGraph(), x, y))) {
                        currentlyMovedElement = GraphUtils.getVertexFromPosition(generalController.getGraph(), x, y);
                        currentDragOperation = new DragOperation(currentlyMovedElement);
                        currentDragOperation.setStartPos(((Vertex) currentlyMovedElement).getPosX(), ((Vertex) currentlyMovedElement).getPosY());
                    }
                }
            } else if (mode == ModeType.EDGE) {
                if (GraphUtils.getEdgeFromPosition(generalController.getGraph(), x, y) != null) {
                    if (generalController.getSelectionController().selectionContains(GraphUtils.getEdgeFromPosition(generalController.getGraph(), x, y))) {
                        Edge edge = GraphUtils.getEdgeFromPosition(generalController.getGraph(), x, y);
                        currentlyMovedElement = edge;
                        currentDragOperation = new DragOperation(currentlyMovedElement);
                        if (edge.getVertexA() == edge.getVertexB()) {
                            Point c = new Point(x, y);
                            Point v = new Point(edge.getVertexA().getPosX(), edge.getVertexA().getPosY());
                            if (c.distance(v) > (edge.getVertexA().getRadius() + edge.getVertexA().getLineWidth() / 2) * 1.5) {
                                changingEdgeAngle = true;
                                currentDragOperation.setEdgeStartState(edge, true);
                            } else {
                                edgeDragDummy = new Vertex(generalController.getGraph());
                                edgeDragDummy.setPosX(x);
                                edgeDragDummy.setPosY(y);
                                edgeDragDummy.setRadius(2);
                                if (c.distance(edge.getInPoint()) < c.distance(edge.getOutPoint())) {
                                    currentDragOperation.setEdgeStartState(edge, false);
                                    edge.setVertexB(edgeDragDummy);
                                } else {
                                    currentDragOperation.setEdgeStartState(edge, true);
                                    edge.setVertexA(edgeDragDummy);
                                }

                            }
                        } else {
                            Point c = new Point(x, y);
                            Point va = new Point(edge.getVertexA().getPosX(), edge.getVertexA().getPosY());
                            Point vb = new Point(edge.getVertexB().getPosX(), edge.getVertexB().getPosY());
                            if (c.distance(va) / c.distance(vb) < 2 && c.distance(va) / c.distance(vb) > 0.5) {
                                changingEdgeAngle = true;
                                currentDragOperation.setEdgeStartState(edge, true);
                            } else {
                                edgeDragDummy = new Vertex(generalController.getGraph());
                                edgeDragDummy.setPosX(x);
                                edgeDragDummy.setPosY(y);
                                edgeDragDummy.setRadius(2);
                                if (c.distance(va) < c.distance(vb)) {
                                    currentDragOperation.setEdgeStartState(edge, true);
                                    edge.setVertexA(edgeDragDummy);
                                } else {
                                    currentDragOperation.setEdgeStartState(edge, false);
                                    edge.setVertexB(edgeDragDummy);
                                }
                            }
                        }
                    }
                }
            } else if (mode == ModeType.LABEL_VERTEX) {
                if (GraphUtils.getLabelVFromPosition(generalController.getGraph(), x, y) != null) {
                    if (generalController.getSelectionController().selectionContains(GraphUtils.getLabelVFromPosition(generalController.getGraph(), x, y))) {
                        currentlyMovedElement = GraphUtils.getLabelVFromPosition(generalController.getGraph(), x, y);
                        currentDragOperation = new DragOperation(currentlyMovedElement);
                        currentDragOperation.setStartAngle(((LabelV) currentlyMovedElement).getPosition());
                    }
                }
            } else if (mode == ModeType.LABEL_EDGE) {
                if (GraphUtils.getLabelEFromPosition(generalController.getGraph(), x, y) != null) {
                    if (generalController.getSelectionController().selectionContains(GraphUtils.getLabelEFromPosition(generalController.getGraph(), x, y))) {
                        currentlyMovedElement = GraphUtils.getLabelEFromPosition(generalController.getGraph(), x, y);
                        currentDragOperation = new DragOperation(currentlyMovedElement);
                        currentDragOperation.setLabelEStartState((LabelE) currentlyMovedElement);
                    }
                }
            }
        }

        operationController.reportGenericOperation(null);
    }

    @Override
    public void processMouseReleasing(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();
        mousePressed = false;
        finishMovingElement();

        if (tool != ToolType.ADD && currentlyMovedElement == null) {
            int x1 = Math.min(mousePressX, x);
            int width = Math.abs(x - mousePressX);
            int y1 = Math.min(mousePressY, y);
            int height = Math.abs(y - mousePressY);

            if (width + height > 2) {
                if (tool == ToolType.REMOVE) {
                    generalController.getSelectionController().clearSelection();
                    generalController.getSelectionController().addToSelection(GraphUtils.getIntersectingElements(generalController.getGraph(), mode, new Rectangle(x1, y1, width, height)), false);
                    generalController.deleteSelection();
                } else if (tool == ToolType.SELECT) {
                    generalController.getSelectionController().addToSelection(GraphUtils.getIntersectingElements(generalController.getGraph(), mode, new Rectangle(x1, y1, width, height)), e.isControlDown());
                }
            }
        }

        currentlyMovedElement = null;
        currentDragOperation = null;
        operationController.reportGenericOperation(null);
    }

    @Override
    public void processMouseMoving(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        if (tool == ToolType.ADD) {

            if (currentlyAddedEdge != null) {
                currentlyAddedEdge.setDirected(e.isShiftDown());
            }

            if (currentlyMovedElement != null) {
                if (currentlyMovedElement instanceof Edge) {
                    ((Edge) currentlyMovedElement).setDirected(e.isShiftDown());
                }
            }

        }

        operationController.reportGenericOperation(null);
    }

    @Override
    public void processMouseDragging(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        mouseX = x;
        mouseY = y;

        if (currentlyMovedElement != null) {
            if (mode == ModeType.VERTEX) {
                Vertex vertex = (Vertex) currentlyMovedElement;
                generalController.getGraph().getVertices().remove(vertex);
                int oldPosX = vertex.getPosX();
                int oldPosY = vertex.getPosY();

                vertex.setPosX(x);
                vertex.setPosY(y);

                if (generalController.getGraph().gridOn) {
                    VertexUtils.adjustToGrid(vertex);
                }

                if (GraphUtils.checkVertexCollision(generalController.getGraph(), vertex) || !VertexUtils.fitsIntoPage(vertex)) {
                    vertex.setPosX(oldPosX);
                    vertex.setPosY(oldPosY);
                }
                generalController.getGraph().getVertices().add(vertex);
            } else if (mode == ModeType.EDGE) {
                if (changingEdgeAngle) {
                    Edge edge = (Edge) currentlyMovedElement;
                    if (edge.getVertexA() == edge.getVertexB()) {
                        double angle = (Math.toDegrees(Math.atan2(x - edge.getVertexB().getPosX(), y - edge.getVertexB().getPosY())) + 270) % 360;
                        edge.setRelativeEdgeAngle(((int) Math.floor((angle + 45) / 90) % 4) * 90);
                    } else {
                        double angle;
                        if (Point.distance(x, y, edge.getVertexA().getPosX(), edge.getVertexA().getPosY()) < Point.distance(x, y, edge.getVertexB().getPosX(), edge.getVertexB().getPosY())) {
                            angle = Math.toDegrees(Math.atan2(x - edge.getVertexA().getPosX(), y - edge.getVertexA().getPosY())) + 270;
                            angle -= Math.toDegrees(Math.atan2(edge.getVertexB().getPosX() - edge.getVertexA().getPosX(), edge.getVertexB().getPosY() - edge.getVertexA().getPosY())) + 270;
                        } else {
                            angle = Math.toDegrees(Math.atan2(x - edge.getVertexB().getPosX(), y - edge.getVertexB().getPosY())) + 270;
                            angle = -angle + Math.toDegrees(Math.atan2(edge.getVertexA().getPosX() - edge.getVertexB().getPosX(), edge.getVertexA().getPosY() - edge.getVertexB().getPosY()))
                                    + 270;
                        }

                        angle = (angle + 362.5) % 360;
                        int intAngle = ((((int) Math.floor(angle) / 5) * 5) + 720) % 360;
                        if (intAngle > 60) {
                            if (intAngle < 180) {
                                intAngle = 60;
                            } else if (intAngle < 300) {
                                intAngle = 300;
                            }

                        }
                        edge.setRelativeEdgeAngle(intAngle);
                    }
                } else {
                    Vertex vertex;
                    if ((vertex = GraphUtils.getVertexFromPosition(generalController.getGraph(), x, y)) != null) {
                        Edge edge = (Edge) currentlyMovedElement;
                        if (currentDragOperation.draggingA()) {
                            edge.setVertexA(vertex);
                        } else {
                            edge.setVertexB(vertex);
                        }

                        if (edge.getVertexA() == edge.getVertexB()) {
                            double angle = (Math.toDegrees(Math.atan2(x - edge.getVertexB().getPosX(), y - edge.getVertexB().getPosY())) + 270) % 360;
                            edge.setRelativeEdgeAngle(((int) Math.floor((angle + 45) / 90) % 4) * 90);
                        } else {
                            double angle;
                            if (Point.distance(x, y, edge.getVertexA().getPosX(), edge.getVertexA().getPosY()) < Point.distance(x, y, edge.getVertexB().getPosX(),
                                    edge.getVertexB().getPosY())) {
                                angle = Math.toDegrees(Math.atan2(x - edge.getVertexA().getPosX(), y - edge.getVertexA().getPosY())) + 270;
                                angle -= Math.toDegrees(Math.atan2(edge.getVertexB().getPosX() - edge.getVertexA().getPosX(), edge.getVertexB().getPosY() - edge.getVertexA().getPosY())) + 270;
                            } else {
                                angle = Math.toDegrees(Math.atan2(x - edge.getVertexB().getPosX(), y - edge.getVertexB().getPosY())) + 270;
                                angle = -angle
                                        + Math.toDegrees(Math.atan2(edge.getVertexA().getPosX() - edge.getVertexB().getPosX(), edge.getVertexA().getPosY() - edge.getVertexB().getPosY()))
                                        + 270;
                            }

                            angle = (angle + 362.5) % 360;
                            int intAngle = ((((int) Math.floor(angle) / 5) * 5) + 720) % 360;
                            if (intAngle > 60) {
                                if (intAngle < 180) {
                                    intAngle = 60;
                                } else if (intAngle < 300) {
                                    intAngle = 300;
                                }

                            }
                            edge.setRelativeEdgeAngle(intAngle);
                        }
                    } else {
                        edgeDragDummy.setPosX(x);
                        edgeDragDummy.setPosY(y);
                        ((Edge) currentlyMovedElement).setRelativeEdgeAngle(0);
                        if (currentDragOperation.draggingA()) {
                            ((Edge) currentlyMovedElement).setVertexA(edgeDragDummy);
                        } else {
                            ((Edge) currentlyMovedElement).setVertexB(edgeDragDummy);
                        }
                    }
                }
            } else if (mode == ModeType.LABEL_VERTEX) {
                Vertex vertex = ((LabelV) currentlyMovedElement).getOwner();
                Point2D p1 = new Point(vertex.getPosX(), vertex.getPosY());
                Point2D p2 = new Point(x, y);
                double angle = Math.toDegrees(Math.asin((x - vertex.getPosX()) / p1.distance(p2)));
                if (y < vertex.getPosY()) {
                    if (x < vertex.getPosX()) {
                        angle += 360;
                    }
                } else {
                    angle = 180 - angle;
                }
                ((LabelV) currentlyMovedElement).setPosition(((int) Math.abs(Math.ceil((angle - 22.5) / 45))) % 8);
            } else if (mode == ModeType.LABEL_EDGE) {
                int bias;
                Edge edge = ((LabelE) currentlyMovedElement).getOwner();
                LabelE labelE = (LabelE) currentlyMovedElement;
                if (edge.getVertexA() == edge.getVertexB()) {
                    double angle = -Math.toDegrees(Math.atan2(x - edge.getArcMiddle().x, y - edge.getArcMiddle().y)) + 270 + edge.getRelativeEdgeAngle();
                    labelE.setPosition((int) Math.round((angle % 360) / 3.6));
                } else {
                    if (edge.getRelativeEdgeAngle() == 0) {
                        Point p1 = edge.getInPoint();
                        Point p2 = edge.getOutPoint();

                        if (p2.x < p1.x) {
                            Point temp = p2;
                            p2 = p1;
                            p1 = temp;
                        }

                        Point c = new Point(x, y);
                        double p1p2 = p1.distance(p2);
                        double p1c = p1.distance(c);
                        double p2c = p2.distance(c);
                        double mc = Line2D.ptLineDist((double) p1.x, (double) p1.y, (double) p2.x, (double) p2.y, (double) x, (double) y);

                        labelE.setTopPlacement(((p2.x - p1.x) * (c.y - p1.y) - (p2.y - p1.y) * (c.x - p1.x) < 0));

                        if (p1c * p1c > p2c * p2c + p1p2 * p1p2) {
                            bias = 100;
                        } else if (p2c * p2c > p1c * p1c + p1p2 * p1p2) {
                            bias = 0;
                        } else {
                            bias = (int) Math.round(100 * Math.sqrt(p1c * p1c - mc * mc) / p1p2);
                        }
                        labelE.setPosition(bias);
                    } else {
                        double startAngle = (Math.toDegrees(Math.atan2(edge.getOutPoint().x - edge.getArcMiddle().x, edge.getOutPoint().y - edge.getArcMiddle().y)) + 270) % 360;
                        double endAngle = (Math.toDegrees(Math.atan2(edge.getInPoint().x - edge.getArcMiddle().x, edge.getInPoint().y - edge.getArcMiddle().y)) + 270) % 360;
                        double mouseAngle = (Math.toDegrees(Math.atan2(x - edge.getArcMiddle().x, y - edge.getArcMiddle().y)) + 270) % 360;

                        int position;
                        double alpha = (startAngle - mouseAngle + 360) % 360;
                        if (alpha > 180) {
                            alpha -= 360;
                        }
                        double beta = (startAngle - endAngle + 360) % 360;
                        if (beta > 180) {
                            beta -= 360;
                        }

                        position = (int) Math.round(100 * (alpha / beta));

                        if (position > -1 && position < 101) {
                            labelE.setTopPlacement((edge.getArcMiddle().distance(new Point(x, y)) > edge.getArcRadius()));
                            labelE.setPosition(position);
                        }
                    }
                }
            }
        }

        operationController.reportGenericOperation(null);
    }

    @Override
    public void paintCurrentlyAddedElement(Graphics2D g) {
        if (tool == ToolType.ADD) {
            if (mode == ModeType.VERTEX) {
                Vertex vertex = new Vertex(generalController.getGraph());
                vertex.setModel(generalController.getGraph().getVertexDefaultModel());
                VertexUtils.updateNumber(vertex, generalController.getGraph().getGraphNumeration().getNextFreeNumber());
                vertex.setPosX(mouseX);
                vertex.setPosY(mouseY);
                if (!GraphUtils.checkVertexCollision(generalController.getGraph(), vertex) && VertexUtils.fitsIntoPage(vertex)) {
                    vertex.draw(g, true);
                }
            } else if (mode == ModeType.EDGE) {
                if (currentlyAddedEdge != null) {
                    Vertex vertex = GraphUtils.getVertexFromPosition(generalController.getGraph(), mouseX, mouseY);
                    if (vertex == null) {
                        vertex = new Vertex(generalController.getGraph());
                        vertex.setPosX(mouseX);
                        vertex.setPosY(mouseY);
                        vertex.setRadius(2);
                        currentlyAddedEdge.setRelativeEdgeAngle(0);
                    }
                    currentlyAddedEdge.setVertexB(vertex);
                    if (GraphUtils.getVertexFromPosition(generalController.getGraph(), mouseX, mouseY) != null) {
                        if (currentlyAddedEdge.getVertexA() != currentlyAddedEdge.getVertexB()) {
                            double angle = Math.toDegrees(Math.atan2(mouseX - currentlyAddedEdge.getVertexB().getPosX(), mouseY
                                    - currentlyAddedEdge.getVertexB().getPosY())) + 270;
                            angle = -angle
                                    + Math.toDegrees(Math.atan2(currentlyAddedEdge.getVertexA().getPosX() - currentlyAddedEdge.getVertexB().getPosX(),
                                    currentlyAddedEdge.getVertexA().getPosY() - currentlyAddedEdge.getVertexB().getPosY())) + 270;
                            angle = (angle + 362.5) % 360;
                            int intAngle = ((((int) Math.floor(angle) / 5) * 5) + 720) % 360;
                            if (intAngle > 60) {
                                if (intAngle < 180) {
                                    intAngle = 60;
                                } else if (intAngle < 300) {
                                    intAngle = 300;
                                }

                            }
                            currentlyAddedEdge.setRelativeEdgeAngle(intAngle);
                        } else {
                            double angle = (Math.toDegrees(Math.atan2(mouseX - currentlyAddedEdge.getVertexB().getPosX(), mouseY
                                    - currentlyAddedEdge.getVertexB().getPosY())) + 270) % 360;
                            currentlyAddedEdge.setRelativeEdgeAngle(((int) Math.floor((angle + 45) / 90) % 4) * 90);
                        }
                    }
                    currentlyAddedEdge.draw(g, true);
                }
            } else if (mode == ModeType.LABEL_VERTEX) {
                Vertex temp = GraphUtils.getVertexFromPosition(generalController.getGraph(), mouseX, mouseY);
                if (temp != null) {
                    if (temp.getLabel() == null) {
                        LabelV labelV = new LabelV(temp, generalController.getGraph());
                        labelV.setModel(generalController.getGraph().getLabelVDefaultModel());

                        Point2D p1 = new Point(temp.getPosX(), temp.getPosY());
                        Point2D p2 = new Point(mouseX, mouseY);
                        double angle = Math.toDegrees(Math.asin((mouseX - temp.getPosX()) / p1.distance(p2)));
                        if (mouseY < temp.getPosY()) {
                            if (mouseX < temp.getPosX()) {
                                angle += 360;
                            }
                        } else {
                            angle = 180 - angle;
                        }
                        labelV.setPosition(((int) Math.abs(Math.ceil((angle - 22.5) / 45))) % 8);

                        generalController.getGraph().getLabelVDefaultModel().position = labelV.getPosition();
                        labelV.draw(g, true);
                    }
                }
            } else {
                Edge temp = GraphUtils.getEdgeFromPosition(generalController.getGraph(), mouseX, mouseY);
                if (temp != null) {
                    if (temp.getLabel() == null) {
                        LabelE labelE = new LabelE(temp, generalController.getGraph());
                        labelE.setModel(generalController.getGraph().getLabelEDefaultModel());
                        labelE.setHorizontalPlacement(shiftDown);

                        int bias;
                        int x = mouseX;
                        int y = mouseY;

                        if (temp.getVertexA() == temp.getVertexB()) {
                            double angle = -Math.toDegrees(Math.atan2(x - temp.getArcMiddle().x, y - temp.getArcMiddle().y)) + 270 + temp.getRelativeEdgeAngle();
                            labelE.setPosition((int) Math.round((angle % 360) / 3.6));
                        } else {
                            if (temp.getRelativeEdgeAngle() == 0) {
                                Point p1 = temp.getInPoint();
                                Point p2 = temp.getOutPoint();

                                if (p2.x < p1.x) {
                                    Point tempP = p2;
                                    p2 = p1;
                                    p1 = tempP;
                                }

                                Point c = new Point(x, y);
                                double p1p2 = p1.distance(p2);
                                double p1c = p1.distance(c);
                                double p2c = p2.distance(c);
                                double mc = Line2D.ptLineDist((double) p1.x, (double) p1.y, (double) p2.x, (double) p2.y, (double) x, (double) y);

                                labelE.setTopPlacement(((p2.x - p1.x) * (c.y - p1.y) - (p2.y - p1.y) * (c.x - p1.x) < 0));

                                if (p1c * p1c > p2c * p2c + p1p2 * p1p2) {
                                    bias = 100;
                                } else if (p2c * p2c > p1c * p1c + p1p2 * p1p2) {
                                    bias = 0;
                                } else {
                                    bias = (int) Math.round(100 * Math.sqrt(p1c * p1c - mc * mc) / p1p2);
                                }
                                labelE.setPosition(bias);
                            } else {
                                double startAngle = (Math.toDegrees(Math
                                        .atan2(temp.getOutPoint().x - temp.getArcMiddle().x, temp.getOutPoint().y - temp.getArcMiddle().y)) + 270) % 360;
                                double endAngle = (Math.toDegrees(Math.atan2(temp.getInPoint().x - temp.getArcMiddle().x, temp.getInPoint().y - temp.getArcMiddle().y)) + 270) % 360;
                                double mouseAngle = (Math.toDegrees(Math.atan2(x - temp.getArcMiddle().x, y - temp.getArcMiddle().y)) + 270) % 360;

                                int position;
                                double alpha = (startAngle - mouseAngle + 360) % 360;
                                if (alpha > 180) {
                                    alpha -= 360;
                                }
                                double beta = (startAngle - endAngle + 360) % 360;
                                if (beta > 180) {
                                    beta -= 360;
                                }

                                position = (int) Math.round(100 * (alpha / beta));

                                if (position > -1 && position < 101) {
                                    labelE.setTopPlacement((temp.getArcMiddle().distance(new Point(x, y)) > temp.getArcRadius()));
                                    labelE.setPosition(position);
                                }
                            }
                        }

                        generalController.getGraph().getLabelEDefaultModel().topPlacement = 0;
                        if (labelE.isTopPlacement()) {
                            generalController.getGraph().getLabelEDefaultModel().topPlacement = 1;
                        }
                        generalController.getGraph().getLabelEDefaultModel().position = labelE.getPosition();
                        labelE.draw(g, true);
                    }
                }
            }
        }
    }
}
