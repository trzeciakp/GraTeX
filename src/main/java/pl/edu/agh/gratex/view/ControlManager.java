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

    public static File currentFile;

    public static ModeType mode = ModeType.VERTEX;
    public static ToolType tool = ToolType.ADD;

    public static Graph graph = new Graph();
    public static Edge currentlyAddedEdge = null;
    //public static LinkedList<GraphElement> selection = new LinkedList<>();
    public static int selectionID = 0;
    public static OperationList operations = null;
    public static GraphElement currentlyMovedElement = null;
    public static DragOperation currentDragOperation = null;
    public static boolean changingEdgeAngle;
    public static PropertyChangeOperation currentPropertyChangeOperation = null;
    public static CopyPasteOperation currentCopyPasteOperation = null;
    public static Vertex edgeDragDummy = null;
    public static boolean changeMade = false;

    public static boolean shiftDown;
    public static boolean mousePressed;
    public static int mousePressX;
    public static int mousePressY;
    public static int mouseX;
    public static int mouseY;

    public static void passWindowHandle(MainWindow _mainWindow) {
        mainWindow = _mainWindow;
    }

    public static ModeType getMode() {
        return mode;
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

    public static void changeMode(ModeType _mode) {
        mode = _mode;
        applyChange();
    }

    public static void changeTool(ToolType _tool) {
        tool = _tool;
        applyChange();
    }

    public static void publishInfo(String entry) {
        mainWindow.label_info.setText(entry);
    }

    public static void reportError(String message) {
        JOptionPane.showMessageDialog(mainWindow, message, StringLiterals.TITLE_ERROR_DIALOG, JOptionPane.ERROR_MESSAGE);
    }

    public static void selectAll() {
        mainWindow.getSelectionController().selectAll();
//        if (getTool() == ToolType.SELECT) {
//            selection.clear();
//            selection.addAll(ControlManager.graph.getElements(getMode().getRelatedElementType()));
//            updatePropertyChangeOperationStatus(true);
//            mainWindow.updateWorkspace();
//        }
    }

    public static void addToSelection(List<GraphElement> elements, boolean controlDown) {
        mainWindow.getSelectionController().addToSelection(elements, controlDown);
//        if (!controlDown) {
//            selection.clear();
//            selection.addAll(elements);
//        } else {
//            for(GraphElement temp : elements) {
//                if (selection.contains(temp)) {
//                    selection.remove(temp);
//                } else {
//                    selection.add(temp);
//                }
//            }
//        }
        updatePropertyChangeOperationStatus(true);
    }

    public static void addToSelection(GraphElement element, boolean controlDown) {
        mainWindow.getSelectionController().addToSelection(element, controlDown);
//        if (element != null) {
//            if (controlDown) {
//                if (selection.contains(element)) {
//                    selection.remove(element);
//                } else {
//                    selection.add(element);
//                }
//            } else {
//                selection.clear();
//                selection.add(element);
//            }
//        } else {
//            if (!controlDown) {
//                selection.clear();
//            }
//        }

        updatePropertyChangeOperationStatus(true);
    }

    public static void deleteSelection() {
        if (mainWindow.getSelectionController().getSize() > 0) {
            operations.addNewOperation(new RemoveOperation(mainWindow.getSelectionController().getSelection()));
            publishInfo(operations.redo());
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
            if (getMode() == ModeType.VERTEX) {
                mainWindow.panel_propertyEditor.setModel(new VertexPropertyModel());
            } else if (getMode() == ModeType.EDGE) {
                mainWindow.panel_propertyEditor.setModel(new EdgePropertyModel());
            } else if (getMode() == ModeType.LABEL_VERTEX) {
                mainWindow.panel_propertyEditor.setModel(new LabelVertexPropertyModel());
            } else if (getMode() == ModeType.LABEL_EDGE) {
                mainWindow.panel_propertyEditor.setModel(new LabelEdgePropertyModel());
            }
        }
    }

    public static void updateSelectedItemsModel(PropertyModel pm) {
        if (currentPropertyChangeOperation != null) {
            if (!operations.mergePropertyChangeOperations(pm, selectionID)) {
                currentPropertyChangeOperation.setEndModel(pm);
                operations.addNewOperation(currentPropertyChangeOperation);
                publishInfo(operations.redo());
            }
            mainWindow.updateWorkspace();
            updatePropertyChangeOperationStatus(false);
        }
    }

    public static void newGraphFile() {
        if (checkForUnsavedProgress()) {
            graph = new Graph();
            operations = new OperationList();
            applyChange();
            changeMade = false;
            currentFile = null;
            editGraphTemplate();
        }
    }

    public static void openGraphFile() {
        if (checkForUnsavedProgress()) {
            OpenFileDialog chooser;
            if (currentFile != null) {
                chooser = new OpenFileDialog(currentFile);
            } else {
                chooser = new OpenFileDialog();
            }
            chooser.showDialog(mainWindow);
        }
    }

    public static boolean saveGraphFile(boolean saveAs) {
        if (!saveAs && currentFile != null) {
            return FileManager.saveFile(currentFile, graph);
        } else {
            SaveFileDialog chooser;
            if (currentFile != null) {
                chooser = new SaveFileDialog(currentFile);
            } else {
                chooser = new SaveFileDialog();
            }
            return chooser.showDialog(mainWindow);
        }
    }

    public static void undo() {
        publishInfo(operations.undo());
        mainWindow.updateWorkspace();
        mainWindow.getSelectionController().clearSelection();
        updatePropertyChangeOperationStatus(false);
        changeMade = true;
    }

    public static void redo() {
        publishInfo(operations.redo());
        mainWindow.updateWorkspace();
        mainWindow.getSelectionController().clearSelection();
        changeMade = true;
    }

    public static void toggleGrid() {
        if (graph.gridOn) {
            graph.gridOn = false;
        } else {
            GridDialog gd = new GridDialog(mainWindow, graph.gridResolutionX, graph.gridResolutionY);
            int[] result = gd.showDialog();
            if (result != null) {
                graph.gridOn = true;
                graph.gridResolutionX = result[0];
                graph.gridResolutionY = result[1];
                GraphUtils.adjustVerticesToGrid(graph);
            }
        }
        mainWindow.updateFunctions();
    }

    public static void setNumeration() {
        mainWindow.getSelectionController().clearSelection();
        updatePropertyChangeOperationStatus(false);
        NumerationDialog nd = new NumerationDialog(mainWindow, graph.getGraphNumeration().isNumerationDigital(), graph.getGraphNumeration().getStartingNumber(), Const.MAX_VERTEX_NUMBER);
        int[] result = nd.showDialog();

        if (result != null) {
            graph.getGraphNumeration().setNumerationDigital(result[0] == 0);
            graph.getGraphNumeration().setStartingNumber(result[1]);
        }

        mainWindow.updateFunctions();
    }

    public static void parseToTeX() {
        new LatexCodeDialog(mainWindow, Parser.parse(graph));
    }

    public static void copyToClipboard() {
        currentCopyPasteOperation = new CopyPasteOperation(mainWindow.getSelectionController().getSelection());
        mainWindow.menuBar.updateFunctions();
        mainWindow.panel_buttonContainer.updateFunctions();
        publishInfo(StringLiterals.INFO_SUBGRAPH_COPY);
    }

    public static void pasteFromClipboard() {
        if (!currentCopyPasteOperation.pasting) {
            currentCopyPasteOperation.startPasting();
            publishInfo(StringLiterals.INFO_SUBGRAPH_WHERE_TO_PASTE);
            //TODO
            changeMode(ModeType.VERTEX);
            changeTool(ToolType.SELECT);
        }
    }

    public static void editGraphTemplate() {
        GraphsTemplateDialog gdd = new GraphsTemplateDialog(mainWindow);
        Graph templateGraph = gdd.displayDialog();

        if (templateGraph != null) {
            operations.addNewOperation(new TemplateChangeOperation(graph, templateGraph));
            publishInfo(operations.redo());
        }
    }

    public static void cancelCurrentOperation() {
        if (currentCopyPasteOperation != null) {
            currentCopyPasteOperation = currentCopyPasteOperation.getCopy();
        }
        currentlyAddedEdge = null;
    }

    public static void showAboutDialog() {
        new AboutDialog(mainWindow);
    }

    public static void processMouseClicking(MouseEvent e) {
        boolean createdLabel = false;

        int x = e.getX();
        int y = e.getY();
        changeMade = true;

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
                    publishInfo(operations.redo());
                    mainWindow.getSelectionController().addToSelection(currentCopyPasteOperation.vertices, false);
                    updatePropertyChangeOperationStatus(true);
                    currentCopyPasteOperation = currentCopyPasteOperation.getCopy();
                    mainWindow.menuBar.updateFunctions();
                    mainWindow.panel_buttonContainer.updateFunctions();
                } else {
                    publishInfo(StringLiterals.INFO_CANNOT_PASTE_SUBGRAPH);
                }
            }
        }

        if (!consumed) {
            if (getMode() == ModeType.VERTEX) {
                if (getTool() == ToolType.ADD) {
                    Vertex vertex = new Vertex(graph);
                    vertex.setModel(graph.getVertexDefaultModel());
                    vertex.setPosX(x);
                    vertex.setPosY(y);
                    if (graph.gridOn) {
                        VertexUtils.adjustToGrid(vertex);
                    }

                    if (VertexUtils.fitsIntoPage(vertex)) {

                        if (!GraphUtils.checkVertexCollision(graph, vertex)) {
                            VertexUtils.updateNumber(vertex, ControlManager.graph.getGraphNumeration().getNextFreeNumber());
                            operations.addNewOperation(new AddOperation(vertex));
                            publishInfo(operations.redo());
                            mainWindow.getSelectionController().addToSelection(vertex, false);
                        } else {
                            publishInfo(StringLiterals.INFO_CANNOT_CREATE_VERTEX_COLLISION);
                        }
                    } else {
                        publishInfo(StringLiterals.INFO_CANNOT_CREATE_VERTEX_BOUNDARY);
                    }
                } else if (getTool() == ToolType.REMOVE) {
                    Vertex temp = GraphUtils.getVertexFromPosition(graph, x, y);
                    if (temp != null) {
                        operations.addNewOperation(new RemoveOperation(temp));
                        publishInfo(operations.redo());
                    } else {
                        publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (getTool() == ToolType.SELECT) {
                    if (!mousePressed) {
                        addToSelection(GraphUtils.getVertexFromPosition(graph, x, y), e.isControlDown());
                    }
                }
            } else if (getMode() == ModeType.EDGE) {
                if (getTool() == ToolType.ADD) {
                    Vertex temp = GraphUtils.getVertexFromPosition(graph, x, y);
                    if (temp == null) {
                        if (currentlyAddedEdge == null) {
                            publishInfo(StringLiterals.INFO_CHOOSE_EDGE_START);
                        } else {
                            currentlyAddedEdge = null;
                            publishInfo(StringLiterals.INFO_EDGE_ADDING_CANCELLED);
                        }
                    } else {
                        if (currentlyAddedEdge == null) {
                            currentlyAddedEdge = new Edge(graph);
                            currentlyAddedEdge.setModel(graph.getEdgeDefaultModel());
                            currentlyAddedEdge.setVertexA(temp);
                            publishInfo(StringLiterals.INFO_CHOOSE_EDGE_END);
                        } else {
                            currentlyAddedEdge.setVertexB(temp);
                            currentlyAddedEdge.setDirected(e.isShiftDown());
                            operations.addNewOperation(new AddOperation(currentlyAddedEdge));
                            publishInfo(operations.redo());
                            mainWindow.getSelectionController().addToSelection(currentlyAddedEdge, false);
                            currentlyAddedEdge = null;
                        }
                    }
                } else if (getTool() == ToolType.REMOVE) {
                    Edge temp = GraphUtils.getEdgeFromPosition(graph, x, y);
                    if (temp != null) {
                        operations.addNewOperation(new RemoveOperation(temp));
                        publishInfo(operations.redo());
                    } else {
                        publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (getTool() == ToolType.SELECT) {
                    if (!mousePressed) {
                        addToSelection(GraphUtils.getEdgeFromPosition(graph, x, y), e.isControlDown());
                    }
                }
            } else if (getMode() == ModeType.LABEL_VERTEX) {
                if (getTool() == ToolType.ADD) {
                    Vertex temp = GraphUtils.getVertexFromPosition(graph, x, y);
                    if (temp != null) {
                        if (temp.getLabel() == null) {
                            LabelV labelV = new LabelV(temp, graph);
                            labelV.setModel(graph.getLabelVDefaultModel());
                            operations.addNewOperation(new AddOperation(labelV));
                            publishInfo(operations.redo());
                            mainWindow.getSelectionController().addToSelection(labelV, false);
                            createdLabel = true;
                        } else {
                            publishInfo(StringLiterals.INFO_CANNOT_CREATE_LABEL_V_EXISTS);
                        }
                    } else {
                        publishInfo(StringLiterals.INFO_CHOOSE_VERTEX_FOR_LABEL);
                    }
                } else if (getTool() == ToolType.REMOVE) {
                    LabelV temp = GraphUtils.getLabelVFromPosition(graph, x, y);
                    if (temp != null) {
                        operations.addNewOperation(new RemoveOperation(temp));
                        publishInfo(operations.redo());
                    } else {
                        publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (getTool() == ToolType.SELECT) {
                    if (!mousePressed) {
                        addToSelection(GraphUtils.getLabelVFromPosition(graph, x, y), e.isControlDown());
                    }
                }
            } else {
                if (getTool() == ToolType.ADD) {
                    Edge temp = GraphUtils.getEdgeFromPosition(graph, x, y);
                    if (temp != null) {
                        if (temp.getLabel() == null) {
                            LabelE labelE = new LabelE(temp, graph);
                            labelE.setModel(graph.getLabelEDefaultModel());
                            labelE.setHorizontalPlacement(e.isShiftDown());
                            operations.addNewOperation(new AddOperation(labelE));
                            publishInfo(operations.redo());
                            mainWindow.getSelectionController().addToSelection(labelE, false);
                            createdLabel = true;
                        } else {
                            publishInfo(StringLiterals.INFO_CANNOT_CREATE_LABEL_E_EXISTS);
                        }
                    } else {
                        publishInfo(StringLiterals.INFO_CHOOSE_EDGE_FOR_LABEL);
                    }
                } else if (getTool() == ToolType.REMOVE) {
                    LabelE temp = GraphUtils.getLabelEFromPosition(graph, x, y);
                    if (temp != null) {
                        operations.addNewOperation(new RemoveOperation(temp));
                        publishInfo(operations.redo());
                    } else {
                        publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
                    }
                } else if (getTool() == ToolType.SELECT) {
                    if (!mousePressed) {
                        addToSelection(GraphUtils.getLabelEFromPosition(graph, x, y), e.isControlDown());
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
        changeMade = true;
        mousePressed = true;
        mousePressX = x;
        mousePressY = y;

        if (getTool() != ToolType.REMOVE) {
            if (getMode() == ModeType.VERTEX) {
                if (GraphUtils.getVertexFromPosition(graph, x, y) != null) {
                    if (mainWindow.getSelectionController().contains(GraphUtils.getVertexFromPosition(graph, x, y))) {
                        currentlyMovedElement = GraphUtils.getVertexFromPosition(graph, x, y);
                        currentDragOperation = new DragOperation(currentlyMovedElement);
                        currentDragOperation.setStartPos(((Vertex) currentlyMovedElement).getPosX(), ((Vertex) currentlyMovedElement).getPosY());
                    }
                }
            } else if (getMode() == ModeType.EDGE) {
                if (GraphUtils.getEdgeFromPosition(graph, x, y) != null) {
                    if (mainWindow.getSelectionController().contains(GraphUtils.getEdgeFromPosition(graph, x, y))) {
                        Edge edge = GraphUtils.getEdgeFromPosition(graph, x, y);
                        currentlyMovedElement = edge;
                        currentDragOperation = new DragOperation(currentlyMovedElement);
                        if (edge.getVertexA() == edge.getVertexB()) {
                            Point c = new Point(x, y);
                            Point v = new Point(edge.getVertexA().getPosX(), edge.getVertexA().getPosY());
                            if (c.distance(v) > (edge.getVertexA().getRadius() + edge.getVertexA().getLineWidth() / 2) * 1.5) {
                                changingEdgeAngle = true;
                                currentDragOperation.setEdgeStartState(edge, true);
                            } else {
                                edgeDragDummy = new Vertex(graph);
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
                                edgeDragDummy = new Vertex(graph);
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
            } else if (getMode() == ModeType.LABEL_VERTEX) {
                if (GraphUtils.getLabelVFromPosition(graph, x, y) != null) {
                    if (mainWindow.getSelectionController().contains(GraphUtils.getLabelVFromPosition(graph, x, y))) {
                        currentlyMovedElement = GraphUtils.getLabelVFromPosition(graph, x, y);
                        currentDragOperation = new DragOperation(currentlyMovedElement);
                        currentDragOperation.setStartAngle(((LabelV) currentlyMovedElement).getPosition());
                    }
                }
            } else if (getMode() == ModeType.LABEL_EDGE) {
                if (GraphUtils.getLabelEFromPosition(graph, x, y) != null) {
                    if (mainWindow.getSelectionController().contains(GraphUtils.getLabelEFromPosition(graph, x, y))) {
                        currentlyMovedElement = GraphUtils.getLabelEFromPosition(graph, x, y);
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
                    publishInfo(operations.redo());
                }
            }
            if (currentlyMovedElement instanceof Edge) {
                Edge edge = (Edge) currentlyMovedElement;
                if (changingEdgeAngle) {
                    currentDragOperation.setEdgeEndState(edge);
                    if (currentDragOperation.changeMade()) {
                        operations.addNewOperation(currentDragOperation);
                        publishInfo(operations.redo());
                    }
                    changingEdgeAngle = false;
                } else {
                    if (currentDragOperation.draggingA()) {
                        if (edge.getVertexA() != currentDragOperation.getDisjointedVertex()) {
                            if (edge.getVertexA() != edgeDragDummy) {
                                currentDragOperation.setEdgeEndState(edge);
                                operations.addNewOperation(currentDragOperation);
                                publishInfo(operations.redo());
                            } else {
                                currentDragOperation.restoreEdgeStartState();
                            }
                        }
                    } else {
                        if (edge.getVertexB() != currentDragOperation.getDisjointedVertex()) {
                            if (edge.getVertexB() != edgeDragDummy) {
                                currentDragOperation.setEdgeEndState(edge);
                                operations.addNewOperation(currentDragOperation);
                                publishInfo(operations.redo());
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
                    publishInfo(operations.redo());
                }
            } else if (currentlyMovedElement instanceof LabelE) {
                currentDragOperation.setLabelEEndState((LabelE) currentlyMovedElement);
                if (currentDragOperation.changeMade()) {
                    operations.addNewOperation(currentDragOperation);
                    publishInfo(operations.redo());
                }
            }

            updatePropertyChangeOperationStatus(true);
        }
    }

    public static void processMouseReleasing(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        changeMade = true;
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
                    mainWindow.getSelectionController().addToSelection(GraphUtils.getIntersectingElements(graph, mode, new Rectangle(x1, y1, width, height)), false);
                    deleteSelection();
                } else if (getTool() == ToolType.SELECT) {
                    addToSelection(GraphUtils.getIntersectingElements(graph, mode, new Rectangle(x1, y1, width, height)), e.isControlDown());
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
        changeMade = true;
        mouseX = x;
        mouseY = y;

        if (currentlyMovedElement != null) {
            if (getMode() == ModeType.VERTEX) {
                Vertex vertex = (Vertex) currentlyMovedElement;
                graph.getVertices().remove(vertex);
                int oldPosX = vertex.getPosX();
                int oldPosY = vertex.getPosY();

                vertex.setPosX(x);
                vertex.setPosY(y);

                if (graph.gridOn) {
                    VertexUtils.adjustToGrid(vertex);
                }

                if (GraphUtils.checkVertexCollision(graph, vertex) || !VertexUtils.fitsIntoPage(vertex)) {
                    vertex.setPosX(oldPosX);
                    vertex.setPosY(oldPosY);
                }
                graph.getVertices().add(vertex);
            } else if (getMode() == ModeType.EDGE) {
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
                    if ((vertex = GraphUtils.getVertexFromPosition(graph, x, y)) != null) {
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
            } else if (getMode() == ModeType.LABEL_VERTEX) {
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
            } else if (getMode() == ModeType.LABEL_EDGE) {
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
            if (getMode() == ModeType.VERTEX) {
                Vertex vertex = new Vertex(graph);
                vertex.setModel(graph.getVertexDefaultModel());
                VertexUtils.updateNumber(vertex, graph.getGraphNumeration().getNextFreeNumber());
                vertex.setPosX(mouseX);
                vertex.setPosY(mouseY);
                if (!GraphUtils.checkVertexCollision(graph, vertex) && VertexUtils.fitsIntoPage(vertex)) {
                    vertex.draw(g, true);
                }
            } else if (getMode() == ModeType.EDGE) {
                if (currentlyAddedEdge != null) {
                    Vertex vertex = GraphUtils.getVertexFromPosition(graph, mouseX, mouseY);
                    if (vertex == null) {
                        vertex = new Vertex(graph);
                        vertex.setPosX(mouseX);
                        vertex.setPosY(mouseY);
                        vertex.setRadius(2);
                        currentlyAddedEdge.setRelativeEdgeAngle(0);
                    }
                    currentlyAddedEdge.setVertexB(vertex);
                    if (GraphUtils.getVertexFromPosition(graph, mouseX, mouseY) != null) {
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
            } else if (getMode() == ModeType.LABEL_VERTEX) {
                Vertex temp = GraphUtils.getVertexFromPosition(graph, mouseX, mouseY);
                if (temp != null) {
                    if (temp.getLabel() == null) {
                        LabelV labelV = new LabelV(temp, graph);
                        labelV.setModel(graph.getLabelVDefaultModel());

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

                        graph.getLabelVDefaultModel().position = labelV.getPosition();
                        labelV.draw(g, true);
                    }
                }
            } else {
                Edge temp = GraphUtils.getEdgeFromPosition(graph, mouseX, mouseY);
                if (temp != null) {
                    if (temp.getLabel() == null) {
                        LabelE labelE = new LabelE(temp,graph);
                        labelE.setModel(graph.getLabelEDefaultModel());
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

                        graph.getLabelEDefaultModel().topPlacement = 0;
                        if (labelE.isTopPlacement()) {
                            graph.getLabelEDefaultModel().topPlacement = 1;
                        }
                        graph.getLabelEDefaultModel().position = labelE.getPosition();
                        labelE.draw(g, true);
                    }
                }
            }
        }
    }

    public static void paintGrid(Graphics2D g) {
        if (graph.gridOn) {
            g.setColor(Const.GRID_COLOR);
            int i = 0;
            while ((i += graph.gridResolutionX) < graph.pageWidth) {
                g.drawLine(i, 0, i, graph.pageHeight);
            }
            i = 0;
            while ((i += graph.gridResolutionY) < graph.pageHeight) {
                g.drawLine(0, i, graph.pageWidth, i);
            }
        }
    }

    public static void paintSelectionArea(Graphics2D g) {
        if (getTool() != ToolType.ADD && currentlyMovedElement == null) {
            if (mousePressed && (mouseX != mousePressX || mouseY != mousePressY)) {
                int x = Math.min(mousePressX, mouseX);
                int width = Math.abs(mouseX - mousePressX);
                int y = Math.min(mousePressY, mouseY);
                int height = Math.abs(mouseY - mousePressY);

                g.setColor(Const.SELECTION_RECT_INSIDE_COLOR);
                g.fillRect(x, y, width, height);

                g.setColor(Const.SELECTION_RECT_BORDER_COLOR);
                g.drawRect(x, y, width, height);
            }
        }
    }

    public static void paintCopiedSubgraph(Graphics2D g) {
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

    public static boolean checkForUnsavedProgress() {
        if (changeMade) {
            Object[] options = {"Save", "Don't save", "Cancel"};
            int option = JOptionPane.showOptionDialog(mainWindow, "There have been changes since last save.\n"
                    + "Would you like to save your graph now?", "Unsaved progress", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);

            if (option == JOptionPane.CANCEL_OPTION) {
                return false;
            } else if (option == JOptionPane.NO_OPTION) {
                return true;
            } else {
                return saveGraphFile(true);
            }
        } else {
            return true;
        }
    }

    public static void exitApplication() {
        if (checkForUnsavedProgress()) {
            System.exit(0);
        }
    }
}
