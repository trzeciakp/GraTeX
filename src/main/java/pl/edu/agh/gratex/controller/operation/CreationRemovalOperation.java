package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelE.LabelEUtils;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.labelV.LabelVUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.util.LinkedList;
import java.util.List;

public class CreationRemovalOperation extends Operation {
    GeneralController generalController;
    private List<String> vertices;
    private List<String> edges;
    private List<String> labelVs;
    private List<String> labelEs;

    private boolean creation;

    public CreationRemovalOperation(GeneralController generalController,
                                    List<? extends GraphElement> createdElements,
                                    OperationType operationType,
                                    String info, boolean creation) {
        this.generalController = generalController;
        this.creation = creation;
        setOperationType(operationType);
        setInfo(info);
        init(createdElements);
    }

    public CreationRemovalOperation(GeneralController generalController,
                                    GraphElement createdElement,
                                    OperationType operationType,
                                    String info, boolean creation) {
        this.generalController = generalController;
        this.creation = creation;
        setOperationType(operationType);
        setInfo(info);
        List<GraphElement> createdElements = new LinkedList<>();
        createdElements.add(createdElement);
        init(createdElements);
    }

    @Override
    public String doOperation() {
        if (creation) {
            createElements();
        } else {
            removeElements();
        }
        return getInfo();
    }

    @Override
    public String undoOperation() {
        if (creation) {
            removeElements();
        } else {
            createElements();
        }
        return getInfo();
    }

    private void init(List<? extends GraphElement> createdElements) {
        this.vertices = new LinkedList<>();
        this.edges = new LinkedList<>();
        this.labelVs = new LinkedList<>();
        this.labelEs = new LinkedList<>();
        for (GraphElement element : createdElements) {
            String latexCode = generalController.getParseController().getParserByElementType(element.getType()).parseToLatex(element);
            switch (element.getType()) {
                case VERTEX:
                    vertices.add(latexCode);
                    break;
                case EDGE:
                    edges.add(latexCode);
                    break;
                case LABEL_VERTEX:
                    labelVs.add(latexCode);
                    break;
                case LABEL_EDGE:
                    labelEs.add(latexCode);
                    break;
            }
        }
        generalController.getOperationController().finishOperation(this);
    }

    private void createElements() {
        generalController.getSelectionController().clearSelection();
        try {
            for (String latexCode : vertices) {
                Vertex vertex = (Vertex) generalController.getParseController().getParserByElementType(GraphElementType.VERTEX).
                        parseToGraph(latexCode, generalController.getGraph());
                generalController.getGraph().getVertices().add(vertex);
                VertexUtils.setPartOfNumeration(vertex, true);
                if (generalController.getModeController().getMode() == ModeType.VERTEX) {
                    generalController.getSelectionController().addToSelection(vertex, true);
                }
            }

            for (String latexCode : edges) {
                Edge edge = (Edge) generalController.getParseController().getParserByElementType(GraphElementType.EDGE).
                        parseToGraph(latexCode, generalController.getGraph());
                generalController.getGraph().getEdges().add(edge);
                if (generalController.getModeController().getMode() == ModeType.EDGE) {
                    generalController.getSelectionController().addToSelection(edge, true);
                }
            }

            for (String latexCode : labelVs) {
                LabelV labelV = (LabelV) generalController.getParseController().getParserByElementType(GraphElementType.LABEL_VERTEX).
                        parseToGraph(latexCode, generalController.getGraph());
                labelV.getOwner().setLabel(labelV);
                LabelVUtils.updatePosition(labelV);
                generalController.getGraph().getLabelsV().add(labelV);
                if (generalController.getModeController().getMode() == ModeType.LABEL_VERTEX) {
                    generalController.getSelectionController().addToSelection(labelV, true);
                }
            }

            for (String latexCode : labelEs) {
                LabelE labelE = (LabelE) generalController.getParseController().getParserByElementType(GraphElementType.LABEL_EDGE).
                        parseToGraph(latexCode, generalController.getGraph());
                labelE.getOwner().setLabel(labelE);
                LabelEUtils.updatePosition(labelE);
                generalController.getGraph().getLabelsE().add(labelE);
                if (generalController.getModeController().getMode() == ModeType.LABEL_EDGE) {
                    generalController.getSelectionController().addToSelection(labelE, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            generalController.criticalError("Parser error", e);
        }
    }

    private void removeElements() {
        for (String latexCode : vertices) {
            Vertex vertex = generalController.getGraph().getVertexByLatexCode(latexCode);
            generalController.getGraph().getVertices().remove(vertex);
            VertexUtils.setPartOfNumeration(vertex, false);
        }

        for (String latexCode : edges) {
            Edge edge = generalController.getGraph().getEdgeByLatexCode(latexCode);
            generalController.getGraph().getEdges().remove(edge);
            LabelE labelE;
            if ((labelE = edge.getLabel()) != null)
            {
                if (!labelEs.contains(labelE.getLatexCode())){
                    labelEs.add(labelE.getLatexCode());
                }
            }
        }

        for (String latexCode : labelVs) {
            LabelV labelV = generalController.getGraph().getLabelVByLatexCode(latexCode);
            labelV.getOwner().setLabel(null);
            generalController.getGraph().getLabelsV().remove(labelV);
        }

        for (String latexCode : labelEs) {
            LabelE labelE = generalController.getGraph().getLabelEByLatexCode(latexCode);
            labelE.getOwner().setLabel(null);
            generalController.getGraph().getLabelsE().remove(labelE);
        }
    }
}
