package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.view.Application;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AlterationOperation extends Operation {
    GeneralController generalController;

    private HashMap<String, String> initialToEndStates = new HashMap<>();
    private HashMap<String, String> endToInitialStates = new HashMap<>();

    private HashMap<GraphElement, String> initialStates = new HashMap<>();

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

    private void init(List<? extends GraphElement> subjects) {
        for (GraphElement element : subjects) {
            initialStates.put(element, getLatexCode(element, generalController.getParseController()));
        }
    }

    public void finish() {
        for (GraphElement element : initialStates.keySet()) {
            String initialState = initialStates.get(element);
            String endState = getLatexCode(element, generalController.getParseController());
            if (!initialState.equals(endState)) {
                initialToEndStates.put(initialState, endState);
                endToInitialStates.put(endState, initialState);
                try {
                    generalController.getParseController().getParserByElementType(element.getType()).
                            updateElementWithCode(element, initialState);
                } catch (Exception e) {
                    e.printStackTrace();
                    Application.criticalError("Parser error", e);
                }
            }
        }

        if (initialToEndStates.keySet().size() > 0) {
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
                GraphElement element = getElementByLatexCode(key, generalController.getGraph(), generalController.getParseController());
                generalController.getParseController().getParserByElementType(element.getType()).
                        updateElementWithCode(element, mapping.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Application.criticalError("Parser error", e);
        }
    }
}


