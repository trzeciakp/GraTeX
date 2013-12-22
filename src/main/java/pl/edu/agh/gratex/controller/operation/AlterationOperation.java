package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AlterationOperation extends Operation {
    GeneralController generalController;
    private HashMap<String, String> verticesInitialToEnd = new HashMap<>();
    private HashMap<String, String> verticesEndToInitial = new HashMap<>();
    private HashMap<String, String> edgeesInitialToEnd = new HashMap<>();
    private HashMap<String, String> edgesEndToInitial = new HashMap<>();
    private HashMap<String, String> labelsEInitialToEnd = new HashMap<>();
    private HashMap<String, String> labelsEEndToInitial = new HashMap<>();
    private HashMap<String, String> labelsVInitialToEnd = new HashMap<>();
    private HashMap<String, String> labelsVEndToInitial = new HashMap<>();

    public AlterationOperation(GeneralController generalController,
                               List<? extends GraphElement> subjects,
                               OperationType operationType,
                               String info) {
        super(info, operationType);
        this.generalController = generalController;
        init(subjects);
    }

    public AlterationOperation(GeneralController generalController,
                               GraphElement subject,
                               OperationType operationType,
                               String info) {
        super(info, operationType);
        this.generalController = generalController;
        List<GraphElement> subjects = new LinkedList<>();
        subjects.add(subject);
        init(subjects);
    }

    private void init(List<?extends GraphElement> elements)
    {
    }

    @Override
    public void doOperation() {
    }

    @Override
    public void undoOperation() {
    }
}


