package pl.edu.agh.gratex.controller;

// TODO Zastanowic sie czy nie zrobic enuma z operacjami, stringami info do nich etc. To by bylo ladne.
public interface OperationController {
    public GeneralController getGeneralController();

    // This method will initialize an undo-redo operation
    public void startOperation(Operation operation, String startInfo);

    // This method will finalize an undo-redo operation
    public void finishOperation();

    // This method is used to report any operation, so as to refresh workspace and / or display info
    public void reportGenericOperation(String info);

    public Operation getCurrentOperation();

    public void addOperationListener(OperationListener operationListener);

    public void removeOperationListener(OperationListener operationListener);
}
