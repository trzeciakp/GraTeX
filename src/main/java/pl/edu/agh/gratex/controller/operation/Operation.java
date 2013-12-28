package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.controller.ParseController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;

public abstract class Operation {
    private String info;
    private OperationType operationType;

    protected Operation(String info, OperationType operationType) {
        this.info = info;
        this.operationType = operationType;
    }

    public String getInfo() {
        return info;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public String getLatexCode(GraphElement element, ParseController parseController) {
        return parseController.getParserByElementType(element.getType()).parseToLatex(element);
    }

    public GraphElement getElementByLatexCode(String code, Graph graph, ParseController parseController) {
        for (GraphElementType type : GraphElementType.values()) {
            for (GraphElement element : graph.getElements(type)) {
                if (parseController.getParserByElementType(element.getType()).parseToLatex(element).equals(code)) {
                    return element;
                }
            }
        }
        return null;
    }

    public abstract void doOperation();

    public abstract void undoOperation();
}
