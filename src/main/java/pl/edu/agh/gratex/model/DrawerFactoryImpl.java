package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.controller.SelectionController;
import pl.edu.agh.gratex.model.boundary.BoundaryDrawer;
import pl.edu.agh.gratex.model.edge.DummyEdgeDrawer;
import pl.edu.agh.gratex.model.edge.EdgeDrawer;
import pl.edu.agh.gratex.model.hyperedge.HyperedgeDrawer;
import pl.edu.agh.gratex.model.labelE.LabelEdgeDrawer;
import pl.edu.agh.gratex.model.labelV.LabelVertexDrawer;
import pl.edu.agh.gratex.model.vertex.VertexDrawer;

import java.util.EnumMap;

/**
 *
 */
public class DrawerFactoryImpl implements DrawerFactory {
    private EnumMap<GraphElementType, Drawer> defaultDrawable = new EnumMap<>(GraphElementType.class);
    private DummyEdgeDrawer dummyEdgeDrawable;

    public DrawerFactoryImpl(SelectionController selectionController) {
        defaultDrawable.put(GraphElementType.VERTEX, new VertexDrawer(selectionController));
        defaultDrawable.put(GraphElementType.LABEL_VERTEX, new LabelVertexDrawer(selectionController));
        defaultDrawable.put(GraphElementType.EDGE, new EdgeDrawer(selectionController));
        defaultDrawable.put(GraphElementType.LABEL_EDGE, new LabelEdgeDrawer(selectionController));
        defaultDrawable.put(GraphElementType.BOUNDARY, new BoundaryDrawer(selectionController));
        defaultDrawable.put(GraphElementType.HYPEREDGE, new HyperedgeDrawer(selectionController));
        dummyEdgeDrawable = new DummyEdgeDrawer(defaultDrawable.get(GraphElementType.EDGE));
    }

    @Override
    public Drawer createDefaultDrawer(GraphElementType type) {
        return defaultDrawable.get(type);
    }

    @Override
    public Drawer createDummyEdgeDrawer() {
        return dummyEdgeDrawable;
    }
}
