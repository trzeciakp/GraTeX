package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.Const;
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
import pl.edu.agh.gratex.model.boundary.BoundaryUtils;

import java.awt.*;
import java.util.*;
import java.util.List;


public class BoundaryMouseController extends GraphElementMouseController {
    private enum DraggedCorner {
        NONE, TOPLEFT, TOPRIGHT, BOTTOMRIGHT, BOTTOMLEFT
    }

    private Boundary currentlyAddedBoundary;
    private Boundary currentlyDraggedBoundary;
    private AlterationOperation currentDragOperation;

    private int startPointX;
    private int startPointY;
    private int refCornerX;
    private int refCornerY;
    private DraggedCorner draggedCorner = DraggedCorner.NONE;

    public BoundaryMouseController(GeneralController generalController, GraphElementFactory graphElementFactory, GraphElementType handledGraphElementType) {
        super(generalController, graphElementFactory, handledGraphElementType);
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
            currentlyAddedBoundary.setTopLeftX(Math.min(mouseX, startPointX));
            currentlyAddedBoundary.setTopLeftY(Math.min(mouseY, startPointY));
            currentlyAddedBoundary.setWidth(Math.abs(mouseX - startPointX));
            currentlyAddedBoundary.setHeight(Math.abs(mouseY - startPointY));
            return currentlyAddedBoundary;
        }
        return null;
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        if (currentlyAddedBoundary == null) {
            currentlyAddedBoundary = (Boundary) getGraphElementFactory().create(GraphElementType.BOUNDARY, generalController.getGraph());
            currentlyAddedBoundary.setTopLeftX(mouseX);
            currentlyAddedBoundary.setTopLeftY(mouseY);
            if (generalController.getGraph().isGridOn()) {
                BoundaryUtils.adjustToGrid(currentlyAddedBoundary);
            }
            startPointX = currentlyAddedBoundary.getTopLeftX();
            startPointY = currentlyAddedBoundary.getTopLeftY();
            generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_BOUNDARY_END));
        } else {
            currentlyAddedBoundary.setTopLeftX(Math.min(mouseX, startPointX));
            currentlyAddedBoundary.setTopLeftY(Math.min(mouseY, startPointY));
            currentlyAddedBoundary.setWidth(Math.abs(mouseX - startPointX));
            currentlyAddedBoundary.setHeight(Math.abs(mouseY - startPointY));
            if (generalController.getGraph().isGridOn()) {
                BoundaryUtils.adjustToGrid(currentlyAddedBoundary);
            }
            if (currentlyAddedBoundary.getWidth() * currentlyAddedBoundary.getHeight() > 0) {
                new CreationRemovalOperation(generalController, currentlyAddedBoundary, OperationType.ADD_BOUNDARY, StringLiterals.INFO_BOUNDARY_ADD, true);
            }
            currentlyAddedBoundary = null;
        }
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {
        if (currentlyDraggedBoundary == null) {
            startMoving();
        } else {
            continueMoving();
        }
    }

    private void startMoving() {
        currentlyDraggedBoundary = (Boundary) generalController.getGraph().getElementFromPosition(GraphElementType.BOUNDARY, mouseX, mouseY);
        currentDragOperation = new AlterationOperation(generalController, currentlyDraggedBoundary, OperationType.MOVE_BOUNDARY, StringLiterals.INFO_BOUNDARY_MOVE);

        int topLeftX = currentlyDraggedBoundary.getTopLeftX();
        int topLeftY = currentlyDraggedBoundary.getTopLeftY();
        int width = currentlyDraggedBoundary.getWidth();
        int height = currentlyDraggedBoundary.getHeight();
        int cornerWidth = (int) (width * Const.BOUNDARY_CORNER_LENGTH_FACTOR);
        int cornerHeight = (int) (height * Const.BOUNDARY_CORNER_LENGTH_FACTOR);

        if (new Rectangle(topLeftX, topLeftY, cornerWidth, cornerHeight).contains(mouseX, mouseY)) {
            draggedCorner = DraggedCorner.TOPLEFT;
            refCornerX = topLeftX + width;
            refCornerY = topLeftY + height;
            startPointX = mouseX - topLeftX;
            startPointY = mouseY - topLeftY;
        } else if (new Rectangle(topLeftX + width - cornerWidth, topLeftY, cornerWidth, cornerHeight).contains(mouseX, mouseY)) {
            draggedCorner = DraggedCorner.TOPRIGHT;
            refCornerX = topLeftX;
            refCornerY = topLeftY + height;
            startPointX = mouseX - topLeftX - width;
            startPointY = mouseY - topLeftY;
        } else if (new Rectangle(topLeftX + width - cornerWidth, topLeftY + height - cornerHeight, cornerWidth, cornerHeight).contains(mouseX, mouseY)) {
            draggedCorner = DraggedCorner.BOTTOMRIGHT;
            refCornerX = topLeftX;
            refCornerY = topLeftY;
            startPointX = mouseX - topLeftX - width;
            startPointY = mouseY - topLeftY - height;
        } else if (new Rectangle(topLeftX, topLeftY + height - cornerHeight, cornerWidth, cornerHeight).contains(mouseX, mouseY)) {
            draggedCorner = DraggedCorner.BOTTOMLEFT;
            refCornerX = topLeftX + width;
            refCornerY = topLeftY;
            startPointX = mouseX - topLeftX;
            startPointY = mouseY - topLeftY - height;
        } else {
            draggedCorner = DraggedCorner.NONE;
            refCornerX = topLeftX;
            refCornerY = topLeftY;
            startPointX = mouseX;
            startPointY = mouseY;
        }
    }

    private void continueMoving() {
        generalController.getSelectionController().addToSelection(currentlyDraggedBoundary, false);
        int oldTopLeftCornerX = currentlyDraggedBoundary.getTopLeftX();
        int oldTopLeftCornerY = currentlyDraggedBoundary.getTopLeftY();
        int oldWidth = currentlyDraggedBoundary.getWidth();
        int oldHeight = currentlyDraggedBoundary.getHeight();
        switch (draggedCorner) {
            case NONE: {
                currentlyDraggedBoundary.setTopLeftX(refCornerX + mouseX - startPointX);
                currentlyDraggedBoundary.setTopLeftY(refCornerY + mouseY - startPointY);
                if (!BoundaryUtils.fitsIntoPage(currentlyDraggedBoundary)) {
                    currentlyDraggedBoundary.setTopLeftX(oldTopLeftCornerX);
                    currentlyDraggedBoundary.setTopLeftY(oldTopLeftCornerY);
                    if (currentlyDraggedBoundary.contains(mouseX, mouseY)) {
                        startPointX = mouseX;
                        startPointY = mouseY;
                        refCornerX = currentlyDraggedBoundary.getTopLeftX();
                        refCornerY = currentlyDraggedBoundary.getTopLeftY();
                    }
                }
                break;
            }

            case TOPLEFT: {
                int topLeftX = mouseX - startPointX;
                int topLeftY = mouseY - startPointY;
                currentlyDraggedBoundary.setTopLeftX(topLeftX);
                currentlyDraggedBoundary.setTopLeftY(topLeftY);
                currentlyDraggedBoundary.setWidth(refCornerX - topLeftX);
                currentlyDraggedBoundary.setHeight(refCornerY - topLeftY);
                if (currentlyDraggedBoundary.getWidth() < 0) {
                    currentlyDraggedBoundary.setTopLeftX(currentlyDraggedBoundary.getTopLeftX() + currentlyDraggedBoundary.getWidth());
                    currentlyDraggedBoundary.setWidth(-currentlyDraggedBoundary.getWidth());
                }
                if (currentlyDraggedBoundary.getHeight() < 0) {
                    currentlyDraggedBoundary.setTopLeftY(currentlyDraggedBoundary.getTopLeftY() + currentlyDraggedBoundary.getHeight());
                    currentlyDraggedBoundary.setHeight(-currentlyDraggedBoundary.getHeight());
                }
                if (!BoundaryUtils.fitsIntoPage(currentlyDraggedBoundary)) {
                    currentlyDraggedBoundary.setTopLeftX(oldTopLeftCornerX);
                    currentlyDraggedBoundary.setTopLeftY(oldTopLeftCornerY);
                    currentlyDraggedBoundary.setWidth(oldWidth);
                    currentlyDraggedBoundary.setHeight(oldHeight);
                    if (currentlyDraggedBoundary.contains(mouseX, mouseY)) {
                        startPointX = mouseX - currentlyDraggedBoundary.getTopLeftX();
                        startPointY = mouseY - currentlyDraggedBoundary.getTopLeftY();
                    }
                }
                break;
            }

            case TOPRIGHT: {
                int topRightX = mouseX - startPointX;
                int topRightY = mouseY - startPointY;
                currentlyDraggedBoundary.setTopLeftY(topRightY);
                currentlyDraggedBoundary.setWidth(topRightX - refCornerX);
                currentlyDraggedBoundary.setHeight(refCornerY - topRightY);
                if (currentlyDraggedBoundary.getWidth() < 0) {
                    currentlyDraggedBoundary.setTopLeftX(refCornerX + currentlyDraggedBoundary.getWidth());
                    currentlyDraggedBoundary.setWidth(-currentlyDraggedBoundary.getWidth());
                }
                if (currentlyDraggedBoundary.getHeight() < 0) {
                    currentlyDraggedBoundary.setTopLeftY(currentlyDraggedBoundary.getTopLeftY() + currentlyDraggedBoundary.getHeight());
                    currentlyDraggedBoundary.setHeight(-currentlyDraggedBoundary.getHeight());
                }
                if (!BoundaryUtils.fitsIntoPage(currentlyDraggedBoundary)) {
                    currentlyDraggedBoundary.setTopLeftX(oldTopLeftCornerX);
                    currentlyDraggedBoundary.setTopLeftY(oldTopLeftCornerY);
                    currentlyDraggedBoundary.setWidth(oldWidth);
                    currentlyDraggedBoundary.setHeight(oldHeight);
                    if (currentlyDraggedBoundary.contains(mouseX, mouseY)) {
                        startPointX = mouseX - currentlyDraggedBoundary.getTopLeftX() - currentlyDraggedBoundary.getWidth();
                        startPointY = mouseY - currentlyDraggedBoundary.getTopLeftY();
                    }
                }
                break;
            }

            case BOTTOMRIGHT: {
                int bottomRightX = mouseX - startPointX;
                int bottomRightY = mouseY - startPointY;
                currentlyDraggedBoundary.setWidth(bottomRightX - refCornerX);
                currentlyDraggedBoundary.setHeight(bottomRightY - refCornerY);
                if (currentlyDraggedBoundary.getWidth() < 0) {
                    currentlyDraggedBoundary.setTopLeftX(refCornerX + currentlyDraggedBoundary.getWidth());
                    currentlyDraggedBoundary.setWidth(-currentlyDraggedBoundary.getWidth());
                }
                if (currentlyDraggedBoundary.getHeight() < 0) {
                    currentlyDraggedBoundary.setTopLeftY(refCornerY + currentlyDraggedBoundary.getHeight());
                    currentlyDraggedBoundary.setHeight(-currentlyDraggedBoundary.getHeight());
                }
                if (!BoundaryUtils.fitsIntoPage(currentlyDraggedBoundary)) {
                    currentlyDraggedBoundary.setTopLeftX(oldTopLeftCornerX);
                    currentlyDraggedBoundary.setTopLeftY(oldTopLeftCornerY);
                    currentlyDraggedBoundary.setWidth(oldWidth);
                    currentlyDraggedBoundary.setHeight(oldHeight);
                    if (currentlyDraggedBoundary.contains(mouseX, mouseY)) {
                        startPointX = mouseX - currentlyDraggedBoundary.getTopLeftX() - currentlyDraggedBoundary.getWidth();
                        startPointY = mouseY - currentlyDraggedBoundary.getTopLeftY() - currentlyDraggedBoundary.getHeight();
                    }
                }
                break;
            }

            case BOTTOMLEFT: {
                int bottomLeftX = mouseX - startPointX;
                int bottomLeftY = mouseY - startPointY;
                currentlyDraggedBoundary.setTopLeftX(bottomLeftX);
                currentlyDraggedBoundary.setWidth(refCornerX - bottomLeftX);
                currentlyDraggedBoundary.setHeight(bottomLeftY - refCornerY);
                if (currentlyDraggedBoundary.getWidth() < 0) {
                    currentlyDraggedBoundary.setTopLeftX(currentlyDraggedBoundary.getTopLeftX() + currentlyDraggedBoundary.getWidth());
                    currentlyDraggedBoundary.setWidth(-currentlyDraggedBoundary.getWidth());
                }
                if (currentlyDraggedBoundary.getHeight() < 0) {
                    currentlyDraggedBoundary.setTopLeftY(refCornerY + currentlyDraggedBoundary.getHeight());
                    currentlyDraggedBoundary.setHeight(-currentlyDraggedBoundary.getHeight());
                }
                if (!BoundaryUtils.fitsIntoPage(currentlyDraggedBoundary)) {
                    currentlyDraggedBoundary.setTopLeftX(oldTopLeftCornerX);
                    currentlyDraggedBoundary.setTopLeftY(oldTopLeftCornerY);
                    currentlyDraggedBoundary.setWidth(oldWidth);
                    currentlyDraggedBoundary.setHeight(oldHeight);
                    if (currentlyDraggedBoundary.contains(mouseX, mouseY)) {
                        startPointX = mouseX - currentlyDraggedBoundary.getTopLeftX();
                        startPointY = mouseY - currentlyDraggedBoundary.getTopLeftY() - currentlyDraggedBoundary.getHeight();
                    }
                }
                break;
            }
        }

        if (generalController.getGraph().isGridOn()) {
            BoundaryUtils.adjustToGrid(currentlyDraggedBoundary);
        }
    }

    @Override
    public void finishMoving() {
        if (currentlyDraggedBoundary != null) {
            currentDragOperation.finish();
            currentlyDraggedBoundary = null;
        }
    }
}
