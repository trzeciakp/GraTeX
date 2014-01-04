package pl.edu.agh.gratex.draw;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.controller.SelectionController;

import java.util.EnumMap;

/**
 *
 */
public class DrawableFactoryImpl implements DrawableFactory {

    private SelectionController selectionController;
    private EnumMap<GraphElementType, Drawable> defaultDrawable = new EnumMap<>(GraphElementType.class);
    private DummyEdgeDrawable dummyEdgeDrawable;

    public DrawableFactoryImpl(SelectionController selectionController) {
        this.selectionController = selectionController;
        defaultDrawable.put(GraphElementType.VERTEX, new VertexDrawable(selectionController));
        defaultDrawable.put(GraphElementType.LABEL_VERTEX, new LabelVertexDrawable(selectionController));
        defaultDrawable.put(GraphElementType.EDGE, new EdgeDrawable(selectionController));
        defaultDrawable.put(GraphElementType.LABEL_EDGE, new LabelEdgeDrawable(selectionController));
        defaultDrawable.put(GraphElementType.BOUNDARY, new BoundaryDrawable(selectionController));

        dummyEdgeDrawable = new DummyEdgeDrawable(defaultDrawable.get(GraphElementType.EDGE));
    }

    @Override
    public Drawable createDefaultDrawable(GraphElementType type) {
        return defaultDrawable.get(type);
    }

    @Override
    public Drawable createDummyEdgeDrawable() {
        return dummyEdgeDrawable;
    }


}
