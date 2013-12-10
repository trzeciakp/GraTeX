package pl.edu.agh.gratex.editor2;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.StringLiterals;

import java.util.LinkedList;

public class OperationList2 {
    private LinkedList<Operation2> operations;
    private int iterator;

    public OperationList2() {
        operations = new LinkedList<>();
        iterator = 0;
    }

    public void addNewOperation(Operation2 operation) {
        while (iterator < operations.size()) {
            operations.removeLast();
        }
        while (operations.size() > Const.MAX_REMEMBERED_OPERATIONS) {
            operations.removeFirst();
        }
        operations.add(operation);
    }

    public String undo() {
        if (iterator < 1) {
            return StringLiterals.INFO_NOTHING_TO_UNDO;
        } else {
            return operations.get(--iterator).undoOperation();
        }
    }

    public String redo() {
        if (iterator == operations.size()) {
            return StringLiterals.INFO_NOTHING_TO_REDO;
        } else {
            return operations.get(iterator++).doOperation();
        }

    }
}
