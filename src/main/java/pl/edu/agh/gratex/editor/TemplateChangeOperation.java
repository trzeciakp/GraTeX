package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;

import java.util.LinkedList;


public class TemplateChangeOperation extends OldOperation {
    private GeneralController generalController;

    private boolean applyToAll;

    private VertexPropertyModel vertexStartModel;
    private EdgePropertyModel edgeStartModel;
    private LabelVertexPropertyModel labelVStartModel;
    private LabelEdgePropertyModel labelEStartModel;

    private VertexPropertyModel vertexEndModel;
    private EdgePropertyModel edgeEndModel;
    private LabelVertexPropertyModel labelVEndModel;
    private LabelEdgePropertyModel labelEEndModel;

    private PropertyChangeOperation vertexChange = null;
    private PropertyChangeOperation edgeChange = null;
    private PropertyChangeOperation labelVChange = null;
    private PropertyChangeOperation labelEChange = null;

    public TemplateChangeOperation(GeneralController generalController, Graph initialGraphState, Graph endGraphState) {
        this.generalController = generalController;

        vertexStartModel = initialGraphState.getVertexDefaultModel();
        edgeStartModel = initialGraphState.getEdgeDefaultModel();
        labelVStartModel = initialGraphState.getLabelVDefaultModel();
        labelEStartModel = initialGraphState.getLabelEDefaultModel();

        vertexEndModel = endGraphState.getVertexDefaultModel();
        edgeEndModel = endGraphState.getEdgeDefaultModel();
        labelVEndModel = endGraphState.getLabelVDefaultModel();
        labelEEndModel = endGraphState.getLabelEDefaultModel();
        applyToAll = endGraphState.gridOn;
    }

    public String doOperation() {
        generalController.getGraph().setVertexDefaultModel(vertexEndModel);
        generalController.getGraph().setEdgeDefaultModel(edgeEndModel);
        generalController.getGraph().setLabelVDefaultModel(labelVEndModel);
        generalController.getGraph().setLabelEDefaultModel(labelEEndModel);

        if (applyToAll) {
            if (generalController.getGraph().getVertices().size() > 0) {
                vertexChange = new PropertyChangeOperation(new LinkedList<GraphElement>(generalController.getGraph().getVertices()), -1);
                vertexChange.setEndModel(vertexEndModel);
                vertexChange.doOperation();
            }

            if (generalController.getGraph().getEdges().size() > 0) {
                edgeChange = new PropertyChangeOperation(new LinkedList<GraphElement>(generalController.getGraph().getEdges()), -1);
                edgeChange.setEndModel(edgeEndModel);
                edgeChange.doOperation();
            }

            if (generalController.getGraph().getLabelsV().size() > 0) {
                labelVChange = new PropertyChangeOperation(new LinkedList<GraphElement>(generalController.getGraph().getLabelsV()), -1);
                labelVChange.setEndModel(labelVEndModel);
                labelVChange.doOperation();
            }

            if (generalController.getGraph().getLabelsE().size() > 0) {
                labelEChange = new PropertyChangeOperation(new LinkedList<GraphElement>(generalController.getGraph().getLabelsE()), -1);
                labelEChange.setEndModel(labelEEndModel);
                labelEChange.doOperation();
            }
        }

        if (applyToAll) {
            return StringLiterals.INFO_TEMPLATE_CHANGE_AND_GLOBAL_APPLY;
        } else {
            return StringLiterals.INFO_TEMPLATE_CHANGE;
        }
    }

    public String undoOperation() {
        generalController.getGraph().setVertexDefaultModel(vertexStartModel);
        generalController.getGraph().setEdgeDefaultModel(edgeStartModel);
        generalController.getGraph().setLabelVDefaultModel(labelVStartModel);
        generalController.getGraph().setLabelEDefaultModel(labelEStartModel);

        if (applyToAll) {
            if (vertexChange != null) {
                vertexChange.undoOperation();
            }
            if (edgeChange != null) {
                edgeChange.undoOperation();
            }
            if (labelVChange != null) {
                labelVChange.undoOperation();
            }
            if (labelEChange != null) {
                labelEChange.undoOperation();
            }
        }

        if (applyToAll) {
            return StringLiterals.INFO_UNDO(StringLiterals.INFO_TEMPLATE_CHANGE_AND_GLOBAL_APPLY);
        } else {
            return StringLiterals.INFO_UNDO(StringLiterals.INFO_TEMPLATE_CHANGE);
        }
    }
}
