package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;

import java.util.HashMap;

public class Operation {
    private OperationType operationType;
    private String info;
    private HashMap<OperationSubject, OperationSubject> initialStateToEndState;
    private HashMap<OperationSubject, OperationSubject> endStateToInitialState;

    // TODO To jeszcze nie jest potrzebne, ale mniej wiecej bedzie tak wygladac
    public Operation(OperationType operationType, String info){ //, List<OperationSubject> initialStates, List<OperationSubject> endStates) {
        this.operationType = operationType;
        this.info = info;
//        this.initialStateToEndState = new HashMap<>();
//        this.endStateToInitialState = new HashMap<>();
//
//        for (int i = 0; i < initialStates.size(); i++) {
//            initialStateToEndState.put(initialStates.get(i), endStates.get(i));
//            endStateToInitialState.put(endStates.get(i), initialStates.get(i));
//        }
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
}
