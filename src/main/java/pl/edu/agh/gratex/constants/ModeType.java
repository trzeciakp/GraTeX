package pl.edu.agh.gratex.constants;

public enum ModeType {
    VERTEX("VERTEX", GraphElementType.VERTEX),
    EDGE("EDGE", GraphElementType.EDGE),
    LABEL_VERTEX("LABEL (vertex)", GraphElementType.LABEL_VERTEX),
    LABEL_EDGE("LABEL (edge)", GraphElementType.LABEL_EDGE),
    HYPEREDGE("HYPEREDGE", GraphElementType.HYPEREDGE),
    BOUNDARY("BOUNDARY", GraphElementType.BOUNDARY),
    LINK_BOUNDARY("LINK (boundary)", GraphElementType.LINK_BOUNDARY);

    private final String name;
    private final GraphElementType relatedElementType;

    private ModeType(String name, GraphElementType relatedElementType) {
        this.name = name;
        this.relatedElementType = relatedElementType;
    }

    @Override
    public String toString() {
        return name;
    }

    public GraphElementType getRelatedElementType() {
        return relatedElementType;
    }
}
