package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;

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

    public PropertyModel createDefaultModel(GraphElementType type) {
        switch (type) {
            case VERTEX:
                return createVertexDefaultModel();
            case EDGE:
                return createEdgeDefaultModel();
            case LABEL_VERTEX:
                return createLabelVertexDefaultModel();
            case LABEL_EDGE:
                return createLabelEdgeDefaultModel();
            default:
                return null;
        }
    }

    public PropertyModel createCopyModel(GraphElement type) {
        switch (type.getType()) {
            case VERTEX:
                return createVertexCopyModel((VertexPropertyModel) type.getModel());
            case EDGE:
                return createEdgeCopyModel((EdgePropertyModel) type.getModel());
            case LABEL_VERTEX:
                return createLabelVertexCopyModel((LabelVertexPropertyModel) type.getModel());
            case LABEL_EDGE:
                return createLabelEdgeCopyModel((LabelEdgePropertyModel) type.getModel());
            default:
                return null;
        }
    }


    private PropertyModel createLabelEdgeCopyModel(LabelEdgePropertyModel model) {
        return new LabelEdgePropertyModel(model);
    }

    private PropertyModel createLabelVertexCopyModel(LabelVertexPropertyModel model) {
        return new LabelVertexPropertyModel(model);
    }

    private PropertyModel createEdgeCopyModel(EdgePropertyModel model) {
        return new EdgePropertyModel(model);
    }

    private PropertyModel createVertexCopyModel(VertexPropertyModel model) {
        return new VertexPropertyModel(model);
    }

    private PropertyModel createLabelEdgeDefaultModel() {
        return null;
    }

    private PropertyModel createLabelVertexDefaultModel() {
        return null;
    }

    private PropertyModel createEdgeDefaultModel() {
        return null;
    }

    private PropertyModel createVertexDefaultModel() {
        return null;
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
