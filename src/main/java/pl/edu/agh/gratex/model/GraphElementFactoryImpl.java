package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.draw.DrawableFactory;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.Vertex;

/**
 *
 */
public class GraphElementFactoryImpl implements GraphElementFactory {

    private DrawableFactory drawableFactory;

    public GraphElementFactoryImpl(DrawableFactory drawableFactory) {
        this.drawableFactory = drawableFactory;
    }

    @Override
    public GraphElement create(GraphElementType type, Graph graph) {
        GraphElement result = null;
        switch (type) {
            case VERTEX:
                result = new Vertex(graph);
                break;
            case EDGE:
                result = new Edge(graph);
                break;
            case LABEL_VERTEX:
                result = new LabelV(null, graph);
                break;
            case LABEL_EDGE:
                result = new LabelE(null, graph);
                break;
        }
        if(result != null) {
            result.setDrawable(drawableFactory.createDefaultDrawable(type));
        }
        return result;
    }

    @Override
    public DrawableFactory getDrawableFactory() {
        return drawableFactory;
    }
}
