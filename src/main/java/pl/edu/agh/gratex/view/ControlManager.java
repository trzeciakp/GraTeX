package pl.edu.agh.gratex.view;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.controller.SelectionControllerTmpImpl;
import pl.edu.agh.gratex.editor.*;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.LinkedList;
import java.util.List;


public class ControlManager {
    public static MainWindow mainWindow;

    public static ToolType tool = ToolType.ADD;

    public static Edge currentlyAddedEdge = null;
    public static int selectionID = 0;
    public static OperationList operations = null;
    public static GraphElement currentlyMovedElement = null;
    public static DragOperation currentDragOperation = null;
    public static boolean changingEdgeAngle;
    public static PropertyChangeOperation currentPropertyChangeOperation = null;
    public static CopyPasteOperation currentCopyPasteOperation = null;
    public static Vertex edgeDragDummy = null;
    //public static boolean changeMade = false;

    public static boolean shiftDown;
    public static boolean mousePressed;
    public static int mousePressX;
    public static int mousePressY;
    public static int mouseX;
    public static int mouseY;

    public static void passWindowHandle(MainWindow _mainWindow) {
        mainWindow = _mainWindow;
    }

    public static ToolType getTool() {
        return tool;
    }
    public static void applyChange() {
        mainWindow.getSelectionController().clearSelection();
        //mainWindow.panel_propertyEditor.setMode(ControlManager.getMode().ordinal() + 1);
        updatePropertyChangeOperationStatus(false);
        finishMovingElement();
        currentlyMovedElement = null;
        currentDragOperation = null;
        currentlyAddedEdge = null;
        mainWindow.updateFunctions();
    }

    public static void changeTool(ToolType _tool) {
        tool = _tool;
        applyChange();
    }

    public static void selectAll() {
        mainWindow.getSelectionController().selectAll();
    }

    public static void addToSelection(List<GraphElement> elements, boolean controlDown) {
        mainWindow.getSelectionController().addToSelection(elements, controlDown);
        updatePropertyChangeOperationStatus(true);
    }

    public static void addToSelection(GraphElement element, boolean controlDown) {
        mainWindow.getSelectionController().addToSelection(element, controlDown);
        updatePropertyChangeOperationStatus(true);
    }

    public static void deleteSelection() {
        if (mainWindow.getSelectionController().getSize() > 0) {
            operations.addNewOperation(new RemoveOperation(mainWindow.getSelectionController().getSelection()));
            mainWindow.getGeneralController().publishInfo(operations.redo());
            mainWindow.updateFunctions();
        }
        mainWindow.getSelectionController().clearSelection();
        updatePropertyChangeOperationStatus(false);
    }

    public static void updatePropertyChangeOperationStatus(boolean newSelection) {
        mainWindow.menuBar.updateFunctions();
        mainWindow.panel_buttonContainer.updateFunctions();

        if (mainWindow.getSelectionController().getSize() > 0) {
            if (newSelection) {
                selectionID++;
                mainWindow.panel_propertyEditor.setEnabled(true);
            }
            currentPropertyChangeOperation = new PropertyChangeOperation(mainWindow.getSelectionController().getSelection(), selectionID);
            mainWindow.panel_propertyEditor.setModel(currentPropertyChangeOperation.initialModel);
        } else {
            currentPropertyChangeOperation = null;
            mainWindow.panel_propertyEditor.setEnabled(false);
            if (mainWindow.getGeneralController().getMode() == ModeType.VERTEX) {
                mainWindow.panel_propertyEditor.setModel(new VertexPropertyModel());
            } else if (mainWindow.getGeneralController().getMode() == ModeType.EDGE) {
                mainWindow.panel_propertyEditor.setModel(new EdgePropertyModel());
            } else if (mainWindow.getGeneralController().getMode() == ModeType.LABEL_VERTEX) {
                mainWindow.panel_propertyEditor.setModel(new LabelVertexPropertyModel());
            } else if (mainWindow.getGeneralController().getMode() == ModeType.LABEL_EDGE) {
                mainWindow.panel_propertyEditor.setModel(new LabelEdgePropertyModel());
            }
        }
    }

    public static void updateSelectedItemsModel(PropertyModel pm) {
        if (currentPropertyChangeOperation != null) {
            if (!operations.mergePropertyChangeOperations(pm, selectionID)) {
                currentPropertyChangeOperation.setEndModel(pm);
                operations.addNewOperation(currentPropertyChangeOperation);
                mainWindow.getGeneralController().publishInfo(operations.redo());
            }
            mainWindow.updateWorkspace();
            updatePropertyChangeOperationStatus(false);
        }
    }

    public static void undo() {
        mainWindow.getGeneralController().publishInfo(operations.undo());
        mainWindow.updateWorkspace();
        mainWindow.getSelectionController().clearSelection();
        updatePropertyChangeOperationStatus(false);
    }

    public static void redo() {
        mainWindow.getGeneralController().publishInfo(operations.redo());
        mainWindow.updateWorkspace();
        mainWindow.getSelectionController().clearSelection();
    }

    public static void toggleGrid() {
        if (mainWindow.getGeneralController().getGraph().gridOn) {
            mainWindow.getGeneralController().getGraph().gridOn = false;
        } else {
            GridDialog gd = new GridDialog(mainWindow, mainWindow.getGeneralController().getGraph().gridResolutionX, mainWindow.getGeneralController().getGraph().gridResolutionY);
            int[] result = gd.showDialog();
            if (result != null) {
                mainWindow.getGeneralController().getGraph().gridOn = true;
                mainWindow.getGeneralController().getGraph().gridResolutionX = result[0];
                mainWindow.getGeneralController().getGraph().gridResolutionY = result[1];
                GraphUtils.adjustVerticesToGrid(mainWindow.getGeneralController().getGraph());
            }
        }
        mainWindow.updateFunctions();
    }

    public static void setNumeration() {
        mainWindow.getSelectionController().clearSelection();
        updatePropertyChangeOperationStatus(false);
        NumerationDialog nd = new NumerationDialog(mainWindow, mainWindow.getGeneralController().getGraph().getGraphNumeration().isNumerationDigital(),
                mainWindow.getGeneralController().getGraph().getGraphNumeration().getStartingNumber(), Const.MAX_VERTEX_NUMBER);
        int[] result = nd.showDialog();

        if (result != null) {
            mainWindow.getGeneralController().getGraph().getGraphNumeration().setNumerationDigital(result[0] == 0);
            mainWindow.getGeneralController().getGraph().getGraphNumeration().setStartingNumber(result[1]);
        }

        mainWindow.updateFunctions();
    }

    public static void parseToTeX() {
        new LatexCodeDialog(mainWindow, Parser.parse(mainWindow.getGeneralController().getGraph()));
    }

    public static void copyToClipboard() {
        currentCopyPasteOperation = new CopyPasteOperation(mainWindow.getSelectionController().getSelection());
        mainWindow.menuBar.updateFunctions();
        mainWindow.panel_buttonContainer.updateFunctions();
        mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_SUBGRAPH_COPY);
    }

    public static void pasteFromClipboard() {
        if (!currentCopyPasteOperation.pasting) {
            currentCopyPasteOperation.startPasting();
            mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_SUBGRAPH_WHERE_TO_PASTE);
            //TODO
            mainWindow.getModeController().setMode(ModeType.VERTEX);
            mainWindow.getToolController().setTool(ToolType.SELECT);
        }
    }

    public static void cancelCurrentOperation() {
        if (currentCopyPasteOperation != null) {
            currentCopyPasteOperation = currentCopyPasteOperation.getCopy();
        }
        currentlyAddedEdge = null;
    }

    public static void processMouseClicking(MouseEvent e) {
        boolean createdLabel = false;

        int x = e.getX();
        int y = e.getY();

        if (getTool() != ToolType.SELECT) {
            mainWindow.getSelectionController().clearSelection();
            updatePropertyChangeOperationStatus(false);
        }

        boolean consumed = false;
        if (currentCopyPasteOperation != null) {
            if (currentCopyPasteOperation.pasting) {
                consumed = true;
                if (currentCopyPasteOperation.fitsIntoPosition()) {
                    operations.addNewOperation(currentCopyPasteOperation);
                    mainWindow.getGeneralController().publishInfo(operations.redo());
                    mainWindow.getSelectionController().addToSelection(currentCopyPasteOperation.vertices, false);
                    updatePropertyChangeOperationStatus(true);
                    currentCopyPasteOperation = currentCopyPasteOperation.getCopy();
                    mainWindow.menuBar.updateFunctions();
                    mainWindow.panel_buttonContainer.updateFunctions();
                } else {
                    mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_CANNOT_PASTE_SUBGRAPH);
                }
            }
        }

        if (!consumed) {
            if (mainWindow.getGeneralController().getMode() == ModeType.VERTEX) {
                if (getTool() == ToolType.ADD) {
                    Vertex vertex = new Vertex(mainWindow.getGeneralController().getGraph());
                    vertex.setModel(mainWindow.getGeneralController().getGraph().getVertexDefaultModel());
                    vertex.setPosX(x);
                    vertex.setPosY(y);
                    if (mainWindow.getGeneralController().getGraph().gridOn) {
                        VertexUtils.adjustToGrid(vertex);
                    }

                    if (VertexUtils.fitsIntoPage(vertex)) {

                        if (!GraphUtils.checkVertexCollision(mainWindow.getGeneralController().getGraph(), vertex)) {
                            VertexUtils.updateNumber(vertex, ControlManager.mainWindow.getGeneralController().getGraph().getGraphNumeration().getNextFreeNumber());
                            operations.addNewOperation(new AddOperation(vertex));
                            mainWindow.getGeneralController().publishInfo(operations.redo());
                            mainWindow.getSelectionController().addToSelection(vertex, false);
                        } else {
                            mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_CANNOT_CREATE_VERTEX_COLLISION);
                        }
                    } else {
                        mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_CANNOT_CREATE_VERTEX_BOUNDARY);
                    }
                } else if (getTool() == ToolType.REMOVE) {
                    Vertex temp = GraphUtils.getVertexFromPosition(mainWindow.getGeneralController().getGraph(), x, y);
                    if (temp != null) {
                        operations.addNewOperation(new RemoveOperation(temp));
                        mainWindow.getGeneralController().publishInfo(operations.redo());
                    } else {
                        mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (getTool() == ToolType.SELECT) {
                    if (!mousePressed) {
                        addToSelection(GraphUtils.getVertexFromPosition(mainWindow.getGeneralController().getGraph(), x, y), e.isControlDown());
                    }
                }
            } else if (mainWindow.getGeneralController().getMode() == ModeType.EDGE) {
                if (getTool() == ToolType.ADD) {
                    Vertex temp = GraphUtils.getVertexFromPosition(mainWindow.getGeneralController().getGraph(), x, y);
                    if (temp == null) {
                        if (currentlyAddedEdge == null) {
                            mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_CHOOSE_EDGE_START);
                        } else {
                            currentlyAddedEdge = null;
                            mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_EDGE_ADDING_CANCELLED);
                        }
                    } else {
                        if (currentlyAddedEdge == null) {
                            currentlyAddedEdge = new Edge(mainWindow.getGeneralController().getGraph());
                            currentlyAddedEdge.setModel(mainWindow.getGeneralController().getGraph().getEdgeDefaultModel());
                            currentlyAddedEdge.setVertexA(temp);
                            mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_CHOOSE_EDGE_END);
                        } else {
                            currentlyAddedEdge.setVertexB(temp);
                            currentlyAddedEdge.setDirected(e.isShiftDown());
                            operations.addNewOperation(new AddOperation(currentlyAddedEdge));
                            mainWindow.getGeneralController().publishInfo(operations.redo());
                            mainWindow.getSelectionController().addToSelection(currentlyAddedEdge, false);
                            currentlyAddedEdge = null;
                        }
                    }
                } else if (getTool() == ToolType.REMOVE) {
                    Edge temp = GraphUtils.getEdgeFromPosition(mainWindow.getGeneralController().getGraph(), x, y);
                    if (temp != null) {
                        operations.addNewOperation(new RemoveOperation(temp));
                        mainWindow.getGeneralController().publishInfo(operations.redo());
                    } else {
                        mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (getTool() == ToolType.SELECT) {
                    if (!mousePressed) {
                        addToSelection(GraphUtils.getEdgeFromPosition(mainWindow.getGeneralController().getGraph(), x, y), e.isControlDown());
                    }
                }
            } else if (mainWindow.getGeneralController().getMode() == ModeType.LABEL_VERTEX) {
                if (getTool() == ToolType.ADD) {
                    Vertex temp = GraphUtils.getVertexFromPosition(mainWindow.getGeneralController().getGraph(), x, y);
                    if (temp != null) {
                        if (temp.getLabel() == null) {
                            LabelV labelV = new LabelV(temp, mainWindow.getGeneralController().getGraph());
                            labelV.setModel(mainWindow.getGeneralController().getGraph().getLabelVDefaultModel());
                            operations.addNewOperation(new AddOperation(labelV));
                            mainWindow.getGeneralController().publishInfo(operations.redo());
                            mainWindow.getSelectionController().addToSelection(labelV, false);
                            createdLabel = true;
                        } else {
                            mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_CANNOT_CREATE_LABEL_V_EXISTS);
                        }
                    } else {
                        mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_CHOOSE_VERTEX_FOR_LABEL);
                    }
                } else if (getTool() == ToolType.REMOVE) {
                    LabelV temp = GraphUtils.getLabelVFromPosition(mainWindow.getGeneralController().getGraph(), x, y);
                    if (temp != null) {
                        operations.addNewOperation(new RemoveOperation(temp));
                        mainWindow.getGeneralController().publishInfo(operations.redo());
                    } else {
                        mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (getTool() == ToolType.SELECT) {
                    if (!mousePressed) {
                        addToSelection(GraphUtils.getLabelVFromPosition(mainWindow.getGeneralController().getGraph(), x, y), e.isControlDown());
                    }
                }
            } else {
                if (getTool() == ToolType.ADD) {
                    Edge temp = GraphUtils.getEdgeFromPosition(mainWindow.getGeneralController().getGraph(), x, y);
                    if (temp != null) {
                        if (temp.getLabel() == null) {
                            LabelE labelE = new LabelE(temp, mainWindow.getGeneralController().getGraph());
                            labelE.setModel(mainWindow.getGeneralController().getGraph().getLabelEDefaultModel());
                            labelE.setHorizontalPlacement(e.isShiftDown());
                            operations.addNewOperation(new AddOperation(labelE));
                            mainWindow.getGeneralController().publishInfo(operations.redo());
                            mainWindow.getSelectionController().addToSelection(labelE, false);
                            createdLabel = true;
                        } else {
                            mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_CANNOT_CREATE_LABEL_E_EXISTS);
                        }
                    } else {
                        mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_CHOOSE_EDGE_FOR_LABEL);
                    }
                } else if (getTool() == ToolType.REMOVE) {
                    LabelE temp = GraphUtils.getLabelEFromPosition(mainWindow.getGeneralController().getGraph(), x, y);
                    if (temp != null) {
                        operations.addNewOperation(new RemoveOperation(temp));
                        mainWindow.getGeneralController().publishInfo(operations.redo());
                    } else {
                        mainWindow.getGeneralController().publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (getTool() == ToolType.SELECT) {
                    if (!mousePressed) {
                        addToSelection(GraphUtils.getLabelEFromPosition(mainWindow.getGeneralController().getGraph(), x, y), e.isControlDown());
                    }
                }
            }
            updatePropertyChangeOperationStatus(true);
            if (createdLabel) {
                mainWindow.panel_propertyEditor.giveFocusToTextField();
            }
        }
    }

    public static void processMousePressing(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        mousePressed = true;
        mousePressX = x;
        mousePressY = y;

        if (getTool() != ToolType.REMOVE) {
            if (mainWindow.getGeneralController().getMode() == ModeType.VERTEX) {
                if (GraphUtils.getVertexFromPosition(mainWindow.getGeneralController().getGraph(), x, y) != null) {
                    if (mainWindow.getSelectionController().contains(GraphUtils.getVertexFromPosition(mainWindow.getGeneralController().getGraph(), x, y))) {
                        currentlyMovedElement = GraphUtils.getVertexFromPosition(mainWindow.getGeneralController().getGraph(), x, y);
                        currentDragOperation = new DragOperation(currentlyMovedElement);
                        currentDragOperation.setStartPos(((Vertex) currentlyMovedElement).getPosX(), ((Vertex) currentlyMovedElement).getPosY());
                    }
                }
            } else if (mainWindow.getGeneralController().getMode() == ModeType.EDGE) {
                if (GraphUtils.getEdgeFromPosition(mainWindow.getGeneralController().getGraph(), x, y) != null) {
                    if (mainWindow.getSelectionController().contains(GraphUtils.getEdgeFromPosition(mainWindow.getGeneralController().getGraph(), x, y))) {
                        Edge edge = GraphUtils.getEdgeFromPosition(mainWindow.getGeneralController().getGraph(), x, y);
                        currentlyMovedElement = edge;
                        currentDragOperation = new DragOperation(currentlyMovedElement);
                        if (edge.getVertexA() == edge.getVertexB()) {
                            Point c = new Point(x, y);
                            Point v = new Point(edge.getVertexA().getPosX(), edge.getVertexA().getPosY());
                            if (c.distance(v) > (edge.getVertexA().getRadius() + edge.getVertexA().getLineWidth() / 2) * 1.5) {
                                changingEdgeAngle = true;
                                currentDragOperation.setEdgeStartState(edge, true);
                            } else {
                                edgeDragDummy = new Vertex(mainWindow.getGeneralController().getGraph());
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
                                edgeDragDummy = new Vertex(mainWindow.getGeneralController().getGraph());
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
            } else if (mainWindow.getGeneralController().getMode() == ModeType.LABEL_VERTEX) {
                if (GraphUtils.getLabelVFromPosition(mainWindow.getGeneralController().getGraph(), x, y) != null) {
                    if (mainWindow.getSelectionController().contains(GraphUtils.getLabelVFromPosition(mainWindow.getGeneralController().getGraph(), x, y))) {
                        currentlyMovedElement = GraphUtils.getLabelVFromPosition(mainWindow.getGeneralController().getGraph(), x, y);
                        currentDragOperation = new DragOperation(currentlyMovedElement);
                        currentDragOperation.setStartAngle(((LabelV) currentlyMovedElement).getPosition());
                    }
                }
            } else if (mainWindow.getGeneralController().getMode() == ModeType.LABEL_EDGE) {
                if (GraphUtils.getLabelEFromPosition(mainWindow.getGeneralController().getGraph(), x, y) != null) {
                    if (mainWindow.getSelectionController().contains(GraphUtils.getLabelEFromPosition(mainWindow.getGeneralController().getGraph(), x, y))) {
                        currentlyMovedElement = GraphUtils.getLabelEFromPosition(mainWindow.getGeneralController().getGraph(), x, y);
                        currentDragOperation = new DragOperation(currentlyMovedElement);
                        currentDragOperation.setLabelEStartState((LabelE) currentlyMovedElement);
                    }
                }
            }
        }
    }

    public static void finishMovingElement() {
        if (currentlyMovedElement != null) {
            if (currentlyMovedElement instanceof Vertex) {
                currentDragOperation.setEndPos(((Vertex) currentlyMovedElement).getPosX(), ((Vertex) currentlyMovedElement).getPosY());
                if (currentDragOperation.changeMade()) {
                    operations.addNewOperation(currentDragOperation);
                    mainWindow.getGeneralController().publishInfo(operations.redo());
                }
            }
            if (currentlyMovedElement instanceof Edge) {
                Edge edge = (Edge) currentlyMovedElement;
                if (changingEdgeAngle) {
                    currentDragOperation.setEdgeEndState(edge);
                    if (currentDragOperation.changeMade()) {
                        operations.addNewOperation(currentDragOperation);
                        mainWindow.getGeneralController().publishInfo(operations.redo());
                    }
                    changingEdgeAngle = false;
                } else {
                    if (currentDragOperation.draggingA()) {
                        if (edge.getVertexA() != currentDragOperation.getDisjointedVertex()) {
                            if (edge.getVertexA() != edgeDragDummy) {
                                currentDragOperation.setEdgeEndState(edge);
                                operations.addNewOperation(currentDragOperation);
                                mainWindow.getGeneralController().publishInfo(operations.redo());
                            } else {
                                currentDragOperation.restoreEdgeStartState();
                            }
                        }
                    } else {
                        if (edge.getVertexB() != currentDragOperation.getDisjointedVertex()) {
                            if (edge.getVertexB() != edgeDragDummy) {
                                currentDragOperation.setEdgeEndState(edge);
                                operations.addNewOperation(currentDragOperation);
                                mainWindow.getGeneralController().publishInfo(operations.redo());
                            } else {
                                currentDragOperation.restoreEdgeStartState();
                            }
                        }
                    }
                }
            } else if (currentlyMovedElement instanceof LabelV) {
                currentDragOperation.setEndAngle(((LabelV) currentlyMovedElement).getPosition());
                if (currentDragOperation.changeMade()) {
                    operations.addNewOperation(currentDragOperation);
                    mainWindow.getGeneralController().publishInfo(operations.redo());
                }
            } else if (currentlyMovedElement instanceof LabelE) {
                currentDragOperation.setLabelEEndState((LabelE) currentlyMovedElement);
                if (currentDragOperation.changeMade()) {
                    operations.addNewOperation(currentDragOperation);
                    mainWindow.getGeneralController().publishInfo(operations.redo());
                }
            }

            updatePropertyChangeOperationStatus(true);
        }
    }

    public static void processMouseReleasing(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        mousePressed = false;
        finishMovingElement();

        if (getTool() != ToolType.ADD && currentlyMovedElement == null) {
            int x1 = Math.min(mousePressX, x);
            int width = Math.abs(x - mousePressX);
            int y1 = Math.min(mousePressY, y);
            int height = Math.abs(y - mousePressY);

            if (width + height > 2) {
                if (getTool() == ToolType.REMOVE) {
                    mainWindow.getSelectionController().clearSelection();
                    mainWindow.getSelectionController().addToSelection(GraphUtils.getIntersectingElements(mainWindow.getGeneralController().getGraph(), mainWindow.getGeneralController().getMode(), new Rectangle(x1, y1, width, height)), false);
                    deleteSelection();
                } else if (getTool() == ToolType.SELECT) {
                    addToSelection(GraphUtils.getIntersectingElements(mainWindow.getGeneralController().getGraph(), mainWindow.getGeneralController().getMode(), new Rectangle(x1, y1, width, height)), e.isControlDown());
                }
            }
        }

        currentlyMovedElement = null;
        currentDragOperation = null;
    }

    public static void processMouseMoving(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        if (currentlyAddedEdge != null) {
            currentlyAddedEdge.setDirected(e.isShiftDown());
        }
        if (currentlyMovedElement != null) {
            if (currentlyMovedElement instanceof Edge) {
                ((Edge) currentlyMovedElement).setDirected(e.isShiftDown());
            }
        }
    }

    public static void processShiftPressing(boolean pressed) {
        shiftDown = pressed;
        if (ControlManager.currentlyAddedEdge != null) {
            ControlManager.currentlyAddedEdge.setDirected(pressed);
        }
        if (currentlyMovedElement != null) {
            if (currentlyMovedElement instanceof Edge) {
                ((Edge) currentlyMovedElement).setDirected(pressed);
            }
            if (currentlyMovedElement instanceof LabelE) {
                ((LabelE) currentlyMovedElement).setHorizontalPlacement(!pressed);
            }
        }
        mainWindow.updateWorkspace();
    }

    public static void processMouseDragging(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        mouseX = x;
        mouseY = y;

        if (currentlyMovedElement != null) {
            if (mainWindow.getGeneralController().getMode() == ModeType.VERTEX) {
                Vertex vertex = (Vertex) currentlyMovedElement;
                mainWindow.getGeneralController().getGraph().getVertices().remove(vertex);
                int oldPosX = vertex.getPosX();
                int oldPosY = vertex.getPosY();

                vertex.setPosX(x);
                vertex.setPosY(y);

                if (mainWindow.getGeneralController().getGraph().gridOn) {
                    VertexUtils.adjustToGrid(vertex);
                }

                if (GraphUtils.checkVertexCollision(mainWindow.getGeneralController().getGraph(), vertex) || !VertexUtils.fitsIntoPage(vertex)) {
                    vertex.setPosX(oldPosX);
                    vertex.setPosY(oldPosY);
                }
                mainWindow.getGeneralController().getGraph().getVertices().add(vertex);
            } else if (mainWindow.getGeneralController().getMode() == ModeType.EDGE) {
                if (changingEdgeAngle) {
                    Edge edge = (Edge) currentlyMovedElement;
                    if (edge.getVertexA() == edge.getVertexB()) {
                        double angle = (Math.toDegrees(Math.atan2(mouseX - edge.getVertexB().getPosX(), mouseY - edge.getVertexB().getPosY())) + 270) % 360;
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
                    if ((vertex = GraphUtils.getVertexFromPosition(mainWindow.getGeneralController().getGraph(), x, y)) != null) {
                        Edge edge = (Edge) currentlyMovedElement;
                        if (currentDragOperation.draggingA()) {
                            edge.setVertexA(vertex);
                        } else {
                            edge.setVertexB(vertex);
                        }

                        if (edge.getVertexA() == edge.getVertexB()) {
                            double angle = (Math.toDegrees(Math.atan2(mouseX - edge.getVertexB().getPosX(), mouseY - edge.getVertexB().getPosY())) + 270) % 360;
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
            } else if (mainWindow.getGeneralController().getMode() == ModeType.LABEL_VERTEX) {
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
            } else if (mainWindow.getGeneralController().getMode() == ModeType.LABEL_EDGE) {
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
    }

    public static void paintCurrentlyAddedElement(Graphics2D g) {
        if (getTool() == ToolType.ADD) {
            if (mainWindow.getGeneralController().getMode() == ModeType.VERTEX) {
                Vertex vertex = new Vertex(mainWindow.getGeneralController().getGraph());
                vertex.setModel(mainWindow.getGeneralController().getGraph().getVertexDefaultModel());
                VertexUtils.updateNumber(vertex, mainWindow.getGeneralController().getGraph().getGraphNumeration().getNextFreeNumber());
                vertex.setPosX(mouseX);
                vertex.setPosY(mouseY);
                if (!GraphUtils.checkVertexCollision(mainWindow.getGeneralController().getGraph(), vertex) && VertexUtils.fitsIntoPage(vertex)) {
                    vertex.draw(g, true);
                }
            } else if (mainWindow.getGeneralController().getMode() == ModeType.EDGE) {
                if (currentlyAddedEdge != null) {
                    Vertex vertex = GraphUtils.getVertexFromPosition(mainWindow.getGeneralController().getGraph(), mouseX, mouseY);
                    if (vertex == null) {
                        vertex = new Vertex(mainWindow.getGeneralController().getGraph());
                        vertex.setPosX(mouseX);
                        vertex.setPosY(mouseY);
                        vertex.setRadius(2);
                        currentlyAddedEdge.setRelativeEdgeAngle(0);
                    }
                    currentlyAddedEdge.setVertexB(vertex);
                    if (GraphUtils.getVertexFromPosition(mainWindow.getGeneralController().getGraph(), mouseX, mouseY) != null) {
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
            } else if (mainWindow.getGeneralController().getMode() == ModeType.LABEL_VERTEX) {
                Vertex temp = GraphUtils.getVertexFromPosition(mainWindow.getGeneralController().getGraph(), mouseX, mouseY);
                if (temp != null) {
                    if (temp.getLabel() == null) {
                        LabelV labelV = new LabelV(temp, mainWindow.getGeneralController().getGraph());
                        labelV.setModel(mainWindow.getGeneralController().getGraph().getLabelVDefaultModel());

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

                        mainWindow.getGeneralController().getGraph().getLabelVDefaultModel().position = labelV.getPosition();
                        labelV.draw(g, true);
                    }
                }
            } else {
                Edge temp = GraphUtils.getEdgeFromPosition(mainWindow.getGeneralController().getGraph(), mouseX, mouseY);
                if (temp != null) {
                    if (temp.getLabel() == null) {
                        LabelE labelE = new LabelE(temp,mainWindow.getGeneralController().getGraph());
                        labelE.setModel(mainWindow.getGeneralController().getGraph().getLabelEDefaultModel());
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

                        mainWindow.getGeneralController().getGraph().getLabelEDefaultModel().topPlacement = 0;
                        if (labelE.isTopPlacement()) {
                            mainWindow.getGeneralController().getGraph().getLabelEDefaultModel().topPlacement = 1;
                        }
                        mainWindow.getGeneralController().getGraph().getLabelEDefaultModel().position = labelE.getPosition();
                        labelE.draw(g, true);
                    }
                }
            }
        }
    }
}
