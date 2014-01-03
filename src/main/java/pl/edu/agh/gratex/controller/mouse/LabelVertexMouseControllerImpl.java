package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.AlterationOperation;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.controller.operation.GenericOperation;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.labelV.LabelVUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.awt.*;


public class LabelVertexMouseControllerImpl extends GraphElementMouseController {
    private GeneralController generalController;

    private LabelV currentlyDraggedLabel;
    private AlterationOperation currentDragOperation;

    public LabelVertexMouseControllerImpl(GeneralController generalController, GraphElementFactory graphElementFactory) {
        super(generalController, graphElementFactory);
        this.generalController = generalController;
    }

    @Override
    public void reset() {
        finishMoving();
        currentlyDraggedLabel = null;
        currentDragOperation = null;
    }

    @Override
    public void drawCurrentlyAddedElement(Graphics2D g) {
        Vertex vertex = GraphUtils.getVertexFromPosition(generalController.getGraph(), mouseX, mouseY);
        if (vertex != null) {
            if (vertex.getLabel() == null) {
                LabelV labelV = (LabelV) getGraphElementFactory().create(GraphElementType.LABEL_VERTEX, generalController.getGraph());
                labelV.setOwner(vertex);
                labelV.setPosition(LabelVUtils.getPositionFromCursorLocation(vertex, mouseX, mouseY));
                labelV.draw(g, true);
            }
        }
    }

    @Override
    public LabelV getElementFromPosition(int mouseX, int mouseY) {
        return GraphUtils.getLabelVFromPosition(generalController.getGraph(), mouseX, mouseY);
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        Vertex owner = GraphUtils.getVertexFromPosition(generalController.getGraph(), mouseX, mouseY);
        if (owner != null) {
            if (owner.getLabel() == null) {
                LabelV labelV = (LabelV) getGraphElementFactory().create(GraphElementType.LABEL_VERTEX, generalController.getGraph());
                labelV.setOwner(owner);
                labelV.setPosition(LabelVUtils.getPositionFromCursorLocation(owner, mouseX, mouseY));
                labelV.updateLocation();
                new CreationRemovalOperation(generalController, labelV, OperationType.ADD_LABEL_VERTEX, StringLiterals.INFO_LABEL_V_ADD, true);
            } else {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CANNOT_CREATE_LABEL_V_EXISTS));
            }
        } else {
            generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_VERTEX_FOR_LABEL));
        }
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {
        if (currentlyDraggedLabel == null) {
            currentlyDraggedLabel = getElementFromPosition(mouseX, mouseY);
            generalController.getSelectionController().addToSelection(currentlyDraggedLabel, false);
            currentDragOperation = new AlterationOperation(generalController, currentlyDraggedLabel, OperationType.MOVE_LABEL_VERTEX, StringLiterals.INFO_LABEL_V_MOVE);
        } else {
            Vertex vertex = currentlyDraggedLabel.getOwner();
            currentlyDraggedLabel.setPosition(LabelVUtils.getPositionFromCursorLocation(vertex, mouseX, mouseY));
        }
    }

    @Override
    public void finishMoving() {
        if (currentlyDraggedLabel != null) {
            currentDragOperation.finish();
            currentlyDraggedLabel = null;
        }
    }
}
