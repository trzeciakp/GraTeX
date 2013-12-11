package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
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
        type = ControlManager.mainWindow.getGeneralController().getMode();
        elements = new LinkedList<>(_elements);
    }

    public RemoveOperation(GraphElement _element) {
        type = ControlManager.mainWindow.getGeneralController().getMode();
        elements = new LinkedList<>();
        elements.add(_element);
    }

    public String doOperation() {
        if (type == ModeType.VERTEX) {
            connectedEdges = new LinkedList<>();
            for (GraphElement temp : elements) {
                Vertex vertex = (Vertex) temp;
                VertexUtils.setPartOfNumeration(vertex, false);
                connectedEdges.addAll(GraphUtils.getAdjacentEdges(ControlManager.mainWindow.getGeneralController().getGraph(), vertex));
                ControlManager.mainWindow.getGeneralController().getGraph().getEdges().removeAll(connectedEdges);
            }
            ControlManager.mainWindow.getGeneralController().getGraph().getVertices().removeAll(elements);

        } else if (type == ModeType.EDGE) {
            ControlManager.mainWindow.getGeneralController().getGraph().getEdges().removeAll(elements);

        } else if (type == ModeType.LABEL_VERTEX) {
            for (GraphElement temp : elements) {
                ((LabelV) temp).getOwner().setLabel(null);
            }
            ControlManager.mainWindow.getGeneralController().getGraph().getLabelsV().removeAll(elements);

        } else {
            for (GraphElement temp : elements) {
                ((LabelE) temp).getOwner().setLabel(null);
            }
            ControlManager.mainWindow.getGeneralController().getGraph().getLabelsV().removeAll(elements);
        }

        return StringLiterals.INFO_REMOVE_ELEMENT(type, elements.size());
    }

    public String undoOperation() {
        if (type == ModeType.VERTEX) {
            for (GraphElement temp : elements) {
                Vertex vertex = (Vertex) temp;
                ControlManager.mainWindow.getGeneralController().getGraph().getVertices().add(vertex);
                VertexUtils.setPartOfNumeration(vertex, true);
            }
            ControlManager.mainWindow.getGeneralController().getGraph().getEdges().addAll(connectedEdges);

        } else if (type == ModeType.EDGE) {
            for (GraphElement temp : elements) {
                Edge edge = (Edge) temp;
                ControlManager.mainWindow.getGeneralController().getGraph().getEdges().add(edge);
            }

        } else if (type == ModeType.LABEL_VERTEX) {
            for (GraphElement temp : elements) {
                LabelV labelV = (LabelV) temp;
                ControlManager.mainWindow.getGeneralController().getGraph().getLabelsV().add(labelV);
                labelV.getOwner().setLabel(labelV);
            }

        } else {
            for (GraphElement temp : elements) {
                LabelE labelE = (LabelE) temp;
                ControlManager.mainWindow.getGeneralController().getGraph().getLabelsE().add(labelE);
                labelE.getOwner().setLabel(labelE);
            }
        }

        return StringLiterals.INFO_UNDO(StringLiterals.INFO_REMOVE_ELEMENT(type, elements.size()));
    }
}