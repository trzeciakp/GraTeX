package pl.edu.agh.gratex.model.vertex;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.GraphNumeration;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;


@SuppressWarnings("serial")
public class Vertex extends GraphElement implements Serializable {

    // Wartości edytowalne przez użytkowanika
    private int number;
    private int radius;
    private int shape;
    private Color vertexColor;
    private int lineWidth;
    private LineType lineType;
    private Color lineColor;
    private Font font = Const.DEFAULT_FONT;
    private Color fontColor;
    private boolean labelInside;

    // Wartości potrzebne do parsowania
    private int posX;
    private int posY;
    private LabelV label = null;
    private String text;

    public Vertex(Graph graph) {
        super(graph);
    }

    public int getNumber() {
        return number;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.VERTEX;
    }

    @Override
    public void addToGraph() {
        graph.getVertices().add(this);
        VertexUtils.setPartOfNumeration(this, true);
        setLatexCode(graph.getGeneralController().getParseController().getVertexParser().parseToLatex(this));
    }

    @Override
    public void removeFromGraph() {
        graph.getVertices().remove(this);
        VertexUtils.setPartOfNumeration(this, false);
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        List<GraphElement> result = new LinkedList<>();
        if (label != null) {
            result.add(label);
        }
        for (Edge edge : GraphUtils.getAdjacentEdges(graph, this)) {
            result.add(edge);
        }
        return result;
    }

    public void draw(Graphics2D g2d, boolean dummy) {
        VertexUtils.drawVertex(this, g2d, dummy);
    }

    public void drawLabel(Graphics2D g, boolean dummy) {
        if (getLabel() != null) {
            getLabel().draw(g, dummy);
        }
    }

    public void setModel(PropertyModel pm) {
        VertexPropertyModel model = (VertexPropertyModel) pm;

        if (model.number > -1) {
            VertexUtils.setPartOfNumeration(this, false);
            VertexUtils.updateNumber(this, model.number);
            VertexUtils.setPartOfNumeration(this, true);
        }

        if (model.radius > -1) {
            setRadius(model.radius);
        }

        if (model.type > -1) {
            setShape(model.type);
        }

        if (model.vertexColor != null) {
            setVertexColor(new Color(model.vertexColor.getRGB()));
        }

        if (model.lineWidth > -1) {
            setLineWidth(model.lineWidth);
        }

        if (model.lineType != LineType.EMPTY) {
            setLineType(model.lineType);
        }

        if (model.lineColor != null) {
            setLineColor(new Color(model.lineColor.getRGB()));
        }

        if (model.fontColor != null) {
            setFontColor(new Color(model.fontColor.getRGB()));
        }

        if (model.labelInside > -1) {
            setLabelInside((model.labelInside == 1));
        }
    }

    public PropertyModel getModel() {
        VertexPropertyModel result = new VertexPropertyModel();

        result.number = number;
        result.radius = getRadius();
        result.type = getShape();
        result.vertexColor = new Color(getVertexColor().getRGB());
        result.lineWidth = getLineWidth();
        result.lineType = getLineType();
        result.lineColor = new Color(getLineColor().getRGB());
        result.labelInside = 0;
        if (isLabelInside()) {
            result.fontColor = new Color(getFontColor().getRGB());
            result.labelInside = 1;
        }

        return result;
    }

    // ============================================
    // Getters & setters

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getShape() {
        return shape;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public void setShape(ShapeType shape) {
        this.shape = shape.getValue();
    }

    public ShapeType getShapeENUM() {
        return ShapeType.values()[shape];
    }

    public Color getVertexColor() {
        return vertexColor;
    }

    public void setVertexColor(Color vertexColor) {
        this.vertexColor = vertexColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Font getFont() {
        return font;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public boolean isLabelInside() {
        return labelInside;
    }

    public void setLabelInside(boolean labelInside) {
        this.labelInside = labelInside;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public LabelV getLabel() {
        return label;
    }

    public void setLabel(LabelV label) {
        this.label = label;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNumber(int number) {
        this.number = number;
        this.text = GraphNumeration.digitalToAlphabetical(number);
    }

    public String getLabelInside() {
        if(getGraph().getGraphNumeration().isNumerationDigital()) {
            return String.valueOf(number);
        } else {
            return text;
        }
    }
}
