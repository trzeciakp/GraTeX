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
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.edge.EdgeUtils;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
import pl.edu.agh.gratex.view.Application;

import java.awt.*;
import java.util.LinkedList;
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
    private int initialTopLeftCornerX;
    private int initialTopLeftCornerY;
    private DraggedCorner draggedCorner = DraggedCorner.NONE;

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
            currentlyAddedBoundary.setLeftCornerX(Math.min(mouseX, startPointX));
            currentlyAddedBoundary.setLeftCornerY(Math.min(mouseY, startPointY));
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
            // TODO To jest tutaj do testow
            currentlyAddedBoundary.setDrawer(getGraphElementFactory().getDrawerFactory().createEditedBoundaryDrawable());
            currentlyAddedBoundary.setLeftCornerX(mouseX);
            currentlyAddedBoundary.setLeftCornerY(mouseY);
            startPointX = mouseX;
            startPointY = mouseY;
            generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_BOUNDARY_END));
        } else {
            currentlyAddedBoundary.setLeftCornerX(Math.min(mouseX, startPointX));
            currentlyAddedBoundary.setLeftCornerY(Math.min(mouseY, startPointY));
            currentlyAddedBoundary.setWidth(Math.abs(mouseX - startPointX));
            currentlyAddedBoundary.setHeight(Math.abs(mouseY - startPointY));
            new CreationRemovalOperation(generalController, currentlyAddedBoundary, OperationType.ADD_BOUNDARY, StringLiterals.INFO_BOUNDARY_ADD, true);
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

        int topLeftX = currentlyDraggedBoundary.getLeftCornerX();
        int topLeftY = currentlyDraggedBoundary.getLeftCornerY();
        int width = currentlyDraggedBoundary.getWidth();
        int height = currentlyDraggedBoundary.getHeight();
        int cornerWidth = (int) (width * Const.BOUNDARY_CORNER_LENGTH_FACTOR);
        int cornerHeight = (int) (height * Const.BOUNDARY_CORNER_LENGTH_FACTOR);

        if (new Rectangle(topLeftX, topLeftY, cornerWidth, cornerHeight).contains(mouseX, mouseY)) {
            draggedCorner = DraggedCorner.TOPLEFT;
            startPointX = topLeftX + width;
            startPointY = topLeftY + height;
        } else if (new Rectangle(topLeftX + width - cornerWidth, topLeftY, cornerWidth, cornerHeight).contains(mouseX, mouseY)) {
            draggedCorner = DraggedCorner.TOPRIGHT;
            startPointX = topLeftX;
            startPointY = topLeftY + height;
        } else if (new Rectangle(topLeftX + width - cornerWidth, topLeftY + height - cornerHeight, cornerWidth, cornerHeight).contains(mouseX, mouseY)) {
            draggedCorner = DraggedCorner.BOTTOMRIGHT;
            startPointX = topLeftX;
            startPointY = topLeftY;
        } else if (new Rectangle(topLeftX, topLeftY + height - cornerHeight, cornerWidth, cornerHeight).contains(mouseX, mouseY)) {
            draggedCorner = DraggedCorner.BOTTOMRIGHT;
            startPointX = topLeftX + width;
            startPointY = topLeftY;
        } else {
            draggedCorner = DraggedCorner.NONE;
            startPointX = mouseX;
            startPointY = mouseY;
            initialTopLeftCornerX = currentlyDraggedBoundary.getLeftCornerX();
            initialTopLeftCornerY = currentlyDraggedBoundary.getLeftCornerY();
        }
    }

    private void continueMoving() {
        switch (draggedCorner) {
            case NONE: {
                int oldTopLeftCornerX = currentlyDraggedBoundary.getLeftCornerX();
                int oldTopLeftCornerY = currentlyDraggedBoundary.getLeftCornerX();
                currentlyDraggedBoundary.setLeftCornerX(initialTopLeftCornerX + mouseX - startPointX);
                currentlyDraggedBoundary.setLeftCornerY(initialTopLeftCornerY + mouseY - startPointY);
                if (!BoundaryUtils.fitsIntoPage(currentlyDraggedBoundary)) {
                    currentlyDraggedBoundary.setLeftCornerX(oldTopLeftCornerX);
                    currentlyDraggedBoundary.setLeftCornerY(oldTopLeftCornerY);
                }
                break;
            }

            case TOPLEFT: {
                break;
            }

            case TOPRIGHT: {
                break;
            }

            case BOTTOMRIGHT: {
                break;
            }

            case BOTTOMLEFT: {
                break;
            }
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
