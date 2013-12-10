package pl.edu.agh.gratex.editor2;

import pl.edu.agh.gratex.constants.GraphElementType;

public class OperationSubject {
    private GraphElementType graphElementType;
    private Object state;

    public OperationSubject(GraphElementType graphElementType, Object state) {
        this.graphElementType = graphElementType;
        this.state = state;
    }

    public void removeFromGraph()
    {
        // TODO Uwzglednic labele dla wierzcholkow itp
    }

    public void addToGraph()
    {
        // TODO Uwzglednic labele dla wierzcholkow itp
    }

    public GraphElementType getGraphElementType() {
        return graphElementType;
    }

    public void setGraphElementType(GraphElementType graphElementType) {
        this.graphElementType = graphElementType;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }
}
