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
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundaryUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;

public class LinkBoundaryMouseController extends GraphElementMouseController {
    private LinkBoundary currentlyAddedLink;
    private LinkBoundary currentlyDraggedLink;
    private AlterationOperation currentDragOperation;
    private boolean disconnectedBoundaryA;
    private Boundary boundaryDragDummy;
    private String initialLatexCodeOfDraggedLink;

    public LinkBoundaryMouseController(GeneralController generalController, GraphElementFactory graphElementFactory, GraphElementType handledGraphElementType) {
        super(generalController, graphElementFactory, handledGraphElementType);
        boundaryDragDummy = (Boundary) getGraphElementFactory().create(GraphElementType.BOUNDARY, null);
        boundaryDragDummy.setWidth(2);
        boundaryDragDummy.setHeight(2);
    }

    @Override
    public void shiftDownChanged() {
        if (currentlyAddedLink != null) {
            currentlyAddedLink.setDirected(shiftDown);
        }
        if (currentlyDraggedLink != null) {
            currentlyDraggedLink.setDirected(shiftDown);
        }
    }

    @Override
    public void reset() {
        finishMoving();
        currentlyDraggedLink = null;
        currentDragOperation = null;
        currentlyAddedLink = null;
    }

    @Override
    public GraphElement getCurrentlyAddedElement() {
        if (currentlyAddedLink != null) {
            Boundary boundary = (Boundary) generalController.getGraph().getElementFromPosition(GraphElementType.BOUNDARY, mouseX, mouseY);
            if (boundary == null) {
                boundary = boundaryDragDummy;
                boundaryDragDummy.setTopLeftX(mouseX - 1);
                boundaryDragDummy.setTopLeftY(mouseY - 1);
                currentlyAddedLink.setInAngle((currentlyAddedLink.getOutAngle() + 180) % 360);
            } else {
                currentlyAddedLink.setInAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, mouseX, mouseY));
            }
            if (currentlyAddedLink.getBoundaryA() != boundary) {
                currentlyAddedLink.setBoundaryB(boundary);
            }
            return currentlyAddedLink;
        } else { // currentlyAddedLink == null
            Boundary boundary = (Boundary) generalController.getGraph().getElementFromPosition(GraphElementType.BOUNDARY, mouseX, mouseY);
            if (boundary != null) {
                LinkBoundary dummy = (LinkBoundary) getGraphElementFactory().create(GraphElementType.LINK_BOUNDARY, generalController.getGraph());
                dummy.setBoundaryA(boundary);
                dummy.setOutAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, mouseX, mouseY));
                return dummy;
            }
        }
        return null;
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        Boundary boundary = (Boundary) generalController.getGraph().getElementFromPosition(GraphElementType.BOUNDARY, mouseX, mouseY);
        if (boundary == null) {
            if (currentlyAddedLink == null) {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_LINK_START));
            } else {
                currentlyAddedLink = null;
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_LINK_ADDING_CANCELLED));
            }
        } else {
            if (currentlyAddedLink == null) {
                currentlyAddedLink = (LinkBoundary) getGraphElementFactory().create(GraphElementType.LINK_BOUNDARY, generalController.getGraph());
                currentlyAddedLink.setBoundaryA(boundary);
                currentlyAddedLink.setOutAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, mouseX, mouseY));
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_EDGE_END));
            } else {
                currentlyAddedLink.setBoundaryB(boundary);
                currentlyAddedLink.setInAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, mouseX, mouseY));
                new CreationRemovalOperation(generalController, currentlyAddedLink, OperationType.ADD_EDGE, StringLiterals.INFO_EDGE_ADD, true);
                currentlyAddedLink = null;
            }
        }
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void finishMoving() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
