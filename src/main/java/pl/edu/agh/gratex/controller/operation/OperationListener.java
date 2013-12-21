package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.controller.operation.AlterationOperation;
import pl.edu.agh.gratex.controller.operation.Operation;
import pl.edu.agh.gratex.model.GraphElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OperationListener {
    public void initOperationEvent(HashMap<GraphElement, String> subjectStates, String info);

    public void finishOperationEvent(Operation operation);

    public void genericOperationEvent(String info);
}
