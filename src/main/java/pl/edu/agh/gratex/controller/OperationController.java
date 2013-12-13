package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.editor2.Operation2;

public interface OperationController {
    public GeneralController getGeneralController();

    public void initOperation(Operation2 operation2, String info);

    public void reportOperationProgress(String info);

    public void finishOperation(String info);

    public Operation2 getCurrentOperation();

    public void addOperationListener(OperationListener operationListener);

    public void removeOperationListener(OperationListener operationListener);
}
