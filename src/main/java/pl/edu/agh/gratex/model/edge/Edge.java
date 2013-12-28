package pl.edu.agh.gratex.model.edge;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.draw.EdgeDrawable;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.io.Serializable;
import java.util.*;
import java.util.List;


public class Edge extends GraphElement implements Serializable {
    private static final long serialVersionUID = -7941761380307220731L;

    // Wartości edytowalne przez użytkowanika
    private LineType lineType;
    private int lineWidth;
    private boolean directed;                                        // Z shiftem rysujemy skierowaną
    private int arrowType;
    private Color lineColor;
    private int relativeEdgeAngle;

    // Wartości potrzebne do parsowania
    private Vertex vertexA;
    private Vertex vertexB;
    private LabelE label;
    private int inAngle;
    private int outAngle;

    // Pozostałe
    private Point arcMiddle = new Point();
    private int arcRadius;
    private Point outPoint;
    private Point inPoint;
    private Arc2D.Double arc;
    private int[] arrowLine1 = null;
    private int[] arrowLine2 = null;

    public Edge(Graph graph) {
        super(graph);
        //setDrawable(new EdgeDrawable());
    }

    public boolean isLoop() {
        return (vertexA != null && vertexA == vertexB);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (inAngle != edge.inAngle) return false;
        if (outAngle != edge.outAngle) return false;
        if (vertexA != null ? !vertexA.equals(edge.vertexA) : edge.vertexA != null) return false;
        if (vertexB != null ? !vertexB.equals(edge.vertexB) : edge.vertexB != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = vertexA != null ? vertexA.getNumber() : 0;
        result = 31 * result + (vertexB != null ? vertexB.getNumber() : 0);
        result = 31 * result + inAngle;
        result = 31 * result + outAngle;
        return result;
    }

    public int getNumber() {
        return hashCode();
    }

//    public Edge getCopy(LinkedList<Vertex> vertices) {
//        Vertex _vertexA = null;
//        Vertex _vertexB = null;
//        Iterator<Vertex> itv = vertices.listIterator();
//        Vertex tempV;
//        while (itv.hasNext()) {
//            tempV = itv.next();
//            if (tempV.getPosX() == getVertexA().getPosX() && tempV.getPosY() == getVertexA().getPosY()) {
//                _vertexA = tempV;
//            }
//            if (tempV.getPosX() == getVertexB().getPosX() && tempV.getPosY() == getVertexB().getPosY()) {
//                _vertexB = tempV;
//            }
//        }
//
//        Edge result = new Edge(this.graph);
//        result.setModel(getModel());
//        if (_vertexA == null || _vertexB == null) {
//            return null;
//        }
//        result.setVertexA(_vertexA);
//        result.setVertexB(_vertexB);
//
//        if (getLabel() != null) {
//            result.setLabel(getLabel().getCopy(result));
//        }
//
//        return result;
//    }

    public void setModel(PropertyModel pm) {
        EdgePropertyModel model = (EdgePropertyModel) pm;

        if (model.lineType != LineType.EMPTY) {
            setLineType(model.lineType);
        }

        if (model.lineWidth > -1) {
            setLineWidth(model.lineWidth);
        }

        if (model.directed > -1) {
            setDirected((model.directed == 1));
        }

        if (model.lineColor != null) {
            setLineColor(new Color(model.lineColor.getRGB()));
        }

        if (model.relativeEdgeAngle > -1) {
            setRelativeEdgeAngle(model.relativeEdgeAngle);
        }

        if (model.arrowType > -1) {
            setArrowType(model.arrowType);
        }
    }

    public PropertyModel getModel() {
        EdgePropertyModel result = new EdgePropertyModel();

        result.lineWidth = getLineWidth();
        result.lineType = getLineType();
        result.arrowType = getArrowType();
        result.lineColor = new Color(getLineColor().getRGB());
        result.relativeEdgeAngle = getRelativeEdgeAngle();
        if (getVertexA() == getVertexB()) {
            result.isLoop = PropertyModel.YES;
        } else
            result.isLoop = PropertyModel.NO;
        result.directed = 0;
        if (isDirected()) {
            result.directed = 1;
        }

        return result;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.EDGE;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    public void drawLabel(Graphics2D g, boolean dummy) {
        if (getLabel() != null) {
            getLabel().draw(g, dummy);
        }
    }

    @Override
    public void addToGraph(String code) {
        graph.getEdges().add(this);
        EdgeUtils.updatePosition(this);
        setLatexCode(code);
    }

    @Override
    public void removeFromGraph() {
        getGraph().getEdges().remove(this);
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        List<GraphElement> result = new LinkedList<>();
        if (label != null) {
            result.add(label);
        }
        return result;
    }
/*
    @Override
    public void draw(Graphics2D g, boolean dummy) {
        EdgeUtils.draw(this, g, dummy);
    }*/


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

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public int getArrowType() {
        return arrowType;
    }

    public void setArrowType(int arrowType) {
        this.arrowType = arrowType;
    }

    public void setArrowType(ArrowType arrowType) {
        this.arrowType = arrowType.getValue();
    }

    public ArrowType getArrowTypeENUM() {
        return ArrowType.values()[this.arrowType + 1];
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public int getRelativeEdgeAngle() {
        return relativeEdgeAngle;
    }

    public void setRelativeEdgeAngle(int relativeEdgeAngle) {
        this.relativeEdgeAngle = relativeEdgeAngle;
    }

    public Vertex getVertexA() {
        return vertexA;
    }

    public void setVertexA(Vertex vertexA) {
        this.vertexA = vertexA;
    }

    public Vertex getVertexB() {
        return vertexB;
    }

    public void setVertexB(Vertex vertexB) {
        this.vertexB = vertexB;
    }

    public LabelE getLabel() {
        return label;
    }

    public void setLabel(LabelE label) {
        this.label = label;
    }

    public int getInAngle() {
        return inAngle;
    }

    public void setInAngle(int inAngle) {
        this.inAngle = inAngle;
    }

    public int getOutAngle() {
        return outAngle;
    }

    public void setOutAngle(int outAngle) {
        this.outAngle = outAngle;
    }

    public Point getArcMiddle() {
        return arcMiddle;
    }

    public void setArcMiddle(Point arcMiddle) {
        this.arcMiddle = arcMiddle;
    }

    public int getArcRadius() {
        return arcRadius;
    }

    public void setArcRadius(int arcRadius) {
        this.arcRadius = arcRadius;
    }

    public Point getOutPoint() {
        return outPoint;
    }

    public void setOutPoint(Point outPoint) {
        this.outPoint = outPoint;
    }

    public Point getInPoint() {
        return inPoint;
    }

    public void setInPoint(Point inPoint) {
        this.inPoint = inPoint;
    }

    public Arc2D.Double getArc() {
        return arc;
    }

    public void setArc(Arc2D.Double arc) {
        this.arc = arc;
    }

    public int[] getArrowLine1() {
        return arrowLine1;
    }

    public void setArrowLine1(int[] arrowLine1) {
        this.arrowLine1 = arrowLine1;
    }

    public int[] getArrowLine2() {
        return arrowLine2;
    }

    public void setArrowLine2(int[] arrowLine2) {
        this.arrowLine2 = arrowLine2;
    }
}
