package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;

/**
 *
 */
public class HyperedgeMouseControllerImpl extends GraphElementMouseController {
    public HyperedgeMouseControllerImpl(GeneralController generalController, GraphElementFactory graphElementFactory) {
        super(generalController, graphElementFactory);
    }

    @Override
    public void reset() {

    }

    @Override
    public GraphElement getCurrentlyAddedElement() {
        return null;
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {

    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {

    }

    @Override
    public void finishMoving() {

    }
}
