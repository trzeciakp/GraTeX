package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.AlterationOperation;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.controller.operation.GenericOperation;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.edge.EdgeUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.view.Application;

import java.awt.*;


public class EdgeMouseControllerImpl extends GraphElementMouseController {
    private Edge currentlyAddedEdge;
    private Edge currentlyDraggedEdge;
    private AlterationOperation currentDragOperation;
    private boolean changingEdgeAngle;
    private boolean disconnectedVertexA;
    private Vertex edgeDragDummy;
    private String initialLatexCodeOfDraggedEdge;

    public EdgeMouseControllerImpl(GeneralController generalController, GraphElementFactory graphElementFactory) {
        super(generalController, graphElementFactory);
        edgeDragDummy = (Vertex) getGraphElementFactory().create(GraphElementType.VERTEX, null);
    }

    @Override
    public void shiftDownChanged() {
        if (currentlyAddedEdge != null) {
            currentlyAddedEdge.setDirected(shiftDown);
        }
        if (currentlyDraggedEdge != null) {
            currentlyDraggedEdge.setDirected(shiftDown);
        }
    }

    @Override
    public void reset() {
        finishMoving();
        currentlyDraggedEdge = null;
        currentDragOperation = null;
        currentlyAddedEdge = null;
        changingEdgeAngle = false;
    }

    @Override
    public GraphElement getCurrentlyAddedElement() {
        if (currentlyAddedEdge != null) {
            Vertex vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY);
            if (vertex == null) {
                vertex = (Vertex) getGraphElementFactory().create(GraphElementType.VERTEX, generalController.getGraph());
                vertex.setPosX(mouseX);
                vertex.setPosY(mouseY);
                vertex.setRadius(2);
                currentlyAddedEdge.setRelativeEdgeAngle(0);
            }
            if (disconnectedVertexA) {
                currentlyAddedEdge.setVertexA(vertex);
            } else {
                currentlyAddedEdge.setVertexB(vertex);
            }
            if (generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY) != null) {
                int angle = 0;
                if (!ctrlDown || currentlyAddedEdge.isLoop()) {
                    angle = EdgeUtils.getEdgeAngleFromCursorLocation(currentlyAddedEdge, mouseX, mouseY);
                }
                currentlyAddedEdge.setRelativeEdgeAngle(angle);
            }
            if (currentlyAddedEdge.getRelativeEdgeAngle() != 0) {
                currentlyAddedEdge.setDrawer(getGraphElementFactory().getDrawerFactory().createDummyEdgeDrawer());
            } else {
                currentlyAddedEdge.setDrawer(getGraphElementFactory().getDrawerFactory().createDefaultDrawer(GraphElementType.EDGE));
            }
            return currentlyAddedEdge;
        }
        return null;
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        Vertex vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY);
        if (vertex == null) {
            if (currentlyAddedEdge == null) {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_EDGE_START));
            } else {
                currentlyAddedEdge = null;
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_EDGE_ADDING_CANCELLED));
            }
        } else {
            if (currentlyAddedEdge == null) {
                currentlyAddedEdge = (Edge) getGraphElementFactory().create(GraphElementType.EDGE, generalController.getGraph());
                currentlyAddedEdge.setVertexA(vertex);
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_EDGE_END));
            } else {
                currentlyAddedEdge.setVertexB(vertex);
                currentlyAddedEdge.setDrawer(getGraphElementFactory().getDrawerFactory().createDefaultDrawer(GraphElementType.EDGE));
                new CreationRemovalOperation(generalController, currentlyAddedEdge, OperationType.ADD_EDGE, StringLiterals.INFO_EDGE_ADD, true);
                currentlyAddedEdge = null;
            }
        }
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {
        if (currentlyDraggedEdge == null) {
            startMoving(mouseX, mouseY);
        } else {
            continueMoving(mouseX, mouseY);
        }
    }

    private void startMoving(int mouseX, int mouseY) {
        Edge edge = (Edge) generalController.getGraph().getElementFromPosition(GraphElementType.EDGE, mouseX, mouseY);
        currentlyDraggedEdge = edge;
        initialLatexCodeOfDraggedEdge = generalController.getParseController().getParserByElementType(GraphElementType.EDGE).parseToLatex(currentlyDraggedEdge);

        currentDragOperation = new AlterationOperation(generalController, currentlyDraggedEdge, OperationType.MOVE_EDGE, StringLiterals.INFO_EDGE_MOVE);
        if (edge.getVertexA() == edge.getVertexB()) {
            Point c = new Point(mouseX, mouseY);
            Point v = new Point(edge.getVertexA().getPosX(), edge.getVertexA().getPosY());
            if (c.distance(v) > (edge.getVertexA().getRadius() + edge.getVertexA().getLineWidth() / 2) * 1.5) {
                changingEdgeAngle = true;
            } else {
                edgeDragDummy.setPosX(mouseX);
                edgeDragDummy.setPosY(mouseY);
                edgeDragDummy.setRadius(2);

                switch (currentlyDraggedEdge.getRelativeEdgeAngle()) {
                    case 0:
                        disconnectedVertexA = mouseY < edge.getVertexA().getPosY();
                        break;
                    case 90:
                        disconnectedVertexA = mouseX < edge.getVertexA().getPosX();
                        break;
                    case 180:
                        disconnectedVertexA = mouseY > edge.getVertexA().getPosY();
                        break;
                    case 270:
                        disconnectedVertexA = mouseX > edge.getVertexA().getPosX();
                        break;
                }

                if (disconnectedVertexA) {
                    edge.setVertexA(edgeDragDummy);
                } else {
                    edge.setVertexB(edgeDragDummy);
                }

            }
        } else {
            Point c = new Point(mouseX, mouseY);
            Point va = new Point(edge.getVertexA().getPosX(), edge.getVertexA().getPosY());
            Point vb = new Point(edge.getVertexB().getPosX(), edge.getVertexB().getPosY());
            if (c.distance(va) / c.distance(vb) < 2 && c.distance(va) / c.distance(vb) > 0.5) {
                changingEdgeAngle = true;
            } else {
                edgeDragDummy.setPosX(mouseX);
                edgeDragDummy.setPosY(mouseY);
                edgeDragDummy.setRadius(2);
                if (c.distance(va) < c.distance(vb)) {
                    edge.setVertexA(edgeDragDummy);
                    disconnectedVertexA = true;
                } else {
                    edge.setVertexB(edgeDragDummy);
                    disconnectedVertexA = false;
                }
            }
        }
        if (!changingEdgeAngle) {
            currentlyDraggedEdge.setRelativeEdgeAngle(0);
        }
    }

    private void continueMoving(int mouseX, int mouseY) {
        generalController.getSelectionController().addToSelection(currentlyDraggedEdge, false);
        if (changingEdgeAngle) {
            currentlyDraggedEdge.setRelativeEdgeAngle(EdgeUtils.getEdgeAngleFromCursorLocation(currentlyDraggedEdge, mouseX, mouseY));
        } else {
            Vertex vertex;
            if ((vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY)) != null) {
                if (disconnectedVertexA) {
                    currentlyDraggedEdge.setVertexA(vertex);
                } else {
                    currentlyDraggedEdge.setVertexB(vertex);
                }
                currentlyDraggedEdge.setRelativeEdgeAngle(EdgeUtils.getEdgeAngleFromCursorLocation(currentlyDraggedEdge, mouseX, mouseY));
            } else {
                edgeDragDummy.setPosX(mouseX);
                edgeDragDummy.setPosY(mouseY);
                currentlyDraggedEdge.setRelativeEdgeAngle(0);
                if (disconnectedVertexA) {
                    currentlyDraggedEdge.setVertexA(edgeDragDummy);
                } else {
                    currentlyDraggedEdge.setVertexB(edgeDragDummy);
                }
            }
        }
    }

    @Override
    public void finishMoving() {
        if (currentlyDraggedEdge != null) {
            if (currentlyDraggedEdge.getVertexA() != edgeDragDummy && currentlyDraggedEdge.getVertexB() != edgeDragDummy) {
                currentDragOperation.finish();
            } else {
                // Restore original edge state (it was dropped in mid air)
                try {
                    generalController.getParseController().getEdgeParser().updateElementWithCode(currentlyDraggedEdge, initialLatexCodeOfDraggedEdge);
                } catch (Exception e) {
                    e.printStackTrace();
                    Application.criticalError("Parser error", e);
                }
            }
            changingEdgeAngle = false;
            currentlyDraggedEdge = null;
        }
    }
}
