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
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.awt.*;
import java.util.*;
import java.util.List;


public class VertexMouseControllerImpl extends GraphElementMouseController {

    private GeneralController generalController;
    private Vertex currentlyDraggedVertex;
    private AlterationOperation currentDragOperation;

    public VertexMouseControllerImpl(GeneralController generalController, GraphElementFactory graphElementFactory) {
        super(generalController, graphElementFactory);
        this.generalController = generalController;
    }

    @Override
    public void reset() {
        finishMoving();
        currentlyDraggedVertex = null;
        currentDragOperation = null;
    }

    @Override
    public List<GraphElement> getCurrentlyAddedElements() {
        List<GraphElement> result = new LinkedList<>();
        Vertex vertex = (Vertex) getGraphElementFactory().create(GraphElementType.VERTEX, generalController.getGraph());
        vertex.setNumber(generalController.getGraph().getGraphNumeration().getNextFreeNumber());
        vertex.setPosX(mouseX);
        vertex.setPosY(mouseY);
        if (!GraphUtils.checkVertexCollision(generalController.getGraph(), vertex) && VertexUtils.fitsIntoPage(vertex)) {
            result.add(vertex);
        }
        return result;
    }

    @Override
    public Vertex getElementFromPosition(int mouseX, int mouseY) {
        return GraphUtils.getVertexFromPosition(generalController.getGraph(), mouseX, mouseY);
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        Vertex vertex = (Vertex) getGraphElementFactory().create(GraphElementType.VERTEX, generalController.getGraph());
        vertex.setPosX(mouseX);
        vertex.setPosY(mouseY);
        if (generalController.getGraph().isGridOn()) {
            VertexUtils.adjustToGrid(vertex);
        }

        if (VertexUtils.fitsIntoPage(vertex)) {
            if (!GraphUtils.checkVertexCollision(generalController.getGraph(), vertex)) {
                vertex.setNumber(generalController.getGraph().getGraphNumeration().getNextFreeNumber());
                new CreationRemovalOperation(generalController, vertex, OperationType.ADD_VERTEX, StringLiterals.INFO_VERTEX_ADD, true);
            } else {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CANNOT_CREATE_VERTEX_COLLISION));
            }
        } else {
            generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CANNOT_CREATE_VERTEX_BOUNDARY));
        }
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {
        if(currentlyDraggedVertex == null) {
            currentlyDraggedVertex = getElementFromPosition(mouseX, mouseY);

            currentDragOperation = new AlterationOperation(generalController, currentlyDraggedVertex, OperationType.MOVE_VERTEX, StringLiterals.INFO_VERTEX_MOVE);
        } else {
            generalController.getSelectionController().addToSelection(currentlyDraggedVertex, false);
            Vertex vertex = currentlyDraggedVertex;
            generalController.getGraph().getVertices().remove(vertex);
            int oldPosX = vertex.getPosX();
            int oldPosY = vertex.getPosY();

            vertex.setPosX(mouseX);
            vertex.setPosY(mouseY);

            if (generalController.getGraph().isGridOn()) {
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
    public void finishMoving() {
        if(currentlyDraggedVertex != null) {
            currentDragOperation.finish();
            currentlyDraggedVertex = null;
        }
    }
}
