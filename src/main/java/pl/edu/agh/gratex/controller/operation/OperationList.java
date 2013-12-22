package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.operation.AlterationOperation;
import pl.edu.agh.gratex.controller.operation.GenericOperation;
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

    public Operation undo() {
        if (iterator < 1) {
            return new GenericOperation(StringLiterals.INFO_NOTHING_TO_UNDO);
        } else {
            Operation operation = operations.get(--iterator);
            operation.undoOperation();
            return new GenericOperation(StringLiterals.INFO_UNDO(operation.getInfo()));
        }
    }

    public Operation redo() {
        if (iterator == operations.size()) {
            return new GenericOperation(StringLiterals.INFO_NOTHING_TO_REDO);
        } else {
            Operation operation = operations.get(iterator++);
            operation.doOperation();
            return new GenericOperation(StringLiterals.INFO_REDO(operation.getInfo()));
        }

    }
}
