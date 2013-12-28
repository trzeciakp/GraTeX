package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.Vertex;

/**
 *
 */
public class GraphElementFactoryImpl implements GraphElementFactory {
    @Override
    public GraphElement create(GraphElementType type, Graph graph) {
        switch (type) {
            case VERTEX:
                return new Vertex(graph);
            case EDGE:
                return new Edge(graph);
            case LABEL_VERTEX:
                return new LabelV(null, graph);
            case LABEL_EDGE:
                return new LabelE(null, graph);
        }
        return null;
    }
}
