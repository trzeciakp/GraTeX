package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.graph.*;
import pl.edu.agh.gratex.gui.ControlManager;

import java.awt.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class CopyPasteOperation extends Operation {
    public LinkedList<Vertex> vertices;
    public LinkedList<Edge> edges;
    public LinkedList<LabelV> labelsV;
    public LinkedList<LabelE> labelsE;

    public int minX = 10000;
    public int minY = 10000;
    public int minNumber = 10000;

    public int targetX = 0;
    public int targetY = 0;

    public int biasX = 0;
    public int biasY = 0;

    public boolean pasting = false;

    public CopyPasteOperation(LinkedList<GraphElement> selectedVertices) {
        vertices = new LinkedList<Vertex>();
        edges = new LinkedList<Edge>();
        labelsV = new LinkedList<LabelV>();
        labelsE = new LinkedList<LabelE>();

        Iterator<GraphElement> it = selectedVertices.listIterator();
        Vertex tempV = null;
        while (it.hasNext()) {
            tempV = (Vertex) it.next();
            vertices.add(tempV.getCopy());
            if (vertices.getLast().getLabel() != null) {
                labelsV.add(vertices.getLast().getLabel());
            }

            if (tempV.getPosX() < minX) {
                minX = tempV.getPosX();
                minY = tempV.getPosY();
            }
            if (tempV.getNumber() < minNumber) {
                minNumber = tempV.getNumber();
            }
        }

        Iterator<Edge> ite = ControlManager.graph.getEdges().listIterator();
        Edge tempE = null;
        while (ite.hasNext()) {
            tempE = ite.next();
            Edge edge = tempE.getCopy(vertices);
            if (edge != null) {
                edges.add(edge);
                LabelE label = edges.getLast().getLabel();
                if (label != null) {
                    labelsE.add(label);
                }
            }
        }
    }

    public void startPasting() {
        pasting = true;
        biasX = 0;
        biasY = 0;
    }

    public void calculatePosition() {
        Iterator<Vertex> itv = vertices.listIterator();
        Vertex tempV = null;
        while (itv.hasNext()) {
            tempV = itv.next();
            tempV.setPosX(tempV.getPosX() + -biasX + targetX - minX);
            tempV.setPosY(tempV.getPosY() + -biasY + targetY - minY);
        }
        biasX = targetX - minX;
        biasY = targetY - minY;
    }

    public boolean fitsIntoPosition() {
        Iterator<Vertex> itvd = vertices.listIterator();
        Iterator<Vertex> itv = null;
        Vertex tempVD = null;

        while (itvd.hasNext()) {
            tempVD = itvd.next();
            itv = ControlManager.graph.getVertices().listIterator();
            while (itv.hasNext()) {
                if (tempVD.collides(itv.next())) {
                    return false;
                }
            }

            if (!tempVD.fitsIntoPage()) {
                return false;
            }
        }

        return true;
    }

    public void drawDummySubgraph(Graphics2D g) {
        Iterator<Edge> ite = edges.listIterator();
        while (ite.hasNext()) {
            ite.next().draw(g, true);
        }

        Iterator<Vertex> itv = vertices.listIterator();
        while (itv.hasNext()) {
            itv.next().draw(g, true);
        }

        ite = edges.listIterator();
        while (ite.hasNext()) {
            ite.next().drawLabel(g, true);
        }

        itv = vertices.listIterator();
        while (itv.hasNext()) {
            itv.next().drawLabel(g, true);
        }
    }

    public CopyPasteOperation getCopy() {
        return new CopyPasteOperation(new LinkedList<GraphElement>(vertices));
    }

    public String doOperation() {
        ControlManager.graph.getVertices().addAll(vertices);
        ControlManager.graph.getEdges().addAll(edges);
        ControlManager.graph.getLabelsV().addAll(labelsV);
        ControlManager.graph.getLabelsE().addAll(labelsE);

        Collections.sort(vertices, new VertexOrderComparator());
        Iterator<Vertex> itv = vertices.listIterator();
        Vertex tempV = null;
        while (itv.hasNext()) {
            tempV = itv.next();
            tempV.updateNumber(ControlManager.graph.getNextFreeNumber());
            tempV.setPartOfNumeration(true);
            tempV.setLabelInside((ControlManager.graph.getVertexDefaultModel().labelInside == 1));
            if (ControlManager.graph.gridOn) {
                tempV.adjustToGrid();
            }
        }

        minNumber = 10000;
        itv = vertices.listIterator();
        while (itv.hasNext()) {
            tempV = itv.next();
            if (tempV.getNumber() < minNumber) {
                minNumber = tempV.getNumber();
            }
        }

        return "Subgraph pasted";
    }

    public String undoOperation() {
        ControlManager.graph.getVertices().removeAll(vertices);
        ControlManager.graph.getEdges().removeAll(edges);
        ControlManager.graph.getLabelsV().removeAll(labelsV);
        ControlManager.graph.getLabelsE().removeAll(labelsE);

        Iterator<Vertex> itv = vertices.listIterator();
        while (itv.hasNext()) {
            itv.next().setPartOfNumeration(false);
        }

        return "Pasting subgraph undone";
    }
}
