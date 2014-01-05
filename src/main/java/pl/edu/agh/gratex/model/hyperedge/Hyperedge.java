package pl.edu.agh.gratex.model.hyperedge;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Hyperedge extends GraphElement {
    public Hyperedge(Graph graph, PropertyModel propertyModel) {
        super(graph, propertyModel);
    }

    //it should contain unique number to identify joint, any vertex cannot have the same number
    //it can be string, not necessary number
    public int getNumber() {
        //TODO
        return 1;
    }

    @Override
    public void finalizeAddingToGraph() {

    }

    @Override
    public void finalizeRemovingFromGraph() {

    }

    @Override
    public Area getArea() {
        return new Area();
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.HYPEREDGE;
    }

    @Override
    public int getDrawingPriority() {
        return 0;
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        return new ArrayList<>();
    }
}
