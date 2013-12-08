package pl.edu.agh.gratex.graph;

/**
 *
 */
public enum GraphElementType {
    VERTEX("VERTEX"),
    EDGE("EDGE"),
    LABEL_VERTEX("LABEL (V)"),
    LABEL_EDGE("LABEL (E)");

    private final String description;

    GraphElementType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
