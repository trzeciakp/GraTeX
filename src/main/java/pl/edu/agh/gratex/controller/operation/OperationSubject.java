package pl.edu.agh.gratex.controller.operation;

import pl.edu.agh.gratex.constants.GraphElementType;

public class OperationSubject {
    public static OperationSubject DUMMY = new OperationSubject(null, null);

    private GraphElementType graphElementType;
    private String state;

    public OperationSubject(GraphElementType graphElementType, String state) {
        this.graphElementType = graphElementType;
        this.state = state;
    }

    public void removeFromGraph() {
        // TODO Uwzglednic labele dla wierzcholkow itp
    }

    public void addToGraph() {
        // TODO Uwzglednic labele dla wierzcholkow itp
    }

    public GraphElementType getGraphElementType() {
        return graphElementType;
    }

    public void setGraphElementType(GraphElementType graphElementType) {
        this.graphElementType = graphElementType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
