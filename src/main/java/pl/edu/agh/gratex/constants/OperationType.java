package pl.edu.agh.gratex.constants;

public enum OperationType {
    GENERIC,

    ADD_VERTEX,
    ADD_EDGE,
    ADD_LABEL_VERTEX,
    ADD_LABEL_EDGE,
    ADD_HYPEREDGE,
    ADD_BOUNDARY,
    ADD_LINK_BOUNDARY,

    MOVE_VERTEX,
    MOVE_EDGE,
    MOVE_LABEL_VERTEX,
    MOVE_LABEL_EDGE,
    MOVE_HYPEREDGE,
    MOVE_BOUNDARY,
    MOVE_LINK_BOUNDARY,

    REMOVE_VERTEX,
    REMOVE_EDGE,
    REMOVE_LABEL_VERTEX,
    REMOVE_LABEL_EDGE,
    REMOVE_HYPEREDGE,
    REMOVE_BOUNDARY,
    REMOVE_LINK_BOUNDARY,

    SHRINK_HYPEREDGE,
    EXTEND_HYPEREDGE,

    DUPLICATION,
    PROPERTY_CHANGE,
    TEMPLATE_GLOBAL_APPLY,
    GRID_TOGGLE,
    ADJUST_ELEMENTS_TO_GRID;

    public static OperationType REMOVE_OPERATION(ModeType mode)
    {
        switch (mode) {
            case VERTEX:
                return REMOVE_VERTEX;
            case EDGE:
                return REMOVE_EDGE;
            case LABEL_VERTEX:
                return REMOVE_LABEL_VERTEX;
            case LABEL_EDGE:
                return REMOVE_LABEL_EDGE;
            case HYPEREDGE:
                return REMOVE_HYPEREDGE;
            case BOUNDARY:
                return REMOVE_BOUNDARY;
            case LINK_BOUNDARY:
                return REMOVE_LINK_BOUNDARY;
            default:
                return null;
        }
    }
}
