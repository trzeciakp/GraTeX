package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.boundary.Boundary;

import java.awt.*;

/**
 *
 */
public class BoundaryMouseController extends GraphElementMouseController {
    private Boundary currentlyAddedElement;

    public BoundaryMouseController(GeneralController generalController, GraphElementFactory graphElementFactory) {
        super(generalController, graphElementFactory);
    }

    @Override
    public void reset() {

    }

    @Override
    public void drawCurrentlyAddedElement(Graphics2D g) {

    }

    @Override
    public GraphElement getElementFromPosition(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        //getGraphElementFactory().create(GraphElementType.BOUNDARY, generalController.getGraph());
        //new Cre
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {

    }

    @Override
    public void finishMoving() {

    }
}
