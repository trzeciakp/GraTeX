package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
import pl.edu.agh.gratex.view.ControlManager;

import java.awt.*;
import java.util.List;
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

    public CopyPasteOperation(List<GraphElement> selectedVertices) {
        vertices = new LinkedList<Vertex>();
        edges = new LinkedList<Edge>();
        labelsV = new LinkedList<LabelV>();
        labelsE = new LinkedList<LabelE>();

        Iterator<GraphElement> it = (new LinkedList<GraphElement>(selectedVertices)).listIterator();
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

        Iterator<Edge> ite = ControlManager.mainWindow.getGeneralController().getGraph().getEdges().listIterator();
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
            itv = ControlManager.mainWindow.getGeneralController().getGraph().getVertices().listIterator();
            while (itv.hasNext()) {

                if (VertexUtils.collides(tempVD, itv.next())) {
                    return false;
                }
            }
            if (!VertexUtils.fitsIntoPage(tempVD)) {
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
        ControlManager.mainWindow.getGeneralController().getGraph().getVertices().addAll(vertices);
        ControlManager.mainWindow.getGeneralController().getGraph().getEdges().addAll(edges);
        ControlManager.mainWindow.getGeneralController().getGraph().getLabelsV().addAll(labelsV);
        ControlManager.mainWindow.getGeneralController().getGraph().getLabelsE().addAll(labelsE);

        Collections.sort(vertices, new VertexOrderComparator());
        Iterator<Vertex> itv = vertices.listIterator();
        Vertex tempV = null;
        while (itv.hasNext()) {
            tempV = itv.next();
            VertexUtils.updateNumber(tempV, ControlManager.mainWindow.getGeneralController().getGraph().getGraphNumeration().getNextFreeNumber());
            VertexUtils.setPartOfNumeration(tempV, true);
            tempV.setLabelInside((ControlManager.mainWindow.getGeneralController().getGraph().getVertexDefaultModel().labelInside == 1));
            if (ControlManager.mainWindow.getGeneralController().getGraph().gridOn) {
                VertexUtils.adjustToGrid(tempV);
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

        return StringLiterals.INFO_SUBGRAPH_PASTE;
    }

    public String undoOperation() {
        ControlManager.mainWindow.getGeneralController().getGraph().getVertices().removeAll(vertices);
        ControlManager.mainWindow.getGeneralController().getGraph().getEdges().removeAll(edges);
        ControlManager.mainWindow.getGeneralController().getGraph().getLabelsV().removeAll(labelsV);
        ControlManager.mainWindow.getGeneralController().getGraph().getLabelsE().removeAll(labelsE);

        Iterator<Vertex> itv = vertices.listIterator();
        while (itv.hasNext()) {
            VertexUtils.setPartOfNumeration(itv.next(), false);
        }

        return StringLiterals.INFO_UNDO(StringLiterals.INFO_SUBGRAPH_PASTE);
    }
}
