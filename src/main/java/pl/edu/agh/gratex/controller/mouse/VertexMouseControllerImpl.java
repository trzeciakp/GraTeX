package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.editor.DragOperation;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
import pl.edu.agh.gratex.view.ControlManager;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *
 */
public class VertexMouseControllerImpl extends GraphElementMouseController {

    private GeneralController generalController;
    private Vertex currentlyMovedElement;
    private DragOperation currentDragOperation;

    public VertexMouseControllerImpl(GeneralController generalController) {
        super(generalController);
        this.generalController = generalController;
    }

    @Override
    public void processShiftPressing(boolean flag) {

    }

    @Override
    public void clear() {
        finishMovingElement();
        currentlyMovedElement = null;
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
                // TODO Tutaj proba zastapienia dodawania tym nowym
                //ControlManager.operations.addNewOperation(new AddOperation(generalController, vertex));
                //generalController.publishInfo(ControlManager.operations.redo());
                //generalController.getSelectionController().addToSelection(vertex, false);
            } else {
                generalController.publishInfo(StringLiterals.INFO_CANNOT_CREATE_VERTEX_COLLISION);
            }
        } else {
            generalController.publishInfo(StringLiterals.INFO_CANNOT_CREATE_VERTEX_BOUNDARY);
        }
    }

    @Override
    public void moveSelection(MouseEvent e) {
        if(currentlyMovedElement == null) {
            currentlyMovedElement = getElementFromPosition(e);
            currentDragOperation = new DragOperation(currentlyMovedElement);
            currentDragOperation.setStartPos(((Vertex) currentlyMovedElement).getPosX(), ((Vertex) currentlyMovedElement).getPosY());
        } else {
            Vertex vertex = (Vertex) currentlyMovedElement;
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
        if(currentlyMovedElement != null) {
            currentDragOperation.setEndPos(((Vertex) currentlyMovedElement).getPosX(), ((Vertex) currentlyMovedElement).getPosY());
            if (currentDragOperation.changeMade()) {
                ControlManager.operations.addNewOperation(currentDragOperation);
                generalController.publishInfo(ControlManager.operations.redo());
            }
            currentlyMovedElement = null;
        }
    }
}
