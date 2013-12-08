package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.graph.*;
import pl.edu.agh.gratex.gui.ControlManager;

import java.util.Iterator;
import java.util.LinkedList;

public class RemoveOperation extends Operation {
    private LinkedList<GraphElement> elements;
    private int type;
    private LinkedList<Edge> connectedEdges;

    // TODO Zmienic type na graphelementtype oraz usunac zbedne rzeczy z kodu ( GraphElementType graphElementType = null; i wszystko dalej)

    public RemoveOperation(LinkedList<GraphElement> _elements) {
        type = ControlManager.getMode().ordinal()+1;
        elements = new LinkedList<GraphElement>(_elements);
    }

    public RemoveOperation(GraphElement _element) {
        type = ControlManager.getMode().ordinal()+1;
        elements = new LinkedList<GraphElement>();
        elements.add(_element);
    }

    public String doOperation() {
        GraphElementType graphElementType = null;
        if (type == 1) {
            graphElementType = GraphElementType.VERTEX;
            if (elements.size() > 1) {
                connectedEdges = new LinkedList<Edge>();
                Iterator<GraphElement> it = elements.listIterator();
                Vertex temp = null;
                while (it.hasNext()) {
                    temp = (Vertex) it.next();
                    temp.setPartOfNumeration(false);
                    connectedEdges.addAll(ControlManager.graph.getAdjacentEdges(temp));
                    ControlManager.graph.getEdges().removeAll(connectedEdges);
                }
                ControlManager.graph.getVertices().removeAll(elements);
            } else {
                connectedEdges = ControlManager.graph.getAdjacentEdges((Vertex) elements.get(0));
                ControlManager.graph.getEdges().removeAll(connectedEdges);
                ControlManager.graph.getVertices().remove((Vertex) elements.get(0));
                ((Vertex) elements.get(0)).setPartOfNumeration(false);
            }
        } else if (type == 2) {
            graphElementType = GraphElementType.EDGE;
            ControlManager.graph.getEdges().removeAll(elements);
        } else if (type == 3) {
            graphElementType = GraphElementType.LABEL_VERTEX;
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                while (it.hasNext()) {
                    ((LabelV) it.next()).getOwner().setLabel(null);
                }
            } else {
                ((LabelV) elements.getFirst()).getOwner().setLabel(null);
            }
            ControlManager.graph.getLabelsV().removeAll(elements);
        } else {
            graphElementType = GraphElementType.LABEL_EDGE;
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                while (it.hasNext()) {
                    ((LabelE) it.next()).getOwner().setLabel(null);
                }
            } else {
                ((LabelE) elements.getFirst()).getOwner().setLabel(null);
            }
            ControlManager.graph.getLabelsV().removeAll(elements);
        }

        return StringLiterals.INFO_REMOVE_ELEMENT(graphElementType, elements.size());
    }

    public String undoOperation() {
        GraphElementType graphElementType = null;
        if (type == 1) {
            graphElementType = GraphElementType.VERTEX;
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                Vertex temp = null;
                while (it.hasNext()) {
                    temp = (Vertex) it.next();
                    ControlManager.graph.getVertices().add(temp);
                    temp.setPartOfNumeration(true);
                }
            } else {
                ControlManager.graph.getVertices().add((Vertex) elements.get(0));
                ((Vertex) elements.get(0)).setPartOfNumeration(true);
            }
            ControlManager.graph.getEdges().addAll(connectedEdges);
        } else if (type == 2) {
            graphElementType = GraphElementType.EDGE;
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                while (it.hasNext()) {
                    ControlManager.graph.getEdges().add((Edge) it.next());
                }
            } else {
                ControlManager.graph.getEdges().add((Edge) elements.get(0));
            }
        } else if (type == 3) {
            graphElementType = GraphElementType.LABEL_VERTEX;
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                LabelV temp = null;
                while (it.hasNext()) {
                    temp = (LabelV) it.next();
                    ControlManager.graph.getLabelsV().add(temp);
                    temp.getOwner().setLabel(temp);
                }
            } else {
                ControlManager.graph.getLabelsV().add((LabelV) elements.get(0));
                ((LabelV) elements.getFirst()).getOwner().setLabel((LabelV) elements.getFirst());
            }
        } else {
            graphElementType = GraphElementType.LABEL_EDGE;
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                LabelE temp = null;
                while (it.hasNext()) {
                    temp = (LabelE) it.next();
                    ControlManager.graph.getLabelsE().add(temp);
                    temp.getOwner().setLabel(temp);
                }
            } else {
                ControlManager.graph.getLabelsE().add((LabelE) elements.get(0));
                ((LabelE) elements.getFirst()).getOwner().setLabel((LabelE) elements.getFirst());
            }
        }
        return StringLiterals.INFO_UNDO(StringLiterals.INFO_REMOVE_ELEMENT(graphElementType, elements.size()));
    }
}