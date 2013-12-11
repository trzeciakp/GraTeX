package pl.edu.agh.gratex.editor;

import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.VertexUtils;
import pl.edu.agh.gratex.view.ControlManager;
import pl.edu.agh.gratex.model.vertex.Vertex;

public class AddOperation extends Operation {
    private GraphElement element;
    private int type;

    public AddOperation(GraphElement _element) {
        type = ControlManager.mainWindow.getGeneralController().getMode().ordinal()+1;
        element = _element;
    }

    public String doOperation() {
        if (type == 1) {
            ControlManager.mainWindow.getGeneralController().getGraph().getVertices().add((Vertex) element);
            VertexUtils.setPartOfNumeration((Vertex) element, true);
            return StringLiterals.INFO_VERTEX_ADD;
        } else if (type == 2) {
            ControlManager.mainWindow.getGeneralController().getGraph().getEdges().add((Edge) element);
            return StringLiterals.INFO_EDGE_ADD;
        } else if (type == 3) {
            ControlManager.mainWindow.getGeneralController().getGraph().getLabelsV().add((LabelV) element);
            ((LabelV) element).getOwner().setLabel((LabelV) element);
            return StringLiterals.INFO_LABEL_V_ADD;
        } else {
            ControlManager.mainWindow.getGeneralController().getGraph().getLabelsE().add((LabelE) element);
            ((LabelE) element).getOwner().setLabel((LabelE) element);
            return StringLiterals.INFO_LABEL_E_ADD;
        }
    }

    public String undoOperation() {
        if (type == 1) {
            ControlManager.mainWindow.getGeneralController().getGraph().getVertices().remove(element);
            VertexUtils.setPartOfNumeration((Vertex) element, false);
            return StringLiterals.INFO_UNDO(StringLiterals.INFO_VERTEX_ADD);
        } else if (type == 2) {
            ControlManager.mainWindow.getGeneralController().getGraph().getEdges().remove(element);
            return StringLiterals.INFO_UNDO(StringLiterals.INFO_EDGE_ADD);
        } else if (type == 3) {
            ControlManager.mainWindow.getGeneralController().getGraph().getLabelsV().remove(element);
            ((LabelV) element).getOwner().setLabel(null);
            return StringLiterals.INFO_UNDO(StringLiterals.INFO_LABEL_V_ADD);
        } else {
            ControlManager.mainWindow.getGeneralController().getGraph().getLabelsE().remove(element);
            ((LabelE) element).getOwner().setLabel(null);
            return StringLiterals.INFO_UNDO(StringLiterals.INFO_LABEL_E_ADD);
        }
    }
}
