package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.controller.operation.Operation;
import pl.edu.agh.gratex.model.GraphElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface OperationController {
    public GeneralController getGeneralController();

    // TODO to  chyba nie powinno byc na zewwnatrz, ale do  testow
    public OperationList getOperationList();

    // This method will initialize an undo-redo operation
    public void initOperation(List<GraphElement> subjects, String initInfo);

    public HashMap<GraphElement, String> getOperationInitialStates();

    // This method will finalize an undo-redo operation
    // Should be called internally from Operation extending class
    public void finishOperation(Operation operation);

    // This method is used to report any operation, so as to refresh workspace and / or display info
    public void reportGenericOperation(String info);

    public void addOperationListener(OperationListener operationListener);

    public void removeOperationListener(OperationListener operationListener);
}
