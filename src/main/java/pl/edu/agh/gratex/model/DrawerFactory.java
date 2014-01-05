package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;

/**
 *
 */
public interface DrawerFactory {
    Drawer createDefaultDrawer(GraphElementType type);
    Drawer createDummyEdgeDrawer();
}
