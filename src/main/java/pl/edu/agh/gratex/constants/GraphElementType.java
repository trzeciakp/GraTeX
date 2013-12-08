package pl.edu.agh.gratex.constants;

public enum GraphElementType {
    VERTEX("VERTEX", "vertex", "vertices"),
    EDGE("EDGE", "edge", "edges"),
    LABEL_VERTEX("LABEL (vertex)", "label (vertex)", "labels (vertex)"),
    LABEL_EDGE("LABEL (edge)", "label (edge)", "labels (edge)");

    private final String description;
    private final String singularName;
    private final String pluralName;

    private GraphElementType(String description, String singularName, String pluralName) {
        this.description = description;
        this.singularName = singularName;
        this.pluralName = pluralName;
    }

    @Override
    public String toString() {
        return description;
    }

    public String getSingularName() {
        return singularName;
    }

    public String getPluralName() {
        return pluralName;
    }
}
