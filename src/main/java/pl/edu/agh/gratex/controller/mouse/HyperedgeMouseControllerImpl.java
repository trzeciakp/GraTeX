package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.AlterationOperation;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.edge.EdgeUtils;
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
            if (ctrlDown || currentlyAddedHyperedge.getConnectedVertices().size() < 2)
            {
                Vertex vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY);
                if (vertex == null) {
                    edgeDragDummy.setPosX(mouseX);
                    edgeDragDummy.setPosY(mouseY);
                    vertex = edgeDragDummy;
                }
                currentlyAddedHyperedge.getConnectedVertices().add(vertex);
                currentlyAddedHyperedge.calculateJoinPosition();
                return currentlyAddedHyperedge;
            }
        }
        return null;
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        if (currentlyAddedHyperedge == null) { }

    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {

    }

    @Override
    public void finishMoving() {

    }
}
