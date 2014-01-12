package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.graph.Graph;

/**
 *
 */
public interface GraphElementFactory {

    GraphElement create(GraphElementType type, Graph graph);
    DrawerFactory getDrawerFactory();
    PropertyModelFactory getPropertyModelFactory();

    Graph createExampleGraph();
}
