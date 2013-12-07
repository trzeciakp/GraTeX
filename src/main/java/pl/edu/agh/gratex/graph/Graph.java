package pl.edu.agh.gratex.graph;


import pl.edu.agh.gratex.gui.ControlManager;
import pl.edu.agh.gratex.model.EdgePropertyModel;
import pl.edu.agh.gratex.model.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.VertexPropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.awt.geom.Area;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;


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
        setVertices(new LinkedList<Vertex>());
        setEdges(new LinkedList<Edge>());
        setLabelsV(new LinkedList<LabelV>());
        setLabelsE(new LinkedList<LabelE>());

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

        if (ControlManager.mode == ControlManager.VERTEX_MODE) {
            Iterator<Vertex> itv = getVertices().listIterator();
            Vertex temp = null;
            while (itv.hasNext()) {
                temp = itv.next();
                Area outline = new Area(Utilities.getVertexShape(temp.getType() + 1, temp.getRadius(), temp.getPosX(), temp.getPosY()));
                outline.intersect(rect);
                if (!outline.isEmpty()) {
                    result.add(temp);
                }
            }
        } else if (ControlManager.mode == ControlManager.EDGE_MODE) {
            Iterator<Edge> ite = getEdges().listIterator();
            Edge temp = null;
            while (ite.hasNext()) {
                temp = ite.next();
                if (Utilities.checkArcRectangleIntersection(temp, area)) {
                    result.add(temp);
                }
            }
        } else if (ControlManager.mode == ControlManager.LABEL_V_MODE) {
            Iterator<LabelV> itlv = getLabelsV().listIterator();
            LabelV temp = null;
            while (itlv.hasNext()) {
                temp = itlv.next();
                if (temp.getOutline().intersects(area)) {
                    result.add(temp);
                }
            }
        } else if (ControlManager.mode == ControlManager.LABEL_E_MODE) {
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

    public LinkedList<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(LinkedList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public LinkedList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(LinkedList<Edge> edges) {
        this.edges = edges;
    }

    public LinkedList<LabelV> getLabelsV() {
        return labelsV;
    }

    public void setLabelsV(LinkedList<LabelV> labelsV) {
        this.labelsV = labelsV;
    }

    public LinkedList<LabelE> getLabelsE() {
        return labelsE;
    }

    public void setLabelsE(LinkedList<LabelE> labelsE) {
        this.labelsE = labelsE;
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
