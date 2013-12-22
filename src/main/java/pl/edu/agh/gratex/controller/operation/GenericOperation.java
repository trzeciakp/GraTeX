package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.OperationType;

public class GenericOperation extends Operation {
    public GenericOperation(String info) {
        super(info, OperationType.GENERIC);
    }

    @Override
    public void doOperation() {
    }

    @Override
    public void undoOperation() {
    }
}
