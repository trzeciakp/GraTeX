package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.editor2.Operation2;

// TODO Zastanowic sie czy nie zrobic enuma z operacjami, stringami info do nich etc. To by bylo ladne.
public interface OperationController {
    public GeneralController getGeneralController();

    // This method will initialize an undo-redo operation
    public void initOperation(Operation2 operation2, String info);

    // This method will finalize an undo-redo operation
    public void finishOperation(String info);

    // This method is used to report any operation, so as to refresh workspace and / or info display
    public void reportGenericOperation(String info);

    public Operation2 getCurrentOperation();

    public void addOperationListener(OperationListener operationListener);

    public void removeOperationListener(OperationListener operationListener);
}
