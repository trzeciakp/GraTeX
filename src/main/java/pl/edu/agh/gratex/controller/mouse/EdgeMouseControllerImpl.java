package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.AlterationOperation;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.controller.operation.GenericOperation;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *
 */
public class EdgeMouseControllerImpl extends GraphElementMouseController {
    private GeneralController generalController;

    private Edge currentlyAddedEdge;
    private Edge currentlyDraggedEdge;
    private AlterationOperation currentDragOperation;
    private boolean changingEdgeAngle;
    private Vertex edgeDragDummy = new Vertex(null);

    public EdgeMouseControllerImpl(GeneralController generalController) {
        super(generalController);
        this.generalController = generalController;
    }

    @Override
    public void processMouseMoving(ToolType toolType, MouseEvent e) {
        super.processMouseMoving(toolType, e);
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
        finishMovingElement();
        currentlyDraggedEdge = null;
        currentDragOperation = null;
        currentlyAddedEdge = null;
        changingEdgeAngle = false;
    }

    @Override
    public void paintCurrentlyAddedElement(Graphics2D g) {
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
    }

    @Override
    public Edge getElementFromPosition(MouseEvent e) {
        return GraphUtils.getEdgeFromPosition(generalController.getGraph(), e.getX(), e.getY());
    }

    @Override
    public void addNewElement(MouseEvent e) {
        Vertex temp = GraphUtils.getVertexFromPosition(generalController.getGraph(), e.getX(), e.getY());
        if (temp == null) {
            if (currentlyAddedEdge == null) {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_EDGE_START));
            } else {
                currentlyAddedEdge = null;
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_EDGE_ADDING_CANCELLED));
            }
        } else {
            if (currentlyAddedEdge == null) {
                currentlyAddedEdge = new Edge(generalController.getGraph());
                currentlyAddedEdge.setModel(generalController.getGraph().getEdgeDefaultModel());
                currentlyAddedEdge.setVertexA(temp);
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_EDGE_END));
            } else {
                currentlyAddedEdge.setVertexB(temp);
                currentlyAddedEdge.setDirected(shiftDown);
                new CreationRemovalOperation(generalController, currentlyAddedEdge, OperationType.ADD_EDGE, StringLiterals.INFO_EDGE_ADD, true);
                currentlyAddedEdge = null;
            }
        }
    }

    @Override
    public void moveSelection(MouseEvent e) {
        if (currentlyDraggedEdge == null) {
            startMoving(e);
        } else {
            continueMoving(e);
        }
    }

    private void startMoving(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Edge edge = getElementFromPosition(e);
        currentlyDraggedEdge = edge;
        generalController.getSelectionController().addToSelection(edge, false);

        currentDragOperation = new AlterationOperation(generalController, currentlyDraggedEdge, OperationType.MOVE_EDGE, StringLiterals.INFO_EDGE_MOVE);
        if (edge.getVertexA() == edge.getVertexB()) {
            Point c = new Point(x, y);
            Point v = new Point(edge.getVertexA().getPosX(), edge.getVertexA().getPosY());
            if (c.distance(v) > (edge.getVertexA().getRadius() + edge.getVertexA().getLineWidth() / 2) * 1.5) {
                changingEdgeAngle = true;
            } else {
                edgeDragDummy.setPosX(x);
                edgeDragDummy.setPosY(y);
                edgeDragDummy.setRadius(2);
                if (c.distance(edge.getInPoint()) < c.distance(edge.getOutPoint())) {
                    edge.setVertexB(edgeDragDummy);
                } else {
                    edge.setVertexA(edgeDragDummy);
                }

            }
        } else {
            Point c = new Point(x, y);
            Point va = new Point(edge.getVertexA().getPosX(), edge.getVertexA().getPosY());
            Point vb = new Point(edge.getVertexB().getPosX(), edge.getVertexB().getPosY());
            if (c.distance(va) / c.distance(vb) < 2 && c.distance(va) / c.distance(vb) > 0.5) {
                changingEdgeAngle = true;
            } else {
                edgeDragDummy.setPosX(x);
                edgeDragDummy.setPosY(y);
                edgeDragDummy.setRadius(2);
                if (c.distance(va) < c.distance(vb)) {
                    edge.setVertexA(edgeDragDummy);
                } else {
                    edge.setVertexB(edgeDragDummy);
                }
            }
        }
    }

    private void continueMoving(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (changingEdgeAngle) {
            Edge edge = currentlyDraggedEdge;
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
                Edge edge = currentlyDraggedEdge;
                if (currentlyDraggedEdge.getVertexA() == edgeDragDummy) {
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
                currentlyDraggedEdge.setRelativeEdgeAngle(0);
            }
        }
    }

    @Override
    public void finishMovingElement() {
        if (currentlyDraggedEdge != null) {
            if (currentlyDraggedEdge.getVertexA() != edgeDragDummy && currentlyDraggedEdge.getVertexB() != edgeDragDummy) {
                currentDragOperation.finish();
            } else {
                // Restore original edge state (it was dropped in mid air)
                try {
                    generalController.getParseController().getEdgeParser().updateElementWithCode(currentlyDraggedEdge, currentlyDraggedEdge.getLatexCode());
                } catch (Exception e) {
                    e.printStackTrace();
                    generalController.criticalError("Parser error", e);
                }
            }
            changingEdgeAngle = false;
            currentlyDraggedEdge = null;
        }
    }

    public Edge getCurrentlyAddedEdge() {
        return currentlyAddedEdge;
    }
}
