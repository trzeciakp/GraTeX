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
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.edge.EdgeUtils;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.util.LinkedList;
import java.util.List;


public class BoundaryMouseController extends GraphElementMouseController {
    private Boundary currentlyAddedBoundary;
    private Boundary currentlyDraggedBoundary;
    private AlterationOperation currentDragOperation;

    public BoundaryMouseController(GeneralController generalController, GraphElementFactory graphElementFactory) {
        super(generalController, graphElementFactory);
    }

    @Override
    public void reset() {
        finishMoving();
        currentlyAddedBoundary = null;
        currentlyDraggedBoundary = null;
        currentDragOperation = null;
    }

    @Override
    public GraphElement getCurrentlyAddedElement() {
        if (currentlyAddedBoundary != null) {
            currentlyAddedBoundary.setLeftCornerX(Math.min(mouseX, currentlyAddedBoundary.getLeftCornerX()));
            currentlyAddedBoundary.setLeftCornerY(Math.min(mouseY, currentlyAddedBoundary.getLeftCornerY()));
            currentlyAddedBoundary.setWidth(Math.abs(mouseX - currentlyAddedBoundary.getLeftCornerX()));
            currentlyAddedBoundary.setHeight(Math.abs(mouseY - currentlyAddedBoundary.getLeftCornerY()));
            return currentlyAddedBoundary;
        }
        return null;
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        if (currentlyAddedBoundary == null) {
            currentlyAddedBoundary = (Boundary) getGraphElementFactory().create(GraphElementType.BOUNDARY, generalController.getGraph());
            currentlyAddedBoundary.setLeftCornerX(mouseX);
            currentlyAddedBoundary.setLeftCornerY(mouseY);
            generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_BOUNDARY_END));
        } else {
            new CreationRemovalOperation(generalController, currentlyAddedBoundary, OperationType.ADD_BOUNDARY, StringLiterals.INFO_BOUNDARY_ADD, true);
            currentlyAddedBoundary = null;
        }
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {

    }

    @Override
    public void finishMoving() {

    }
}
