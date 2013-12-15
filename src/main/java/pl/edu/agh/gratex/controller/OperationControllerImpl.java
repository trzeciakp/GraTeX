package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.controller.operation.Operation;
import pl.edu.agh.gratex.model.GraphElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OperationControllerImpl implements OperationController {
    private GeneralController generalController;
    private HashMap<GraphElement, String> initialStates;
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
    public OperationList getOperationList() {
        return operationList;
    }

    @Override
    public void initOperation(List<GraphElement> subjects, String initInfo) {
        initialStates = new HashMap<>();
        for (GraphElement subject : subjects) {
            initialStates.put(subject, generalController.getParseController().getParserByElementType(subject.getType()).parseToLatex(subject));
        }

        for (OperationListener listener : listeners) {
            listener.initOperationEvent(initialStates, initInfo);
        }
    }

    @Override
    public void finishOperation(Operation operation) {
        operationList.addNewOperation(operation);
        operationList.redo();
        for (OperationListener listener : listeners) {
            listener.finishOperationEvent(operation);
        }
    }

    @Override
    public void reportGenericOperation(String info) {
        for (OperationListener listener : listeners) {
            listener.genericOperationEvent(info);
        }
    }

    @Override
    public HashMap<GraphElement, String> getOperationInitialStates() {
        return initialStates;
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
