package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.operation.AlterationOperation;
import pl.edu.agh.gratex.controller.operation.Operation;

import java.util.LinkedList;

public class OperationList {
    private LinkedList<Operation> operations;
    private int iterator;

    public OperationList() {
        operations = new LinkedList<>();
        iterator = 0;
    }

    public void addNewOperation(Operation operation) {
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
