package pl.edu.agh.gratex.editor;

import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

public class AddOperation extends Operation {
    private GeneralController generalController;

    private GraphElement element;
    private int type;

    public AddOperation(GeneralController generalController, GraphElement element) {
        this.generalController = generalController;
        this.element = element;
        type = generalController.getModeController().getMode().ordinal()+1;
    }

    public String doOperation() {
        if (type == 1) {
            generalController.getGraph().getVertices().add((Vertex) element);
            VertexUtils.setPartOfNumeration((Vertex) element, true);
            return StringLiterals.INFO_VERTEX_ADD;
        } else if (type == 2) {
            generalController.getGraph().getEdges().add((Edge) element);
            return StringLiterals.INFO_EDGE_ADD;
        } else if (type == 3) {
            generalController.getGraph().getLabelsV().add((LabelV) element);
            ((LabelV) element).getOwner().setLabel((LabelV) element);
            return StringLiterals.INFO_LABEL_V_ADD;
        } else {
            generalController.getGraph().getLabelsE().add((LabelE) element);
            ((LabelE) element).getOwner().setLabel((LabelE) element);
            return StringLiterals.INFO_LABEL_E_ADD;
        }
    }

    public String undoOperation() {
        if (type == 1) {
            generalController.getGraph().getVertices().remove(element);
            VertexUtils.setPartOfNumeration((Vertex) element, false);
            return StringLiterals.INFO_UNDO(StringLiterals.INFO_VERTEX_ADD);
        } else if (type == 2) {
            generalController.getGraph().getEdges().remove(element);
            return StringLiterals.INFO_UNDO(StringLiterals.INFO_EDGE_ADD);
        } else if (type == 3) {
            generalController.getGraph().getLabelsV().remove(element);
            ((LabelV) element).getOwner().setLabel(null);
            return StringLiterals.INFO_UNDO(StringLiterals.INFO_LABEL_V_ADD);
        } else {
            generalController.getGraph().getLabelsE().remove(element);
            ((LabelE) element).getOwner().setLabel(null);
            return StringLiterals.INFO_UNDO(StringLiterals.INFO_LABEL_E_ADD);
        }
    }
}
