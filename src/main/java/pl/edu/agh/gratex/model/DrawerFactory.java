package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;

/**
 *
 */
public interface DrawerFactory {
    Drawer createDefaultDrawable(GraphElementType type);
    Drawer createDummyEdgeDrawable();
}
