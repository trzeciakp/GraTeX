package pl.edu.agh.gratex.controller.mouse;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;

public class LinkBoundaryMouseController extends GraphElementMouseController {

    public LinkBoundaryMouseController(GeneralController generalController, GraphElementFactory graphElementFactory, GraphElementType handledGraphElementType) {
        super(generalController, graphElementFactory, handledGraphElementType);
    }

    @Override
    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public GraphElement getCurrentlyAddedElement() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void finishMoving() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
