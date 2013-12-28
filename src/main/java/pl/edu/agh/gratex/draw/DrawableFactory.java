package pl.edu.agh.gratex.draw;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;

/**
 *
 */
public interface DrawableFactory {

    Drawable createDefaultDrawable(GraphElementType type);
    Drawable createDummyEdgeDrawable();
}
