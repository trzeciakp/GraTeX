package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
import pl.edu.agh.gratex.view.ControlManager;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.util.LinkedList;
import java.util.List;

public class RemoveOperation extends Operation {
    private LinkedList<GraphElement> elements;
    private ModeType type;
    private LinkedList<Edge> connectedEdges;

    public RemoveOperation(List<GraphElement> _elements) {
        type = ControlManager.getMode();
        elements = new LinkedList<>(_elements);
    }

    public RemoveOperation(GraphElement _element) {
        type = ControlManager.getMode();
        elements = new LinkedList<>();
        elements.add(_element);
    }

    public String doOperation() {
        if (type == ModeType.VERTEX) {
            connectedEdges = new LinkedList<>();
            for (GraphElement temp : elements) {
                Vertex vertex = (Vertex) temp;
                VertexUtils.setPartOfNumeration(vertex, false);
                connectedEdges.addAll(GraphUtils.getAdjacentEdges(ControlManager.graph, vertex));
                ControlManager.graph.getEdges().removeAll(connectedEdges);
            }
            ControlManager.graph.getVertices().removeAll(elements);

        } else if (type == ModeType.EDGE) {
            ControlManager.graph.getEdges().removeAll(elements);

        } else if (type == ModeType.LABEL_VERTEX) {
            for (GraphElement temp : elements) {
                ((LabelV) temp).getOwner().setLabel(null);
            }
            ControlManager.graph.getLabelsV().removeAll(elements);

        } else {
            for (GraphElement temp : elements) {
                ((LabelE) temp).getOwner().setLabel(null);
            }
            ControlManager.graph.getLabelsV().removeAll(elements);
        }

        return StringLiterals.INFO_REMOVE_ELEMENT(type, elements.size());
    }

    public String undoOperation() {
        if (type == ModeType.VERTEX) {
            for (GraphElement temp : elements) {
                Vertex vertex = (Vertex) temp;
                ControlManager.graph.getVertices().add(vertex);
                VertexUtils.setPartOfNumeration(vertex, true);
            }
            ControlManager.graph.getEdges().addAll(connectedEdges);

        } else if (type == ModeType.EDGE) {
            for (GraphElement temp : elements) {
                Edge edge = (Edge) temp;
                ControlManager.graph.getEdges().add(edge);
            }

        } else if (type == ModeType.LABEL_VERTEX) {
            for (GraphElement temp : elements) {
                LabelV labelV = (LabelV) temp;
                ControlManager.graph.getLabelsV().add(labelV);
                labelV.getOwner().setLabel(labelV);
            }

        } else {
            for (GraphElement temp : elements) {
                LabelE labelE = (LabelE) temp;
                ControlManager.graph.getLabelsE().add(labelE);
                labelE.getOwner().setLabel(labelE);
            }
        }

        return StringLiterals.INFO_UNDO(StringLiterals.INFO_REMOVE_ELEMENT(type, elements.size()));
    }
}