package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.labelV.LabelVUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.util.LinkedList;
import java.util.List;

public class CreationOperation extends Operation {
    GeneralController generalController;
    private GraphElementType subjectType;
    private List<String> subjects;

    public CreationOperation(GeneralController generalController,
                             List<? extends GraphElement> createdElements,
                             OperationType operationType,
                             String info) {

        this.generalController = generalController;
        setOperationType(operationType);
        setInfo(info);

        init(createdElements);
    }

    public CreationOperation(GeneralController generalController,
                             GraphElement createdElement,
                             OperationType operationType,
                             String info) {
        this.generalController = generalController;
        setOperationType(operationType);
        setInfo(info);

        List<GraphElement> createdElements = new LinkedList<>();
        createdElements.add(createdElement);
        init(createdElements);
    }

    private void init(List<? extends GraphElement> createdElements) {
        this.subjectType = createdElements.get(0).getType();
        this.subjects = new LinkedList<>();
        for (GraphElement element : createdElements) {
            subjects.add(generalController.getParseController().getParserByElementType(subjectType).parseToLatex(element));
        }
        generalController.getOperationController().finishOperation(this);
    }

    @Override
    public String doOperation() {
        for (String subject : subjects) {
            System.out.println(subject);
            try {
                GraphElement newElement = generalController.getParseController().getParserByElementType(subjectType).parseToGraph(subject, generalController.getGraph());
                switch (subjectType) {
                    case VERTEX:
                        generalController.getGraph().getVertices().add((Vertex) newElement);
                        break;
                    case EDGE:
                        generalController.getGraph().getEdges().add((Edge) newElement);
                        break;
                    case LABEL_VERTEX:
                        LabelV labelV = (LabelV) newElement;
                        labelV.getOwner().setLabel(labelV);
                        LabelVUtils.updatePosition(labelV);
                        generalController.getGraph().getLabelsV().add((LabelV) newElement);
                        break;
                    case LABEL_EDGE:
                        generalController.getGraph().getLabelsE().add((LabelE) newElement);
                        break;
                }
            } catch (Exception e) {
                generalController.criticalError("Parser error", e);
            }
        }

        return getInfo();
    }

    @Override
    public String undoOperation() {
        System.out.println("subject " + subjects.get(0));
        System.out.println("graph " + generalController.getGraph().getLabelsV().get(0).getLatexCode());
        // TODO TAM NIE MA STIRNGOW IDIOTO< TYLKO LABELE!
        System.out.println(generalController.getGraph().getLabelsV().contains(subjects.get(0)));
        return null;
    }
}
