package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.controller.operation.GenericOperation;
import pl.edu.agh.gratex.editor.DragOperation;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.view.ControlManager;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

/**
 *
 */
public class LabelEdgeMouseControllerImpl extends GraphElementMouseController {
    private LabelE currentlyMovedLabel;
    private DragOperation currentDragOperation;

    public LabelEdgeMouseControllerImpl(GeneralController generalController) {
        super(generalController);
        this.generalController = generalController;
    }

    private GeneralController generalController;

    @Override
    public void shiftDownChanged(){
        if (currentlyMovedLabel != null){
            currentlyMovedLabel.setHorizontalPlacement(shiftDown);
        }
    }

    @Override
    public void reset() {
        finishMovingElement();
        currentlyMovedLabel = null;
        currentDragOperation = null;
        shiftDown = false;
    }

    @Override
    public void paintCurrentlyAddedElement(Graphics2D g) {
        Edge temp = GraphUtils.getEdgeFromPosition(generalController.getGraph(), mouseX, mouseY);
        if (temp != null) {
            if (temp.getLabel() == null) {
                LabelE labelE = new LabelE(temp, generalController.getGraph());
                labelE.setModel(generalController.getGraph().getLabelEDefaultModel());

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

    @Override
    public LabelE getElementFromPosition(MouseEvent e) {
        return GraphUtils.getLabelEFromPosition(generalController.getGraph(), e.getX(), e.getY());
    }

    @Override
    public void addNewElement(MouseEvent e) {
        Edge temp = GraphUtils.getEdgeFromPosition(generalController.getGraph(), e.getX(), e.getY());
        if (temp != null) {
            if (temp.getLabel() == null) {
                LabelE labelE = new LabelE(temp, generalController.getGraph());
                labelE.setHorizontalPlacement(shiftDown);
                labelE.setModel(generalController.getGraph().getLabelEDefaultModel());
                new CreationRemovalOperation(generalController, labelE, OperationType.ADD_LABEL_EDGE, StringLiterals.INFO_LABEL_E_ADD, true);
            } else {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CANNOT_CREATE_LABEL_E_EXISTS));
            }
        } else {
            generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_EDGE_FOR_LABEL));
        }
    }

    @Override
    public void moveSelection(MouseEvent e) {
        if(currentlyMovedLabel == null) {
            currentlyMovedLabel = getElementFromPosition(e);
            currentDragOperation = new DragOperation(currentlyMovedLabel);
            currentDragOperation.setLabelEStartState(currentlyMovedLabel);
        } else {
           continueMoving(e);
        }
    }

    private void continueMoving(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int bias;
        Edge edge = currentlyMovedLabel.getOwner();
        LabelE labelE = currentlyMovedLabel;
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

    @Override
    public void finishMovingElement() {
        if(currentlyMovedLabel != null) {
            currentDragOperation.setLabelEEndState(currentlyMovedLabel);
            if (currentDragOperation.changeMade()) {
                ControlManager.operations.addNewOperation(currentDragOperation);
                generalController.publishInfo(ControlManager.operations.redo());
            }
        }
        currentlyMovedLabel = null;
    }
}
