package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.boundary.BoundaryPropertyModel;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.hyperedge.HyperedgePropertyModel;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundaryPropertyModel;
import pl.edu.agh.gratex.model.properties.*;
import pl.edu.agh.gratex.model.properties.JointDisplay;
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
        emptyModelMap.put(GraphElementType.HYPEREDGE, new HyperedgePropertyModel());
        emptyModelMap.put(GraphElementType.BOUNDARY, new BoundaryPropertyModel());
        emptyModelMap.put(GraphElementType.LINK_BOUNDARY, new LinkBoundaryPropertyModel());

        defaultModelMap.put(GraphElementType.VERTEX, new VertexPropertyModel());
        defaultModelMap.put(GraphElementType.LABEL_VERTEX, new LabelVertexPropertyModel());
        defaultModelMap.put(GraphElementType.EDGE, new EdgePropertyModel());
        defaultModelMap.put(GraphElementType.LABEL_EDGE, new LabelEdgePropertyModel());
        defaultModelMap.put(GraphElementType.HYPEREDGE, new HyperedgePropertyModel());
        defaultModelMap.put(GraphElementType.BOUNDARY, new BoundaryPropertyModel());
        defaultModelMap.put(GraphElementType.LINK_BOUNDARY, new LinkBoundaryPropertyModel());

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
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setShape(ShapeType.CIRCLE);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setVertexColor(PropertyModel.REVERSE_COLORS.get("orange"));
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setLineType(LineType.SOLID);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setLineWidth(1);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setLineColor(Color.black);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setFontColor(Color.black);
        ((VertexPropertyModel) createDefaultModel(GraphElementType.VERTEX)).setLabelInside(IsLabelInside.YES);

        ((EdgePropertyModel) createDefaultModel(GraphElementType.EDGE)).setLineType(LineType.SOLID);
        ((EdgePropertyModel) createDefaultModel(GraphElementType.EDGE)).setLineWidth(1);
        ((EdgePropertyModel) createDefaultModel(GraphElementType.EDGE)).setDirected(IsDirected.NO);
        ((EdgePropertyModel) createDefaultModel(GraphElementType.EDGE)).setArrowType(ArrowType.BASIC);
        ((EdgePropertyModel) createDefaultModel(GraphElementType.EDGE)).setLineColor(Color.black);
        ((EdgePropertyModel) createDefaultModel(GraphElementType.EDGE)).setRelativeEdgeAngle(PropertyModel.EMPTY);

        ((LabelVertexPropertyModel) createDefaultModel(GraphElementType.LABEL_VERTEX)).setText("Label");
        ((LabelVertexPropertyModel) createDefaultModel(GraphElementType.LABEL_VERTEX)).setFontColor(Color.black);
        ((LabelVertexPropertyModel) createDefaultModel(GraphElementType.LABEL_VERTEX)).setLabelPosition(LabelPosition.EMPTY);
        ((LabelVertexPropertyModel) createDefaultModel(GraphElementType.LABEL_VERTEX)).setSpacing(5);

        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setText("Label");
        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setFontColor(Color.black);
        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setPosition(PropertyModel.EMPTY);
        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setSpacing(5);
        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setTopPlacement(LabelTopPlacement.EMPTY);
        ((LabelEdgePropertyModel) createDefaultModel(GraphElementType.LABEL_EDGE)).setHorizontalPlacement(LabelRotation.TANGENT);

        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setIsJointDisplay(JointDisplay.VISIBLE);
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setLineWidth(1);
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setLineType(LineType.SOLID);
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setLineColor(PropertyModel.REVERSE_COLORS.get("black"));
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setJointShape(ShapeType.CIRCLE);
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setJointSize(8);
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setJointColor(PropertyModel.REVERSE_COLORS.get("gray"));
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setJointLineType(LineType.SOLID);
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setJointLineWidth(1);
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setJointLineColor(PropertyModel.REVERSE_COLORS.get("black"));
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setJointHasLabel(IsLabelInside.YES);
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setText("Label");
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setJointLabelPosition(JointLabelPosition.ABOVE);
        ((HyperedgePropertyModel) createDefaultModel(GraphElementType.HYPEREDGE)).setJointLabelColor(PropertyModel.REVERSE_COLORS.get("black"));

        ((BoundaryPropertyModel) createDefaultModel(GraphElementType.BOUNDARY)).setLineType(LineType.SOLID);
        ((BoundaryPropertyModel) createDefaultModel(GraphElementType.BOUNDARY)).setLineWidth(1);
        ((BoundaryPropertyModel) createDefaultModel(GraphElementType.BOUNDARY)).setLineColor(PropertyModel.REVERSE_COLORS.get("darkgray"));
        ((BoundaryPropertyModel) createDefaultModel(GraphElementType.BOUNDARY)).setFillColor(PropertyModel.REVERSE_COLORS.get("lightgray"));

        ((LinkBoundaryPropertyModel) createDefaultModel(GraphElementType.LINK_BOUNDARY)).setLineType(LineType.SOLID);
        ((LinkBoundaryPropertyModel) createDefaultModel(GraphElementType.LINK_BOUNDARY)).setLineWidth(2);
        ((LinkBoundaryPropertyModel) createDefaultModel(GraphElementType.LINK_BOUNDARY)).setLineColor(PropertyModel.REVERSE_COLORS.get("red"));
        ((LinkBoundaryPropertyModel) createDefaultModel(GraphElementType.LINK_BOUNDARY)).setDirected(IsDirected.YES);
        ((LinkBoundaryPropertyModel) createDefaultModel(GraphElementType.LINK_BOUNDARY)).setArrowType(ArrowType.FILLED);
    }
}
