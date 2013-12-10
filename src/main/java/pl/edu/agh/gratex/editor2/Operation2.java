package pl.edu.agh.gratex.editor2;

import pl.edu.agh.gratex.constants.StringLiterals;

import java.util.HashMap;
import java.util.List;

public class Operation2 {
    private String info;
    private HashMap<OperationSubject, OperationSubject> initialStateToEndState;
    private HashMap<OperationSubject, OperationSubject> endStateToInitialState;

    public Operation2(String info, List<OperationSubject> initialStates, List<OperationSubject> endStates) {
        this.info = info;
        this.initialStateToEndState = new HashMap<>();
        this.endStateToInitialState = new HashMap<>();

        for (int i = 0; i < initialStates.size(); i++) {
            initialStateToEndState.put(initialStates.get(i), endStates.get(i));
            endStateToInitialState.put(endStates.get(i), initialStates.get(i));
        }
    }

    public String doOperation() {
        for (OperationSubject initialState : initialStateToEndState.keySet())
        {
            initialState.removeFromGraph();
            initialStateToEndState.get(initialState).addToGraph();
        }

        return info;
    }

    public String undoOperation() {
        for (OperationSubject endState : endStateToInitialState.keySet())
        {
            endState.removeFromGraph();
            endStateToInitialState.get(endState).addToGraph();
        }

        return StringLiterals.INFO_UNDO(info);
    }
}
