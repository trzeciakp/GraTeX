package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.editor2.Operation2;

import java.util.ArrayList;

public class OperationControllerImpl implements OperationController {
    private GeneralController generalController;
    private Operation2 currentOperation2;
    private ArrayList<OperationListener> listeners = new ArrayList<>();

    public OperationControllerImpl(GeneralController generalController) {
        this.generalController = generalController;
    }

    @Override
    public GeneralController getGeneralController() {
        return generalController;
    }

    @Override
    public void initOperation(Operation2 operation2, String info) {
        currentOperation2 = operation2;
        for (OperationListener listener : listeners)
        {
            listener.operationStarted(info);
        }
    }

    @Override
    public void reportOperationProgress(String info) {
        for (OperationListener listener : listeners)
        {
            listener.operationInProgress(info);
        }
    }

    @Override
    public void finishOperation(String info) {
        for (OperationListener listener : listeners)
        {
            listener.operationFinished(info);
        }
    }

    @Override
    public Operation2 getCurrentOperation() {
        return currentOperation2;
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
