package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;

import java.util.HashMap;
import java.util.List;

// TODO TO JEST W ROZPIERDOLU!!
// TODO TO JEST W ROZPIERDOLU!!
// TODO TO JEST W ROZPIERDOLU!!
// TODO TO JEST W ROZPIERDOLU!!
// TODO TO JEST W ROZPIERDOLU!!
public class AlterationOperation extends Operation {
    GeneralController generalController;

    private OperationType operationType;
    private String info;
    private GraphElementType subjectType;

    private List<? extends GraphElement> subjects;
    private HashMap<OperationSubject, OperationSubject> initialStateToEndState;
    private HashMap<OperationSubject, OperationSubject> endStateToInitialState;

    // TODO To jeszcze nie jest potrzebne, ale mniej wiecej bedzie tak wygladac
    public AlterationOperation(GeneralController generalController, String info, GraphElementType subjectType, List<? extends GraphElement> subjects) {
        this.generalController = generalController;
        this.info = info;
        this.subjectType = subjectType;
        this.subjects = subjects;
    }

    public void finish(OperationType operationType, List<? extends GraphElement> endSubjects) {
        this.operationType = operationType;

        if (subjects == null) {
            //finishCreateOperation(endSubjects);
        } else if (endSubjects == null) {
            finishRemoveOperation();
        } else {
            //finishChangeOperation(endSubjects);
        }
    }

    public String getInfo() {
        return info;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public String doOperation() {
        for (OperationSubject initialState : initialStateToEndState.keySet()) {
            initialState.removeFromGraph();
            initialStateToEndState.get(initialState).addToGraph();
        }

        return info;
    }

    public String undoOperation() {
        for (OperationSubject endState : endStateToInitialState.keySet()) {
            endState.removeFromGraph();
            endStateToInitialState.get(endState).addToGraph();
        }

        return StringLiterals.INFO_UNDO(info);
    }

    private void finishCreateOperation(List<GraphElement> endSubjects) {
        this.initialStateToEndState = new HashMap<>();
        this.endStateToInitialState = new HashMap<>();

        for (GraphElement subject : subjects) {
            OperationSubject startSubject = new OperationSubject(subjectType,
                    generalController.getParseController().getParserByElementType(subjectType).parseToLatex(subject));

        }

        //initialStateToEndState.put(initialStates.get(i), this.endStates.get(i));
        //endStateToInitialState.put(this.endStates.get(i), initialStates.get(i));
    }


    private void finishRemoveOperation() {
    }

    private void finishChangeOperation(List<GraphElement> endSubjects) {
    }
}
