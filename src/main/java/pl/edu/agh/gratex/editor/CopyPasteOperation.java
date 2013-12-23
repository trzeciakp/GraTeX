package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.awt.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CopyPasteOperation extends OldOperation {
    private GeneralController generalController;

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

    public CopyPasteOperation(GeneralController generalController, List<GraphElement> selectedVertices) {
        this.generalController = generalController;
        vertices = new LinkedList<Vertex>();
        edges = new LinkedList<Edge>();
        labelsV = new LinkedList<LabelV>();
        labelsE = new LinkedList<LabelE>();

        Iterator<GraphElement> it = (new LinkedList<GraphElement>(selectedVertices)).listIterator();
        Vertex tempV = null;
        while (it.hasNext()) {
            tempV = (Vertex) it.next();
            //vertices.add(tempV.getCopy());
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

        Iterator<Edge> ite = generalController.getGraph().getEdges().listIterator();
        Edge tempE = null;
        while (ite.hasNext()) {
            tempE = ite.next();
//            //Edge edge = tempE.getCopy(vertices);
//            if (edge != null) {
//                edges.add(edge);
//                LabelE label = edges.getLast().getLabel();
//                if (label != null) {
//                    labelsE.add(label);
//                }
//            }
        }
    }

    /*public void startPasting() {
        pasting = true;
        biasX = 0;
        biasY = 0;
    }*/

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
            itv = generalController.getGraph().getVertices().listIterator();
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
        return new CopyPasteOperation(generalController, new LinkedList<GraphElement>(vertices));
    }

    public String doOperation() {
        generalController.getGraph().getVertices().addAll(vertices);
        generalController.getGraph().getEdges().addAll(edges);
        generalController.getGraph().getLabelsV().addAll(labelsV);
        generalController.getGraph().getLabelsE().addAll(labelsE);

        Collections.sort(vertices, new VertexOrderComparator());
        Iterator<Vertex> itv = vertices.listIterator();
        Vertex tempV = null;
        while (itv.hasNext()) {
            tempV = itv.next();
            VertexUtils.updateNumber(tempV, generalController.getGraph().getGraphNumeration().getNextFreeNumber());
            VertexUtils.setPartOfNumeration(tempV, true);
            tempV.setLabelInside((generalController.getGraph().getVertexDefaultModel().labelInside == 1));
            if (generalController.getGraph().gridOn) {
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

        return StringLiterals.INFO_SUBGRAPH_DUPLICATE;
    }

    public String undoOperation() {
        generalController.getGraph().getVertices().removeAll(vertices);
        generalController.getGraph().getEdges().removeAll(edges);
        generalController.getGraph().getLabelsV().removeAll(labelsV);
        generalController.getGraph().getLabelsE().removeAll(labelsE);

        Iterator<Vertex> itv = vertices.listIterator();
        while (itv.hasNext()) {
            VertexUtils.setPartOfNumeration(itv.next(), false);
        }

        return StringLiterals.INFO_UNDO(StringLiterals.BUTTON_GENERAL_CANCEL);
    }
}
