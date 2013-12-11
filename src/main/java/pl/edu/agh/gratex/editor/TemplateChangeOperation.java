package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.view.ControlManager;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;

import java.util.LinkedList;


public class TemplateChangeOperation extends Operation {
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

    public TemplateChangeOperation(Graph initialGraphState, Graph endGraphState) {
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
        ControlManager.mainWindow.getGeneralController().getGraph().setVertexDefaultModel(vertexEndModel);
        ControlManager.mainWindow.getGeneralController().getGraph().setEdgeDefaultModel(edgeEndModel);
        ControlManager.mainWindow.getGeneralController().getGraph().setLabelVDefaultModel(labelVEndModel);
        ControlManager.mainWindow.getGeneralController().getGraph().setLabelEDefaultModel(labelEEndModel);

        if (applyToAll) {
            if (ControlManager.mainWindow.getGeneralController().getGraph().getVertices().size() > 0) {
                vertexChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.mainWindow.getGeneralController().getGraph().getVertices()), -1);
                vertexChange.setEndModel(vertexEndModel);
                vertexChange.doOperation();
            }

            if (ControlManager.mainWindow.getGeneralController().getGraph().getEdges().size() > 0) {
                edgeChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.mainWindow.getGeneralController().getGraph().getEdges()), -1);
                edgeChange.setEndModel(edgeEndModel);
                edgeChange.doOperation();
            }

            if (ControlManager.mainWindow.getGeneralController().getGraph().getLabelsV().size() > 0) {
                labelVChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.mainWindow.getGeneralController().getGraph().getLabelsV()), -1);
                labelVChange.setEndModel(labelVEndModel);
                labelVChange.doOperation();
            }

            if (ControlManager.mainWindow.getGeneralController().getGraph().getLabelsE().size() > 0) {
                labelEChange = new PropertyChangeOperation(new LinkedList<GraphElement>(ControlManager.mainWindow.getGeneralController().getGraph().getLabelsE()), -1);
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
        ControlManager.mainWindow.getGeneralController().getGraph().setVertexDefaultModel(vertexStartModel);
        ControlManager.mainWindow.getGeneralController().getGraph().setEdgeDefaultModel(edgeStartModel);
        ControlManager.mainWindow.getGeneralController().getGraph().setLabelVDefaultModel(labelVStartModel);
        ControlManager.mainWindow.getGeneralController().getGraph().setLabelEDefaultModel(labelEStartModel);

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
