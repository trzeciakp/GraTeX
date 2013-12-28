package pl.edu.agh.gratex.draw;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.controller.SelectionController;

/**
 *
 */
public class DrawableFactoryImpl implements DrawableFactory {

    private SelectionController selectionController;

    public DrawableFactoryImpl(SelectionController selectionController) {
        this.selectionController = selectionController;
    }

    @Override
    public Drawable createDefaultDrawable(GraphElementType type) {
        switch (type) {
            case VERTEX:
                return new VertexDrawable(selectionController);
            case EDGE:
                return new EdgeDrawable(selectionController);
            case LABEL_VERTEX:
                return new LabelVertexDrawable(selectionController);
            case LABEL_EDGE:
                return new LabelEdgeDrawable(selectionController);
        }
        return null;
    }
}
