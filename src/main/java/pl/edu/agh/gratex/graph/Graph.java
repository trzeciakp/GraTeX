package pl.edu.agh.gratex.graph;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.graph.utils.Geometry;
import pl.edu.agh.gratex.gui.ControlManager;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.awt.geom.Area;
import java.io.Serializable;
import java.util.*;
import java.util.List;


public class Graph implements Serializable {
    private static final long serialVersionUID = 6647099578243878702L;

    private LinkedList<Vertex> vertices;
    private LinkedList<Edge> edges;
    private LinkedList<LabelV> labelsV;
    private LinkedList<LabelE> labelsE;

    private VertexPropertyModel vertexDefaultModel;
    private EdgePropertyModel edgeDefaultModel;
    private LabelVertexPropertyModel labelVDefaultModel;
    private LabelEdgePropertyModel labelEDefaultModel;

    private EnumMap<GraphElementType, List<? extends GraphElement>> elements;
    private EnumMap<GraphElementType, List<? extends PropertyModel>> defaultModels;

    public int gridResolutionX = 20;
    public int gridResolutionY = 20;
    public boolean gridOn;

    public int pageWidth = 672;
    public int pageHeight = 880;

    public boolean[] usedNumber;
    public int startNumber = 1;
    public int maxNumber = 703;    // ZZ tyle wynosi, 3 litery to za dużo
    public boolean digitalNumeration = true;

    public Graph() {
        elements = new EnumMap<>(GraphElementType.class);
        for(GraphElementType type : GraphElementType.values()) {
            elements.put(type, new LinkedList<GraphElement>());
            //defaultModels(type, )
        }
        /*setVertices(new LinkedList<Vertex>());
        setEdges(new LinkedList<Edge>());
        setLabelsV(new LinkedList<LabelV>());
        setLabelsE(new LinkedList<LabelE>());*/
        initDefaultModels();

        usedNumber = new boolean[maxNumber];
        for (int i = 0; i < maxNumber; i++) {
            usedNumber[i] = false;
        }
        if (startNumber < 1 || startNumber >= maxNumber) {
            startNumber = 1;
        }
    }

    public int getNextFreeNumber() {
        for (int i = startNumber; i < maxNumber; i++) {
            if (!usedNumber[i]) {
                return i;
            }
        }
        startNumber = 1;
        return 1;
    }

    public LinkedList<GraphElement> getIntersectingElements(Rectangle area) {
        LinkedList<GraphElement> result = new LinkedList<GraphElement>();
        Area rect = new Area(area);

        if (ControlManager.getMode() == ModeType.VERTEX) {
            Iterator<Vertex> itv = getVertices().listIterator();
            Vertex temp = null;
            while (itv.hasNext()) {
                temp = itv.next();
                Area outline = new Area(Geometry.getVertexShape(temp.getShape() + 1, temp.getRadius(), temp.getPosX(), temp.getPosY()));
                outline.intersect(rect);
                if (!outline.isEmpty()) {
                    result.add(temp);
                }
            }
        } else if (ControlManager.getMode() == ModeType.EDGE) {
            Iterator<Edge> ite = getEdges().listIterator();
            Edge temp = null;
            while (ite.hasNext()) {
                temp = ite.next();
                if (Geometry.checkArcRectangleIntersection(temp, area)) {
                    result.add(temp);
                }
            }
        } else if (ControlManager.getMode() == ModeType.LABEL_VERTEX) {
            Iterator<LabelV> itlv = getLabelsV().listIterator();
            LabelV temp = null;
            while (itlv.hasNext()) {
                temp = itlv.next();
                if (temp.getOutline().intersects(area)) {
                    result.add(temp);
                }
            }
        } else if (ControlManager.getMode() == ModeType.LABEL_EDGE) {
            Iterator<LabelE> itle = getLabelsE().listIterator();
            LabelE temp = null;
            while (itle.hasNext()) {
                temp = itle.next();
                if (temp.getOutline().intersects(area)) {
                    result.add(temp);
                }
            }
        }

        return result;
    }

    public Vertex getVertexFromPosition(int x, int y) {
        Iterator<Vertex> itv = getVertices().listIterator();
        Vertex temp = null;
        while (itv.hasNext()) {
            temp = itv.next();
            if (temp.intersects(x, y)) {
                return temp;
            }
        }
        return null;
    }

    public boolean vertexCollision(Vertex vertex) {
        Iterator<Vertex> itv = getVertices().listIterator();
        while (itv.hasNext()) {
            if (itv.next().collides(vertex)) {
                return true;
            }
        }
        return false;
    }

    public LinkedList<Edge> getAdjacentEdges(Vertex vertex) {
        LinkedList<Edge> result = new LinkedList<Edge>();

        Iterator<Edge> ite = getEdges().listIterator();
        Edge temp = null;
        while (ite.hasNext()) {
            temp = ite.next();
            if (temp.getVertexA() == vertex || temp.getVertexB() == vertex) {
                result.add(temp);
            }
        }
        return result;
    }

    public Edge getEdgeFromPosition(int x, int y) {
        Iterator<Edge> ite = getEdges().listIterator();
        Edge temp = null;
        while (ite.hasNext()) {
            temp = ite.next();
            if (temp.intersects(x, y)) {
                return temp;
            }
        }
        return null;
    }

    public LabelV getLabelVFromPosition(int x, int y) {
        Iterator<LabelV> itlv = getLabelsV().listIterator();
        LabelV temp = null;
        while (itlv.hasNext()) {
            temp = itlv.next();
            if (temp.intersects(x, y)) {
                return temp;
            }
        }
        return null;
    }

    public LabelE getLabelEFromPosition(int x, int y) {
        Iterator<LabelE> itle = getLabelsE().listIterator();
        LabelE temp = null;
        while (itle.hasNext()) {
            temp = itle.next();
            if (temp.intersects(x, y)) {
                return temp;
            }
        }
        return null;
    }

    public void drawAll(Graphics2D g) {
        Iterator<Edge> ite = getEdges().listIterator();
        while (ite.hasNext()) {
            ite.next().draw(g, false);
        }

        Iterator<Vertex> itv = getVertices().listIterator();
        while (itv.hasNext()) {
            itv.next().draw(g, false);
        }

        ite = getEdges().listIterator();
        while (ite.hasNext()) {
            ite.next().drawLabel(g, false);
        }

        itv = getVertices().listIterator();
        while (itv.hasNext()) {
            itv.next().drawLabel(g, false);
        }
    }

    public void adjustVerticesToGrid() {
        Iterator<Vertex> itv = getVertices().listIterator();
        while (itv.hasNext()) {
            itv.next().adjustToGrid();
        }
    }

    public void deleteUnusedLabels() {
        Iterator<LabelV> itlv = getLabelsV().listIterator();
        while (itlv.hasNext()) {
            if (itlv.next().getOwner().getLabel() == null) {
                itlv.remove();
            }
        }

        for (LabelE edgeLabel : getLabelsE()) {
            if (edgeLabel.getOwner() == null) {
                getLabelsE().remove(edgeLabel);
            }
        }
    }

    public void initDefaultModels() {
        //TODO
        setVertexDefaultModel(new VertexPropertyModel());
        getVertexDefaultModel().number = -1;
        getVertexDefaultModel().radius = 40;
        getVertexDefaultModel().type = 1;
        getVertexDefaultModel().vertexColor = new Color(new Float(1), new Float(0.5), new Float(0));
        getVertexDefaultModel().lineType = LineType.SOLID;
        getVertexDefaultModel().lineWidth = 1;
        getVertexDefaultModel().lineColor = Color.black;
        getVertexDefaultModel().fontColor = Color.black;
        getVertexDefaultModel().labelInside = 1;

        setEdgeDefaultModel(new EdgePropertyModel());
        getEdgeDefaultModel().lineType = LineType.SOLID;
        getEdgeDefaultModel().lineWidth = 1;
        getEdgeDefaultModel().directed = 0;
        getEdgeDefaultModel().lineColor = Color.black;
        getEdgeDefaultModel().relativeEdgeAngle = 0;

        setLabelVDefaultModel(new LabelVertexPropertyModel());
        getLabelVDefaultModel().text = "Label";
        getLabelVDefaultModel().fontColor = Color.black;
        getLabelVDefaultModel().position = 0;
        getLabelVDefaultModel().spacing = 5;

        setLabelEDefaultModel(new LabelEdgePropertyModel());
        getLabelEDefaultModel().text = "Label";
        getLabelEDefaultModel().fontColor = Color.black;
        getLabelEDefaultModel().position = 50;
        getLabelEDefaultModel().spacing = 5;
        getLabelEDefaultModel().topPlacement = 1;
        getLabelEDefaultModel().horizontalPlacement = 0;
    }

    public List<Vertex> getVertices() {
        return (List<Vertex>) elements.get(GraphElementType.VERTEX);
        //return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.elements.put(GraphElementType.VERTEX, vertices);
        //this.vertices = vertices;
    }

    public List<Edge> getEdges() {
        return (List<Edge>) elements.get(GraphElementType.EDGE);
    }

    public void setEdges(List<Edge> edges) {
        this.elements.put(GraphElementType.EDGE, edges);
        //this.edges = edges;
    }

    public List<LabelV> getLabelsV() {
        return (List<LabelV>) elements.get(GraphElementType.LABEL_VERTEX);
        //return labelsV;
    }

    public void setLabelsV(LinkedList<LabelV> labelsV) {
        this.elements.put(GraphElementType.LABEL_VERTEX, labelsV);
        //this.labelsV = labelsV;
    }

    public List<LabelE> getLabelsE() {
        return (List<LabelE>) elements.get(GraphElementType.LABEL_EDGE);
        //return labelsE;
    }

    public void setLabelsE(LinkedList<LabelE> labelsE) {
        this.elements.put(GraphElementType.LABEL_EDGE, labelsE);
        //this.labelsE = labelsE;
    }

    public List<? extends GraphElement> getElements(GraphElementType type) {
        return elements.get(type);
    }

    public List<? extends GraphElement> getAllElements() {
        List<GraphElement> result = new LinkedList<>();
        for(GraphElementType type : GraphElementType.values()) {
            result.addAll(elements.get(type));
        }
        return result;
    }

    public Map<GraphElementType, List<? extends GraphElement>> getElementsMap() {
        return elements;
    }

    public VertexPropertyModel getVertexDefaultModel() {
        return vertexDefaultModel;
    }

    public void setVertexDefaultModel(VertexPropertyModel vertexDefaultModel) {
        this.vertexDefaultModel = vertexDefaultModel;
    }

    public EdgePropertyModel getEdgeDefaultModel() {
        return edgeDefaultModel;
    }

    public void setEdgeDefaultModel(EdgePropertyModel edgeDefaultModel) {
        this.edgeDefaultModel = edgeDefaultModel;
    }

    public LabelVertexPropertyModel getLabelVDefaultModel() {
        return labelVDefaultModel;
    }

    public void setLabelVDefaultModel(LabelVertexPropertyModel labelVDefaultModel) {
        this.labelVDefaultModel = labelVDefaultModel;
    }

    public LabelEdgePropertyModel getLabelEDefaultModel() {
        return labelEDefaultModel;
    }

    public void setLabelEDefaultModel(LabelEdgePropertyModel labelEDefaultModel) {
        this.labelEDefaultModel = labelEDefaultModel;
    }
}
