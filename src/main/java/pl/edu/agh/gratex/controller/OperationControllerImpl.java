package pl.edu.agh.gratex.controller;

import java.util.ArrayList;

public class OperationControllerImpl implements OperationController {
    private GeneralController generalController;
    private Operation currentOperation;
    private ArrayList<OperationListener> listeners = new ArrayList<>();

    public OperationControllerImpl(GeneralController generalController) {
        this.generalController = generalController;
    }

    @Override
    public GeneralController getGeneralController() {
        return generalController;
    }

    @Override
    public void startOperation(Operation operation, String startInfo) {
        currentOperation = operation;
        for (OperationListener listener : listeners)
        {
            System.out.println("listener = [" + listener + "], startInfo = [" + startInfo + "]");
            listener.startOperationEvent(startInfo);
        }
    }

    @Override
    public void finishOperation() {
        for (OperationListener listener : listeners)
        {
            listener.finishOperationEvent(currentOperation);
        }
    }

    @Override
    public void reportGenericOperation(String info) {
        for (OperationListener listener : listeners)
        {
            listener.genericOperationEvent(info);
        }
    }

    @Override
    public Operation getCurrentOperation() {
        return currentOperation;
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
