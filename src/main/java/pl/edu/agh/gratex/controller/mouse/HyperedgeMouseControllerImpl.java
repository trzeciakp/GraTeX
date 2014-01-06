package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.AlterationOperation;
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
            currentlyAddedHyperedge.getConnectedVertices().remove(edgeDragDummy);
            if (ctrlDown || currentlyAddedHyperedge.getConnectedVertices().size() < 2) {
                currentlyAddedHyperedge.getConnectedVertices().add(edgeDragDummy);
            }
            currentlyAddedHyperedge.calculateJointPosition();
            return currentlyAddedHyperedge;

        }
        return null;
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        Vertex vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY);
        if (vertex == null) {
            if (currentlyAddedHyperedge == null) {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_HYPEREDGE_START));
            } else {
                currentlyAddedHyperedge = null;
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_HYPEREDGE_ADDING_CANCELLED));
            }
        } else {
            if (currentlyAddedHyperedge == null) {
                currentlyAddedHyperedge = (Hyperedge) graphElementFactory.create(GraphElementType.HYPEREDGE, generalController.getGraph());
                currentlyAddedHyperedge.getConnectedVertices().add(vertex);
                currentlyAddedHyperedge.getConnectedVertices().add(edgeDragDummy);
            } else if (!currentlyAddedHyperedge.getConnectedVertices().contains(vertex)) {
                currentlyAddedHyperedge.getConnectedVertices().add(vertex);
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
