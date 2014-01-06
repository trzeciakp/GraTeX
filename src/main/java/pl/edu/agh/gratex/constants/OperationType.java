package pl.edu.agh.gratex.constants;

public enum OperationType {
    GENERIC,

    ADD_VERTEX,
    ADD_EDGE,
    ADD_LABEL_VERTEX,
    ADD_LABEL_EDGE,
    ADD_BOUNDARY,
    ADD_HYPEREDGE,

    MOVE_VERTEX,
    MOVE_EDGE,
    MOVE_LABEL_VERTEX,
    MOVE_LABEL_EDGE,
    MOVE_BOUNDARY,
    MOVE_HYPEREDGE,

    REMOVE_VERTEX,
    REMOVE_EDGE,
    REMOVE_LABEL_VERTEX,
    REMOVE_LABEL_EDGE,
    REMOVE_BOUNDARY,
    REMOVE_HYPEREDGE,

    SHRINK_HYPEREDGE,
    EXTEND_HYPEREDGE,

    DUPLICATION,
    PROPERTY_CHANGE,
    TEMPLATE_GLOBAL_APPLY;

    public static OperationType REMOVE_OPERATION(ModeType mode)
    {
        switch (mode)
        {
            case VERTEX:
                return REMOVE_VERTEX;
            case EDGE:
                return REMOVE_EDGE;
            case LABEL_VERTEX:
                return REMOVE_LABEL_VERTEX;
            case LABEL_EDGE:
                return REMOVE_LABEL_EDGE;
            case BOUNDARY:
                return REMOVE_BOUNDARY;
            default:
                return null;
        }
    }
}
