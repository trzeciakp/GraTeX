package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.graph.*;
import pl.edu.agh.gratex.gui.ControlManager;

import java.util.Iterator;
import java.util.LinkedList;

public class RemoveOperation extends Operation {
    private LinkedList<GraphElement> elements;
    private int type;
    private LinkedList<Edge> connectedEdges;

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
        String result = null;
        if (type == 1) {
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
                result = elements.size() + " vertices removed";
            } else {
                connectedEdges = ControlManager.graph.getAdjacentEdges((Vertex) elements.get(0));
                ControlManager.graph.getEdges().removeAll(connectedEdges);
                ControlManager.graph.getVertices().remove((Vertex) elements.get(0));
                ((Vertex) elements.get(0)).setPartOfNumeration(false);
                result = "Vertex removed";
            }
        } else if (type == 2) {
            ControlManager.graph.getEdges().removeAll(elements);
            if (elements.size() > 1) {
                result = elements.size() + " edges removed";
            } else {
                result = "Edge removed";
            }
        } else if (type == 3) {
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                while (it.hasNext()) {
                    ((LabelV) it.next()).getOwner().setLabel(null);
                }
                result = elements.size() + " labels removed";
            } else {
                ((LabelV) elements.getFirst()).getOwner().setLabel(null);
                result = "Label removed";
            }
            ControlManager.graph.getLabelsV().removeAll(elements);
        } else {
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                while (it.hasNext()) {
                    ((LabelE) it.next()).getOwner().setLabel(null);
                }
                result = elements.size() + " labels removed";
            } else {
                ((LabelE) elements.getFirst()).getOwner().setLabel(null);
                result = "Label removed";
            }
            ControlManager.graph.getLabelsV().removeAll(elements);
        }

        return result;
    }

    public String undoOperation() {
        String result = null;

        if (type == 1) {
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                Vertex temp = null;
                while (it.hasNext()) {
                    temp = (Vertex) it.next();
                    ControlManager.graph.getVertices().add(temp);
                    temp.setPartOfNumeration(true);
                }
                result = "Removing multiple vertices undone";
            } else {
                ControlManager.graph.getVertices().add((Vertex) elements.get(0));
                ((Vertex) elements.get(0)).setPartOfNumeration(true);
                result = "Removing vertex undone";
            }
            ControlManager.graph.getEdges().addAll(connectedEdges);
        } else if (type == 2) {
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                while (it.hasNext()) {
                    ControlManager.graph.getEdges().add((Edge) it.next());
                }
                result = "Removing multiple edges undone";
            } else {
                ControlManager.graph.getEdges().add((Edge) elements.get(0));
                result = "Removing edge undone";
            }
        } else if (type == 3) {
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                LabelV temp = null;
                while (it.hasNext()) {
                    temp = (LabelV) it.next();
                    ControlManager.graph.getLabelsV().add(temp);
                    temp.getOwner().setLabel(temp);
                }
                result = "Removing multiple labels undone";
            } else {
                ControlManager.graph.getLabelsV().add((LabelV) elements.get(0));
                ((LabelV) elements.getFirst()).getOwner().setLabel((LabelV) elements.getFirst());
                result = "Removing label undone";
            }
        } else {
            if (elements.size() > 1) {
                Iterator<GraphElement> it = elements.listIterator();
                LabelE temp = null;
                while (it.hasNext()) {
                    temp = (LabelE) it.next();
                    ControlManager.graph.getLabelsE().add(temp);
                    temp.getOwner().setLabel(temp);
                }
                result = "Removing multiple labels undone";
            } else {
                ControlManager.graph.getLabelsE().add((LabelE) elements.get(0));
                ((LabelE) elements.getFirst()).getOwner().setLabel((LabelE) elements.getFirst());
                result = "Removing label undone";
            }
        }
        return result;
    }
}