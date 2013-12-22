package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.AlterationOperation;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.controller.operation.GenericOperation;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.awt.*;
import java.awt.event.MouseEvent;

public class VertexMouseControllerImpl extends GraphElementMouseController {

    private GeneralController generalController;
    private Vertex currentlyDraggedVertex;
    private AlterationOperation currentDragOperation;

    public VertexMouseControllerImpl(GeneralController generalController) {
        super(generalController);
        this.generalController = generalController;
    }

    @Override
    public void shiftDownChanged() {
    }

    @Override
    public void reset() {
        finishMovingElement();
        currentlyDraggedVertex = null;
        currentDragOperation = null;
    }

    @Override
    public void paintCurrentlyAddedElement(Graphics2D g) {
        Vertex vertex = new Vertex(generalController.getGraph());
        vertex.setModel(generalController.getGraph().getVertexDefaultModel());
        VertexUtils.updateNumber(vertex, generalController.getGraph().getGraphNumeration().getNextFreeNumber());
        vertex.setPosX(mouseX);
        vertex.setPosY(mouseY);
        if (!GraphUtils.checkVertexCollision(generalController.getGraph(), vertex) && VertexUtils.fitsIntoPage(vertex)) {
            vertex.draw(g, true);
        }
    }

    @Override
    public Vertex getElementFromPosition(MouseEvent e) {
        return GraphUtils.getVertexFromPosition(generalController.getGraph(), e.getX(), e.getY());
    }

    @Override
    public void addNewElement(MouseEvent e) {
        Vertex vertex = new Vertex(generalController.getGraph());
        vertex.setModel(generalController.getGraph().getVertexDefaultModel());
        vertex.setPosX(e.getX());
        vertex.setPosY(e.getY());
        if (generalController.getGraph().gridOn) {
            VertexUtils.adjustToGrid(vertex);
        }

        if (VertexUtils.fitsIntoPage(vertex)) {
            if (!GraphUtils.checkVertexCollision(generalController.getGraph(), vertex)) {
                VertexUtils.updateNumber(vertex, generalController.getGraph().getGraphNumeration().getNextFreeNumber());
                new CreationRemovalOperation(generalController, vertex, OperationType.ADD_VERTEX, StringLiterals.INFO_VERTEX_ADD, true);
            } else {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CANNOT_CREATE_VERTEX_COLLISION));
            }
        } else {
            generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CANNOT_CREATE_VERTEX_BOUNDARY));
        }
    }

    @Override
    public void moveSelection(MouseEvent e) {
        if(currentlyDraggedVertex == null) {
            currentlyDraggedVertex = getElementFromPosition(e);
            generalController.getSelectionController().addToSelection(currentlyDraggedVertex, false);

            currentDragOperation = new AlterationOperation(generalController, currentlyDraggedVertex, OperationType.MOVE_VERTEX, StringLiterals.INFO_VERTEX_MOVE);
        } else {
            Vertex vertex = currentlyDraggedVertex;
            generalController.getGraph().getVertices().remove(vertex);
            int oldPosX = vertex.getPosX();
            int oldPosY = vertex.getPosY();

            vertex.setPosX(e.getX());
            vertex.setPosY(e.getY());

            if (generalController.getGraph().gridOn) {
                VertexUtils.adjustToGrid(vertex);
            }

            if (GraphUtils.checkVertexCollision(generalController.getGraph(), vertex) || !VertexUtils.fitsIntoPage(vertex)) {
                vertex.setPosX(oldPosX);
                vertex.setPosY(oldPosY);
            }
            generalController.getGraph().getVertices().add(vertex);
        }
    }

    @Override
    public void finishMovingElement() {
        if(currentlyDraggedVertex != null) {
            currentDragOperation.finish();
            currentlyDraggedVertex = null;
        }
    }
}
