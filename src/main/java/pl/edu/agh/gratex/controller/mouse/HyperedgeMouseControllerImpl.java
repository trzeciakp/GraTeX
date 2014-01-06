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
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.vertex.Vertex;

/**
 *
 */
public class HyperedgeMouseControllerImpl extends GraphElementMouseController {
    private Hyperedge currentlyAddedHyperedge;
    private Hyperedge currentlyDraggedHyperedge;
    private AlterationOperation currentDragOperation;

    private Vertex edgeDragDummy;

    public HyperedgeMouseControllerImpl(GeneralController generalController, GraphElementFactory graphElementFactory) {
        super(generalController, graphElementFactory);
        edgeDragDummy = (Vertex) getGraphElementFactory().create(GraphElementType.VERTEX, null);
        edgeDragDummy.setRadius(2);
    }

    @Override
    public void reset() {
        finishMoving();
        currentlyAddedHyperedge = null;
        currentlyDraggedHyperedge = null;
        currentDragOperation = null;
    }

    @Override
    public GraphElement getCurrentlyAddedElement() {
        if (currentlyAddedHyperedge != null) {
            Vertex vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY);
            if (vertex == null) {
                edgeDragDummy.setPosX(mouseX);
                edgeDragDummy.setPosY(mouseY);
            } else {
                edgeDragDummy.setPosX(vertex.getPosX());
                edgeDragDummy.setPosY(vertex.getPosY());
            }
            currentlyAddedHyperedge.calculateJointPosition();
            return currentlyAddedHyperedge;
        }
        return null;
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        if (currentlyAddedHyperedge == null) {
            Vertex vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY);
            if (vertex == null) {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_HYPEREDGE_START));
            } else {
                currentlyAddedHyperedge = (Hyperedge) getGraphElementFactory().create(GraphElementType.HYPEREDGE, generalController.getGraph());
                System.out.println("vertex = [" + vertex + "]");
                currentlyAddedHyperedge.getConnectedVertices().add(vertex);
                currentlyAddedHyperedge.getConnectedVertices().add(edgeDragDummy);
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_HYPEREDGE_END));
            }
        } else {
            Vertex vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY);
            if (vertex == null) {
                currentlyAddedHyperedge = null;
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_HYPEREDGE_ADDING_CANCELLED));
            } else {
                currentlyAddedHyperedge.getConnectedVertices().remove(edgeDragDummy);
                currentlyAddedHyperedge.getConnectedVertices().add(vertex);
                for (Vertex vertex3 : currentlyAddedHyperedge.getConnectedVertices()) {
                    System.out.println("przed przeparsowaniem = [" + vertex3 + "]");
                }
                new CreationRemovalOperation(generalController, currentlyAddedHyperedge, OperationType.ADD_HYPEREDGE, StringLiterals.INFO_HYPEREDGE_ADD, true);
                currentlyAddedHyperedge = null;
                for (Vertex vertex3 : ((Hyperedge) generalController.getGraph().getElements(GraphElementType.HYPEREDGE).get(0)).getConnectedVertices()) {
                    System.out.println("po przeparsowaniu = [" + vertex3 + "]");
                }
            }
        }
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {

    }

    @Override
    public void finishMoving() {

    }
}
