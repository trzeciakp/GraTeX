package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.OperationType;

public abstract class Operation {
    private String info;
    private OperationType operationType;

    protected Operation(String info, OperationType operationType) {
        this.info = info;
        this.operationType = operationType;
    }

    public String getInfo() {
        return info;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public abstract void doOperation();

    public abstract void undoOperation();
}
