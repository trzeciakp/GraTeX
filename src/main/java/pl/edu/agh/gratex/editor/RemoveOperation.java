package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.util.LinkedList;
import java.util.List;

public class RemoveOperation extends OldOperation {
    private GeneralController generalController;

    private LinkedList<GraphElement> elements;
    private ModeType type;
    private LinkedList<Edge> connectedEdges;

    public RemoveOperation(GeneralController generalController, List<GraphElement> elements) {
        this.generalController = generalController;
        this.elements = new LinkedList<>(elements);
        type = generalController.getModeController().getMode();
    }

    public RemoveOperation(GeneralController generalController, GraphElement element) {
        this.generalController = generalController;
        type = generalController.getModeController().getMode();
        elements = new LinkedList<>();
        elements.add(element);
    }

    public String doOperation() {
        if (type == ModeType.VERTEX) {
            connectedEdges = new LinkedList<>();
            for (GraphElement temp : elements) {
                Vertex vertex = (Vertex) temp;
                VertexUtils.setPartOfNumeration(vertex, false);
                connectedEdges.addAll(GraphUtils.getAdjacentEdges(generalController.getGraph(), vertex));
                generalController.getGraph().getEdges().removeAll(connectedEdges);
            }
            generalController.getGraph().getVertices().removeAll(elements);

        } else if (type == ModeType.EDGE) {
            generalController.getGraph().getEdges().removeAll(elements);

        } else if (type == ModeType.LABEL_VERTEX) {
            for (GraphElement temp : elements) {
                ((LabelV) temp).getOwner().setLabel(null);
            }
            generalController.getGraph().getLabelsV().removeAll(elements);

        } else {
            for (GraphElement temp : elements) {
                ((LabelE) temp).getOwner().setLabel(null);
            }
            generalController.getGraph().getLabelsV().removeAll(elements);
        }

        return StringLiterals.INFO_REMOVE_ELEMENT(type, elements.size());
    }

    public String undoOperation() {
        if (type == ModeType.VERTEX) {
            for (GraphElement temp : elements) {
                Vertex vertex = (Vertex) temp;
                generalController.getGraph().getVertices().add(vertex);
                VertexUtils.setPartOfNumeration(vertex, true);
            }
            generalController.getGraph().getEdges().addAll(connectedEdges);

        } else if (type == ModeType.EDGE) {
            for (GraphElement temp : elements) {
                Edge edge = (Edge) temp;
                generalController.getGraph().getEdges().add(edge);
            }

        } else if (type == ModeType.LABEL_VERTEX) {
            for (GraphElement temp : elements) {
                LabelV labelV = (LabelV) temp;
                generalController.getGraph().getLabelsV().add(labelV);
                labelV.getOwner().setLabel(labelV);
            }

        } else {
            for (GraphElement temp : elements) {
                LabelE labelE = (LabelE) temp;
                generalController.getGraph().getLabelsE().add(labelE);
                labelE.getOwner().setLabel(labelE);
            }
        }

        return StringLiterals.INFO_UNDO(StringLiterals.INFO_REMOVE_ELEMENT(type, elements.size()));
    }
}