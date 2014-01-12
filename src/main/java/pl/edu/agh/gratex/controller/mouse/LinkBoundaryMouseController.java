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
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundaryUtils;
import pl.edu.agh.gratex.view.Application;

import java.awt.*;

public class LinkBoundaryMouseController extends GraphElementMouseController {
    private LinkBoundary currentlyAddedLink;
    private LinkBoundary currentlyDraggedLink;
    private AlterationOperation currentDragOperation;
    private boolean disconnectedBoundaryA;
    private Boundary linkDragDummy;
    private String initialLatexCodeOfDraggedLink;

    // Used to straighten links with Ctrl
    private int dummyMouseX;
    private int dummyMouseY;

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
    public void ctrlDownChanged() {
        checkForCtrl();
        generalController.getOperationController().reportOperationEvent(null);
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
        checkForCtrl();
        if (currentlyAddedLink != null) {
            Boundary boundary = (Boundary) generalController.getGraph().getElementFromPosition(GraphElementType.BOUNDARY, dummyMouseX, dummyMouseY);
            if (boundary == null) {
                boundary = linkDragDummy;
                linkDragDummy.setTopLeftX(dummyMouseX);
                linkDragDummy.setTopLeftY(dummyMouseY);
            } else {
                if (ctrlDown) {
                    currentlyAddedLink.setInAngle(LinkBoundaryUtils.getAngleFromLineIntersection(boundary,
                            currentlyAddedLink.getOutPointX(), currentlyAddedLink.getOutPointY(), dummyMouseX, dummyMouseY));
                } else {
                    currentlyAddedLink.setInAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, dummyMouseX, dummyMouseY));
                }
            }
            if (currentlyAddedLink.getBoundaryA() != boundary) {
                currentlyAddedLink.setBoundaryB(boundary);
            }
            return currentlyAddedLink;
        } else { // currentlyAddedLink == null
            Boundary boundary = (Boundary) generalController.getGraph().getElementFromPosition(GraphElementType.BOUNDARY, dummyMouseX, dummyMouseY);
            if (boundary != null) {
                LinkBoundary dummy = (LinkBoundary) getGraphElementFactory().create(GraphElementType.LINK_BOUNDARY, generalController.getGraph());
                dummy.setBoundaryA(boundary);
                dummy.setOutAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, dummyMouseX, dummyMouseY));
                return dummy;
            }
        }
        return null;
    }

    @Override
    public void addNewElement(int moudeX, int mouseY) {
        checkForCtrl();
        Boundary boundary = (Boundary) generalController.getGraph().getElementFromPosition(GraphElementType.BOUNDARY, dummyMouseX, dummyMouseY);
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
                currentlyAddedLink.setOutAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, dummyMouseX, dummyMouseY));
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_LINK_END));
            } else {
                if (currentlyAddedLink.getBoundaryA() != boundary) {
                    currentlyAddedLink.setBoundaryB(boundary);
                    if (ctrlDown) {
                        currentlyAddedLink.setInAngle(LinkBoundaryUtils.getAngleFromLineIntersection(boundary,
                                currentlyAddedLink.getOutPointX(), currentlyAddedLink.getOutPointY(), dummyMouseX, dummyMouseY));
                    } else {
                        currentlyAddedLink.setInAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, dummyMouseX, dummyMouseY));
                    }
                    new CreationRemovalOperation(generalController, currentlyAddedLink, OperationType.ADD_LINK_BOUNDARY, StringLiterals.INFO_LINK_BOUNDARY_ADD, true);
                    currentlyAddedLink = null;
                } else {
                    currentlyAddedLink = null;
                    generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_LINK_ADDING_CANCELLED));
                }
            }
        }
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {
        checkForCtrl();
        if (currentlyDraggedLink == null) {
            currentlyDraggedLink = (LinkBoundary) generalController.getGraph().getElementFromPosition(GraphElementType.LINK_BOUNDARY, dummyMouseX, dummyMouseY);
            initialLatexCodeOfDraggedLink = generalController.getParseController().getParserByElementType(GraphElementType.LINK_BOUNDARY).parseToLatex(currentlyDraggedLink);

            currentDragOperation = new AlterationOperation(generalController, currentlyDraggedLink, OperationType.MOVE_LINK_BOUNDARY, StringLiterals.INFO_LINK_BOUNDARY_MOVE);

            linkDragDummy.setTopLeftX(dummyMouseX);
            linkDragDummy.setTopLeftY(dummyMouseY);
            if (Point.distance(currentlyDraggedLink.getOutPointX(), currentlyDraggedLink.getOutPointY(), dummyMouseX, dummyMouseY) <
                    Point.distance(currentlyDraggedLink.getInPointX(), currentlyDraggedLink.getInPointY(), dummyMouseX, dummyMouseY)) {
                disconnectedBoundaryA = true;
                currentlyDraggedLink.setBoundaryA(linkDragDummy);
            } else {
                disconnectedBoundaryA = false;
                currentlyDraggedLink.setBoundaryB(linkDragDummy);
            }
        } else {
            generalController.getSelectionController().addToSelection(currentlyDraggedLink, false);
            Boundary boundary = (Boundary) generalController.getGraph().getElementFromPosition(GraphElementType.BOUNDARY, dummyMouseX, dummyMouseY);
            if (boundary != null) {
                if (disconnectedBoundaryA && currentlyDraggedLink.getBoundaryB() != boundary) {
                    currentlyDraggedLink.setBoundaryA(boundary);
                    if (ctrlDown) {
                        currentlyDraggedLink.setOutAngle(LinkBoundaryUtils.getAngleFromLineIntersection(boundary,
                                currentlyDraggedLink.getOutPointX(), currentlyDraggedLink.getOutPointY(), dummyMouseX, dummyMouseY));
                    } else {
                        currentlyDraggedLink.setOutAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, dummyMouseX, dummyMouseY));
                    }
                } else if (!disconnectedBoundaryA && currentlyDraggedLink.getBoundaryA() != boundary) {
                    currentlyDraggedLink.setBoundaryB(boundary);
                    if (ctrlDown) {
                        currentlyDraggedLink.setInAngle(LinkBoundaryUtils.getAngleFromLineIntersection(boundary,
                                currentlyDraggedLink.getOutPointX(), currentlyDraggedLink.getOutPointY(), dummyMouseX, dummyMouseY));
                    } else {
                        currentlyDraggedLink.setInAngle(LinkBoundaryUtils.getAngleFromCursorLocation(boundary, dummyMouseX, dummyMouseY));
                    }
                }
            } else {
                linkDragDummy.setTopLeftX(dummyMouseX);
                linkDragDummy.setTopLeftY(dummyMouseY);
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

    private void checkForCtrl() {
        if (!ctrlDown) {
            dummyMouseX = mouseX;
            dummyMouseY = mouseY;
        } else {
            Boundary boundary = (Boundary) generalController.getGraph().getElementFromPosition(GraphElementType.BOUNDARY, dummyMouseX, dummyMouseY);
            if (currentlyAddedLink != null) {
                straightenMousePosRelativeTo(currentlyAddedLink.getOutPointX(), currentlyAddedLink.getOutPointY());
            } else if (currentlyDraggedLink != null) {
                if (disconnectedBoundaryA) {
                    straightenMousePosRelativeTo(currentlyDraggedLink.getInPointX(), currentlyDraggedLink.getInPointY());
                } else {
                    straightenMousePosRelativeTo(currentlyDraggedLink.getOutPointX(), currentlyDraggedLink.getOutPointY());
                }
            } else if (boundary != null) {
                straightenMousePosRelativeTo(boundary.getTopLeftX() + boundary.getWidth() / 2, boundary.getTopLeftY() + boundary.getHeight() / 2);
            } else {
                dummyMouseX = mouseX;
                dummyMouseY = mouseY;
            }
        }
    }

    private void straightenMousePosRelativeTo(int x, int y) {
        int d = Math.max(Math.abs(mouseX - x), Math.abs(mouseY - y));
        int diagonalX = x + (int) (d * Math.signum(mouseX - x));
        int diagonalY = y + (int) (d * Math.signum(mouseY - y));

        double vertDist = Point.distance(mouseX, mouseY, x, diagonalY);
        double horizDist = Point.distance(mouseX, mouseY, diagonalX, y);
        double diagDist = Point.distance(mouseX, mouseY, diagonalX, diagonalY);

        double minDist = Math.min(Math.min(vertDist, horizDist), diagDist);

        if (minDist == vertDist) {
            dummyMouseX = x;
            dummyMouseY = diagonalY;
        } else if (minDist == horizDist) {
            dummyMouseX = diagonalX;
            dummyMouseY = y;
        } else {
            dummyMouseX = diagonalX;
            dummyMouseY = diagonalY;
        }
    }
}
