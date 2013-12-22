package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.edge.EdgeUtils;
import pl.edu.agh.gratex.model.graph.GraphUtils;
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
    private List<String> vertices = new LinkedList<>();
    private List<String> edges = new LinkedList<>();
    private List<String> labelVs = new LinkedList<>();
    private List<String> labelEs = new LinkedList<>();

    private boolean creation;

    public CreationRemovalOperation(GeneralController generalController,
                                    List<? extends GraphElement> subjects,
                                    OperationType operationType,
                                    String info, boolean creation) {
        super(info, operationType);
        this.generalController = generalController;
        this.creation = creation;
        init(subjects);
    }

    public CreationRemovalOperation(GeneralController generalController,
                                    GraphElement subject,
                                    OperationType operationType,
                                    String info, boolean creation) {
        super(info, operationType);
        this.generalController = generalController;
        this.creation = creation;
        List<GraphElement> subjects = new LinkedList<>();
        subjects.add(subject);
        init(subjects);
    }

    @Override
    public void doOperation() {
        if (creation) {
            createElements();
        } else {
            removeElements();
        }
    }

    @Override
    public void undoOperation() {
        if (creation) {
            removeElements();
        } else {
            createElements();
        }
    }

    private void init(List<? extends GraphElement> elements) {
        for (GraphElement element : elements) {
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
        generalController.getOperationController().registerOperation(this);
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
                EdgeUtils.updatePosition(edge);
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

            LabelV labelV;
            if ((labelV = vertex.getLabel()) != null) {
                if (!labelVs.contains(labelV.getLatexCode())) {
                    labelVs.add(labelV.getLatexCode());
                }
            }

            for (Edge edge : GraphUtils.getAdjacentEdges(generalController.getGraph(), vertex)) {
                if (!edges.contains(edge.getLatexCode())) {
                    edges.add(edge.getLatexCode());
                }
            }
        }

        for (String latexCode : edges) {
            Edge edge = generalController.getGraph().getEdgeByLatexCode(latexCode);
            generalController.getGraph().getEdges().remove(edge);
            LabelE labelE;
            if ((labelE = edge.getLabel()) != null) {
                if (!labelEs.contains(labelE.getLatexCode())) {
                    labelEs.add(labelE.getLatexCode());
                }
            }
        }

        for (String latexCode : labelVs) {
            LabelV labelV = generalController.getGraph().getLabelVByLatexCode(latexCode);
            generalController.getGraph().getLabelsV().remove(labelV);
        }

        for (String latexCode : labelEs) {
            LabelE labelE = generalController.getGraph().getLabelEByLatexCode(latexCode);
            generalController.getGraph().getLabelsE().remove(labelE);
        }
    }
}
