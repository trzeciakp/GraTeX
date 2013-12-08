package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;

/**
 *
 */
public class PropertyModelFactory {

    public PropertyModel create(GraphElementType type) {
        switch (type) {
            case VERTEX:
                return createVertexPropertyModel();
            case EDGE:
                return createEdgePropertyModel();
            case LABEL_VERTEX:
                return createLabelVertexPropertyModel();
            case LABEL_EDGE:
                return createLabelEdgePropertyModel();
            default:
                //TODO maybe null object?
                return null;
        }
    }

    private LabelVertexPropertyModel createLabelVertexPropertyModel() {
        return new LabelVertexPropertyModel();
    }

    private LabelEdgePropertyModel createLabelEdgePropertyModel() {
        return new LabelEdgePropertyModel();
    }

    private EdgePropertyModel createEdgePropertyModel() {
        return new EdgePropertyModel();
    }

    private VertexPropertyModel createVertexPropertyModel() {
        return new VertexPropertyModel();
    }

    //TODO add defaults
}
