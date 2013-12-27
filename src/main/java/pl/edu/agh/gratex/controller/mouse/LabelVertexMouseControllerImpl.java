package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.AlterationOperation;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.controller.operation.GenericOperation;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.labelV.LabelVUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 *
 */
public class LabelVertexMouseControllerImpl extends GraphElementMouseController {
    private GeneralController generalController;

    private LabelV currentlyMovedLabel;
    private AlterationOperation currentDragOperation;

    public LabelVertexMouseControllerImpl(GeneralController generalController) {
        super(generalController);
        this.generalController = generalController;
    }

    @Override
    public void shiftDownChanged() {
    }

    @Override
    public void reset() {
        finishMovingElement();
        currentlyMovedLabel = null;
        currentDragOperation = null;
    }

    @Override
    public void paintCurrentlyAddedElement(Graphics2D g) {
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
    }

    @Override
    public LabelV getElementFromPosition(MouseEvent e) {
        return GraphUtils.getLabelVFromPosition(generalController.getGraph(), e.getX(), e.getY());
    }

    @Override
    public void addNewElement(MouseEvent e) {
        Vertex temp = GraphUtils.getVertexFromPosition(generalController.getGraph(), e.getX(), e.getY());
        if (temp != null) {
            if (temp.getLabel() == null) {
                LabelV labelV = new LabelV(temp, generalController.getGraph());
                labelV.setModel(generalController.getGraph().getLabelVDefaultModel());
                LabelVUtils.updatePosition(labelV);
                new CreationRemovalOperation(generalController, labelV, OperationType.ADD_LABEL_VERTEX, StringLiterals.INFO_LABEL_V_ADD, true);
            } else {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CANNOT_CREATE_LABEL_V_EXISTS));
            }
        } else {
            generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_VERTEX_FOR_LABEL));
        }
    }

    @Override
    public void moveSelection(MouseEvent e) {
        if (currentlyMovedLabel == null) {
            startMoving(e);
        } else {
            continueMoving(e);
        }
    }

    private void startMoving(MouseEvent e) {
        currentlyMovedLabel = getElementFromPosition(e);
        generalController.getSelectionController().addToSelection(currentlyMovedLabel, false);
        currentDragOperation = new AlterationOperation(generalController, currentlyMovedLabel, OperationType.MOVE_LABEL_VERTEX, StringLiterals.INFO_LABEL_V_MOVE);
    }

    private void continueMoving(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Vertex vertex = currentlyMovedLabel.getOwner();
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
        currentlyMovedLabel.setPosition(((int) Math.abs(Math.ceil((angle - 22.5) / 45))) % 8);
    }

    @Override
    public void finishMovingElement() {
        if (currentlyMovedLabel != null) {
            currentDragOperation.finish();
            currentlyMovedLabel = null;
        }
    }
}
