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
import pl.edu.agh.gratex.view.Application;

import java.awt.*;

public class LinkBoundaryMouseController extends GraphElementMouseController {
    private LinkBoundary currentlyAddedLink;
    private LinkBoundary currentlyDraggedLink;
    private AlterationOperation currentDragOperation;
    private boolean disconnectedBoundaryA;
    private Boundary linkDragDummy;
    private String initialLatexCodeOfDraggedLink;

    public LinkBoundaryMouseController(GeneralController generalController, GraphElementFactory graphElementFactory, GraphElementType handledGraphElementType) {
        super(generalController, graphElementFactory, handledGraphElementType);
        linkDragDummy = (Boundary) getGraphElementFactory().create(GraphElementType.BOUNDARY, null);
        linkDragDummy.setWidth(1);
        linkDragDummy.setHeight(1);
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
                boundary = linkDragDummy;
                linkDragDummy.setTopLeftX(mouseX);
                linkDragDummy.setTopLeftY(mouseY);
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
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_LINK_END));
            } else {
                currentlyAddedLink.setBoundaryB(boundary);
                currentlyAddedLink.setInAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, mouseX, mouseY));
                new CreationRemovalOperation(generalController, currentlyAddedLink, OperationType.ADD_LINK_BOUNDARY, StringLiterals.INFO_LINK_BOUNDARY_ADD, true);
                currentlyAddedLink = null;
            }
        }
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {
        if (currentlyDraggedLink == null) {
            currentlyDraggedLink = (LinkBoundary) generalController.getGraph().getElementFromPosition(GraphElementType.LINK_BOUNDARY, mouseX, mouseY);
            initialLatexCodeOfDraggedLink = generalController.getParseController().getParserByElementType(GraphElementType.LINK_BOUNDARY).parseToLatex(currentlyDraggedLink);

            currentDragOperation = new AlterationOperation(generalController, currentlyDraggedLink, OperationType.MOVE_LINK_BOUNDARY, StringLiterals.INFO_LINK_BOUNDARY_MOVE);

            linkDragDummy.setTopLeftX(mouseX);
            linkDragDummy.setTopLeftY(mouseY);
            if (Point.distance(currentlyDraggedLink.getOutPointX(), currentlyDraggedLink.getOutPointY(), mouseX, mouseY) <
                    Point.distance(currentlyDraggedLink.getInPointX(), currentlyDraggedLink.getInPointY(), mouseX, mouseY)) {
                disconnectedBoundaryA = true;
                currentlyDraggedLink.setBoundaryA(linkDragDummy);
            } else {
                disconnectedBoundaryA = false;
                currentlyDraggedLink.setBoundaryB(linkDragDummy);
            }
        } else {
            generalController.getSelectionController().addToSelection(currentlyDraggedLink, false);
            Boundary boundary = (Boundary) generalController.getGraph().getElementFromPosition(GraphElementType.BOUNDARY, mouseX, mouseY);
            if (boundary != null) {
                if (disconnectedBoundaryA && currentlyDraggedLink.getBoundaryB() != boundary) {
                    currentlyDraggedLink.setBoundaryA(boundary);
                    currentlyDraggedLink.setOutAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, mouseX, mouseY));
                } else if (!disconnectedBoundaryA && currentlyDraggedLink.getBoundaryA() != boundary) {
                    currentlyDraggedLink.setBoundaryB(boundary);
                    currentlyDraggedLink.setInAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, mouseX, mouseY));
                }
            } else {
                linkDragDummy.setTopLeftX(mouseX);
                linkDragDummy.setTopLeftY(mouseY);
                if (disconnectedBoundaryA) {
                    currentlyDraggedLink.setBoundaryA(linkDragDummy);
                } else {
                    currentlyDraggedLink.setBoundaryB(linkDragDummy);
                }
            }
        }
    }

    @Override
    public void finishMoving() {
        if (currentlyDraggedLink != null) {
            if (currentlyDraggedLink.getBoundaryA() != linkDragDummy && currentlyDraggedLink.getBoundaryB() != linkDragDummy) {
                currentDragOperation.finish();
            } else {
                // Restore original edge state (it was dropped in mid air)
                try {
                    generalController.getParseController().getParserByElementType(GraphElementType.LINK_BOUNDARY).
                            updateElementWithCode(currentlyDraggedLink, initialLatexCodeOfDraggedLink);
                } catch (Exception e) {
                    e.printStackTrace();
                    Application.criticalError("Parser error", e);
                }
            }
            currentlyDraggedLink = null;
        }
    }
}
