package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;

import java.awt.*;
import java.util.EnumMap;

/**
 *
 */
public class PropertyModelFactoryImpl implements PropertyModelFactory {

    private EnumMap<GraphElementType, PropertyModel> templateModelMap = new EnumMap<>(GraphElementType.class);
    private EnumMap<GraphElementType, PropertyModel> defaultModelMap = new EnumMap<>(GraphElementType.class);
    private EnumMap<GraphElementType, PropertyModel> emptyModelMap = new EnumMap<>(GraphElementType.class);

    public PropertyModelFactoryImpl() {
        emptyModelMap.put(GraphElementType.VERTEX, new VertexPropertyModel());
        emptyModelMap.put(GraphElementType.LABEL_VERTEX, new LabelVertexPropertyModel());
        emptyModelMap.put(GraphElementType.EDGE, new EdgePropertyModel());
        emptyModelMap.put(GraphElementType.LABEL_EDGE, new LabelEdgePropertyModel());

        defaultModelMap.put(GraphElementType.VERTEX, new VertexPropertyModel());
        defaultModelMap.put(GraphElementType.LABEL_VERTEX, new LabelVertexPropertyModel());
        defaultModelMap.put(GraphElementType.EDGE, new EdgePropertyModel());
        defaultModelMap.put(GraphElementType.LABEL_EDGE, new LabelEdgePropertyModel());

        initDefaultModels();

        for (GraphElementType graphElementType : GraphElementType.values()) {
            templateModelMap.put(graphElementType, defaultModelMap.get(graphElementType).getCopy());
        }
    }

    @Override
    public PropertyModel createEmptyModel(GraphElementType type) {
        return emptyModelMap.get(type).getCopy();
    }

    @Override
    public PropertyModel createDefaultModel(GraphElementType type) {
        return defaultModelMap.get(type);
    }

    @Override
    public PropertyModel createTemplateModel(GraphElementType type) {
        return templateModelMap.get(type).getCopy();
    }

    @Override
    public void setTemplateModel(GraphElementType type, PropertyModel model) {
        templateModelMap.get(type).updateWithModel(model);
    }

    public void initDefaultModels() {
        //TODO
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setNumber(PropertyModel.EMPTY);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setRadius(40);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setShape(1);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setVertexColor(new Color(new Float(1), new Float(0.5), new Float(0)));
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setLineType(LineType.SOLID);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setLineWidth(1);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setLineColor(Color.black);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setFontColor(Color.black);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setLabelInside(PropertyModel.YES);

        ((EdgePropertyModel) createDefaultModel(GraphElementType.EDGE)).setLineType(LineType.SOLID);
        ((EdgePropertyModel) createDefaultModel(GraphElementType.EDGE)).setLineWidth(1);
        ((EdgePropertyModel) createDefaultModel(GraphElementType.EDGE)).setDirected(PropertyModel.NO);
        ((EdgePropertyModel) createDefaultModel(GraphElementType.EDGE)).setLineColor(Color.black);
        ((EdgePropertyModel) createDefaultModel(GraphElementType.EDGE)).setRelativeEdgeAngle(PropertyModel.EMPTY);

        ((LabelVertexPropertyModel) createDefaultModel(GraphElementType.LABEL_VERTEX)).setText("Label");
        ((LabelVertexPropertyModel) createDefaultModel(GraphElementType.LABEL_VERTEX)).setFontColor(Color.black);
        ((LabelVertexPropertyModel) createDefaultModel(GraphElementType.LABEL_VERTEX)).setPosition(PropertyModel.EMPTY);
        ((LabelVertexPropertyModel) createDefaultModel(GraphElementType.LABEL_VERTEX)).setSpacing(5);

        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setText("Label");
        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setFontColor(Color.black);
        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setPosition(PropertyModel.EMPTY);
        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setSpacing(5);
        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setTopPlacement(1);
        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setHorizontalPlacement(0);
    }


}
