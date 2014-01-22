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
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.util.LinkedList;
import java.util.List;


public class VertexMouseControllerImpl extends GraphElementMouseController {
    private AlterationOperation currentDragOperation;
    private List<GraphElement> currentlyDraggedVertices;
    public int biasX;
    public int biasY;
    public int startX;
    public int startY;

    public VertexMouseControllerImpl(GeneralController generalController, GraphElementFactory graphElementFactory, GraphElementType handledGraphElementType) {
        super(generalController, graphElementFactory, handledGraphElementType);
    }

    @Override
    public void reset() {
        finishMoving();
        currentlyDraggedVertices = null;
        currentDragOperation = null;
    }

    @Override
    public GraphElement getCurrentlyAddedElement() {
        Vertex vertex = (Vertex) getGraphElementFactory().create(GraphElementType.VERTEX, generalController.getGraph());
        vertex.setNumber(generalController.getGraph().getGraphNumeration().getNextFreeNumber());
        vertex.setPosX(mouseX);
        vertex.setPosY(mouseY);
        if (!GraphUtils.checkVertexCollision(generalController.getGraph(), vertex) && VertexUtils.fitsIntoPage(vertex)) {
            return vertex;
        }
        return null;
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
        if(currentlyDraggedVertices == null) {
            currentlyDraggedVertices = new LinkedList<>(generalController.getSelectionController().getSelection());
            currentDragOperation = new AlterationOperation(generalController, currentlyDraggedVertices,
                    OperationType.MOVE_VERTEX, StringLiterals.INFO_VERTEX_MOVE);
            biasX = 0;
            biasY = 0;
            startX = mouseX;
            startY = mouseY;
        } else {
            int oldPosX = startX + biasX;
            int oldPosY = startY + biasY;

            for (GraphElement element : currentlyDraggedVertices) {
                Vertex vertex = ((Vertex) element);
                vertex.setPosX(vertex.getPosX() - biasX + mouseX - startX);
                vertex.setPosY(vertex.getPosY() - biasY + mouseY - startY);

                if (generalController.getGraph().isGridOn()) {
                    VertexUtils.adjustToGrid(vertex);
                }
            }

            biasX = mouseX - startX;
            biasY = mouseY - startY;

//            if (GraphUtils.checkVertexCollision(generalController.getGraph(), vertex) || !VertexUtils.fitsIntoPage(vertex)) {
//                vertex.setPosX(oldPosX);
//                vertex.setPosY(oldPosY);
//            }
        }
    }

    @Override
    public void finishMoving() {
        if(currentlyDraggedVertices != null) {
            currentDragOperation.finish(true);
            currentlyDraggedVertices = null;
        }
    }
}
