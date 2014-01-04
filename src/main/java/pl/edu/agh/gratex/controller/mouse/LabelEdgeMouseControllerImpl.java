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
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelE.LabelEUtils;

import java.awt.*;
import java.util.*;
import java.util.List;


public class LabelEdgeMouseControllerImpl extends GraphElementMouseController {
    private GeneralController generalController;

    private LabelE currentlyDraggedLabel;
    private AlterationOperation currentDragOperation;
    private boolean shiftChangedWhileAdding;

    public LabelEdgeMouseControllerImpl(GeneralController generalController, GraphElementFactory graphElementFactory) {
        super(generalController, graphElementFactory);
        this.generalController = generalController;
    }


    @Override
    public void shiftDownChanged() {
        shiftChangedWhileAdding = true;
        if (currentlyDraggedLabel != null) {
            currentlyDraggedLabel.setHorizontalPlacement(shiftDown);
        }
    }

    @Override
    public void reset() {
        finishMoving();
        currentlyDraggedLabel = null;
        currentDragOperation = null;
        shiftDown = false;
        shiftChangedWhileAdding = false;
    }

    @Override
    public List<GraphElement> getCurrentlyAddedElements() {
        List<GraphElement> result = new LinkedList<>();
        Edge owner = GraphUtils.getEdgeFromPosition(generalController.getGraph(), mouseX, mouseY);
        if (owner != null) {
            if (owner.getLabel() == null) {
                LabelE labelE = (LabelE) getGraphElementFactory().create(GraphElementType.LABEL_EDGE, generalController.getGraph());
                labelE.setOwner(owner);
                if (shiftChangedWhileAdding) {
                    labelE.setHorizontalPlacement(shiftDown);
                }
                int position = LabelEUtils.getPositionFromCursorLocation(owner, mouseX, mouseY);
                labelE.setPosition(Math.abs(position));
                labelE.setTopPlacement(position >= 0);
                result.add(labelE);
            }
        }
        return result;
    }

    @Override
    public LabelE getElementFromPosition(int mouseX, int mouseY) {
        return GraphUtils.getLabelEFromPosition(generalController.getGraph(), mouseX, mouseY);
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        Edge owner = GraphUtils.getEdgeFromPosition(generalController.getGraph(), mouseX, mouseY);
        if (owner != null) {
            if (owner.getLabel() == null) {
                LabelE labelE = (LabelE) getGraphElementFactory().create(GraphElementType.LABEL_EDGE, generalController.getGraph());
                labelE.setOwner(owner);
                if (shiftChangedWhileAdding) {
                    labelE.setHorizontalPlacement(shiftDown);
                }
                int position = LabelEUtils.getPositionFromCursorLocation(owner, mouseX, mouseY);
                labelE.setPosition(Math.abs(position));
                labelE.setTopPlacement(position >= 0);
                labelE.updateLocation();
                new CreationRemovalOperation(generalController, labelE, OperationType.ADD_LABEL_EDGE, StringLiterals.INFO_LABEL_E_ADD, true);
            } else {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CANNOT_CREATE_LABEL_E_EXISTS));
            }
        } else {
            generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_EDGE_FOR_LABEL));
        }
        shiftChangedWhileAdding = false;
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {
        if (currentlyDraggedLabel == null) {
            currentlyDraggedLabel = getElementFromPosition(mouseX, mouseY);
            currentDragOperation = new AlterationOperation(generalController, currentlyDraggedLabel, OperationType.MOVE_LABEL_EDGE, StringLiterals.INFO_LABEL_E_MOVE);
        } else {
            generalController.getSelectionController().addToSelection(currentlyDraggedLabel, false);
            Edge owner = currentlyDraggedLabel.getOwner();
            if (shiftChangedWhileAdding) {
                currentlyDraggedLabel.setHorizontalPlacement(shiftDown);
            }
            int position = LabelEUtils.getPositionFromCursorLocation(owner, mouseX, mouseY);
            currentlyDraggedLabel.setPosition(Math.abs(position));
            currentlyDraggedLabel.setTopPlacement(position >= 0);
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
