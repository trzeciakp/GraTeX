package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;

import java.util.HashMap;
import java.util.List;


public interface OperationController {

    // This method will register an undo-redo operation
    // Should be called internally from Operation extending class
    public void registerOperation(Operation operation);

    // This method is used to report any operation, so as to refresh workspace and / or display info
    public void reportOperationEvent(Operation operation);

    public void undo();

    public void redo();

    public void addOperationListener(OperationListener operationListener);

    public void removeOperationListener(OperationListener operationListener);
}
