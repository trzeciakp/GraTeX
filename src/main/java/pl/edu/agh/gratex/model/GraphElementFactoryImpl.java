package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.Vertex;

/**
 *
 */
public class GraphElementFactoryImpl implements GraphElementFactory {

    private DrawerFactory drawerFactory;
    private PropertyModelFactory propertyModelFactory;

    public GraphElementFactoryImpl(DrawerFactory drawerFactory, PropertyModelFactory propertyModelFactory) {
        this.drawerFactory = drawerFactory;
        this.propertyModelFactory = propertyModelFactory;
    }

    @Override
    public GraphElement create(GraphElementType type, Graph graph) {
        GraphElement result = null;
        PropertyModel propertyModel = propertyModelFactory.createTemplateModel(type);
        switch (type) {
            case VERTEX:
                result = new Vertex(graph, propertyModel);
                break;
            case EDGE:
                result = new Edge(graph, propertyModel);
                break;
            case LABEL_VERTEX:
                result = new LabelV(null, graph, propertyModel);
                break;
            case LABEL_EDGE:
                result = new LabelE(null, graph, propertyModel);
                break;
            case BOUNDARY:
                result = new Boundary(graph, propertyModel);
                break;
            case HYPEREDGE:
                result = new Hyperedge(graph, propertyModel);
                break;
        }
        result.setDrawer(drawerFactory.createDefaultDrawer(type));
        return result;
    }

    @Override
    public DrawerFactory getDrawerFactory() {
        return drawerFactory;
    }

    @Override
    public PropertyModelFactory getPropertyModelFactory() {
        //TODO
        return propertyModelFactory;
    }
}
