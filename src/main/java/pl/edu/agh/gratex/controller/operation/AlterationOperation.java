package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AlterationOperation extends Operation {
    GeneralController generalController;

    private HashMap<String, String> initialToEndStates = new HashMap<>();
    private HashMap<String, String> endToInitialStates = new HashMap<>();

    public AlterationOperation(GeneralController generalController,
                               List<? extends GraphElement> subjects,
                               OperationType operationType,
                               String info) {
        super(info, operationType);
        this.generalController = generalController;
        init(subjects);
    }

    public AlterationOperation(GeneralController generalController,
                               GraphElement subject,
                               OperationType operationType,
                               String info) {
        super(info, operationType);
        this.generalController = generalController;
        List<GraphElement> subjects = new LinkedList<>();
        subjects.add(subject);
        init(subjects);
    }

    private void init(List<? extends GraphElement> elements) {
        for (GraphElement element : elements) {
            initialToEndStates.put(generalController.getParseController().getParserByElementType(element.getType()).parseToLatex(element), null);
        }
    }

    public void finish() {
        int numberOfChanges = 0;
        for (String initialState : new LinkedList<>(initialToEndStates.keySet())) {
            GraphElement element = generalController.getGraph().getElementByLatexCode(initialState);
            String endState = generalController.getParseController().getParserByElementType(element.getType()).parseToLatex(element);
            if (!initialState.equals(endState)) {
                initialToEndStates.put(initialState, endState);
                endToInitialStates.put(endState, initialState);
                numberOfChanges++;
            } else {
                initialToEndStates.remove(initialState);
            }
        }

        if (numberOfChanges > 0) {
            generalController.getOperationController().registerOperation(this);
        }
    }

    @Override
    public void doOperation() {
        morphElements(initialToEndStates);
    }

    @Override
    public void undoOperation() {
        morphElements(endToInitialStates);
    }

    private void morphElements(HashMap<String, String> mapping) {
        try {
            for (String key : mapping.keySet()) {
                GraphElement element = generalController.getGraph().getElementByLatexCode(key);
                generalController.getParseController().getParserByElementType(element.getType()).
                        updateElementWithCode(element, mapping.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
            generalController.criticalError("Parser error", e);
        }
    }
}


