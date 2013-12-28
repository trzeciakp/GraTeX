package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.view.Application;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

public class CreationRemovalOperation extends Operation {
    GeneralController generalController;
    private EnumMap<GraphElementType, List<String>> elements = new EnumMap<>(GraphElementType.class);

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

    // =================================
    // Internal functions

    private void init(List<? extends GraphElement> subjects) {
        for (GraphElementType type : GraphElementType.values()) {
            elements.put(type, new LinkedList<String>());
        }

        for (GraphElement element : subjects) {
            String latexCode = generalController.getParseController().getParserByElementType(element.getType()).parseToLatex(element);
            elements.get(element.getType()).add(latexCode);
        }

        generalController.getOperationController().registerOperation(this);
    }

    private void createElements() {
        generalController.getSelectionController().clearSelection();
        try {
            for (GraphElementType type : GraphElementType.values()) {
                for (String latexCode : elements.get(type)) {
                    GraphElement element = generalController.getParseController().getParserByElementType(type).
                            parseToGraph(latexCode, generalController.getGraph());
                    element.addToGraph(latexCode);
                    if (generalController.getModeController().getMode().getRelatedElementType() == element.getType()) {
                        generalController.getSelectionController().addToSelection(element, true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Application.criticalError("Parser error", e);
        }
    }

    private void removeElements() {
        for (GraphElementType type : GraphElementType.values()) {
            for (String latexCode : elements.get(type)) {
                GraphElement element = generalController.getGraph().getElementByLatexCode(latexCode);
                element.removeFromGraph();
                for (GraphElement connectedElement : element.getConnectedElements()) {
                    if (!elements.get(connectedElement.getType()).contains(connectedElement.getLatexCode())) {
                        elements.get(connectedElement.getType()).add(connectedElement.getLatexCode());
                    }
                }
            }
        }
    }
}
