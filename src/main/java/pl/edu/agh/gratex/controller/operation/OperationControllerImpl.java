package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.controller.GeneralController;

import java.util.ArrayList;

public class OperationControllerImpl implements OperationController {
    private GeneralController generalController;
    private ArrayList<OperationListener> listeners = new ArrayList<>();

    OperationList operationList = new OperationList();

    public OperationControllerImpl(GeneralController generalController) {
        this.generalController = generalController;
    }

    @Override
    public GeneralController getGeneralController() {
        return generalController;
    }

    @Override
    public void registerOperation(Operation operation) {
        operationList.addNewOperation(operation);
        operationList.redo();
        for (OperationListener listener : listeners) {
            listener.operationEvent(operation);
        }
    }

    @Override
    public void reportOperationEvent(Operation operation) {
        for (OperationListener listener : listeners) {
            listener.operationEvent(operation);
        }
    }

    @Override
    public void undo() {
        reportOperationEvent(operationList.undo());
    }

    @Override
    public void redo() {
        reportOperationEvent(operationList.redo());
    }

    @Override
    public void addOperationListener(OperationListener operationListener) {
        listeners.add(operationListener);
    }

    @Override
    public void removeOperationListener(OperationListener operationListener) {
        listeners.remove(operationListener);
    }
}
