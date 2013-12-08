package pl.edu.agh.gratex.constants;

public enum GraphElementType {
    VERTEX("vertex", "vertices"),
    EDGE("edge", "edges"),
    LABEL_VERTEX("label (vertex)", "labels (vertex)"),
    LABEL_EDGE("label (edge)", "labels (edge)");

    private final String singularName;
    private final String pluralName;

    private GraphElementType(String singularName, String pluralName) {
        this.singularName = singularName;
        this.pluralName = pluralName;
    }

    public String getSingularName() {
        return singularName;
    }

    public String getPluralName() {
        return pluralName;
    }
}
