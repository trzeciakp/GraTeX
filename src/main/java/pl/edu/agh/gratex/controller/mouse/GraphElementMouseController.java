package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.controller.operation.GenericOperation;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *
 */
public abstract class GraphElementMouseController {
    protected int mouseX;
    protected int mouseY;
    protected boolean shiftDown;

    private GeneralController generalController;
    private GraphElementFactory graphElementFactory;

    protected GraphElementMouseController(GeneralController generalController, GraphElementFactory graphElementFactory) {
        this.generalController = generalController;
        this.graphElementFactory = graphElementFactory;
    }

    public GraphElementFactory getGraphElementFactory() {
        return graphElementFactory;
    }

    public void setMouseLocation(int x, int y)
    {
        mouseX = x;
        mouseY = y;
    }

    public void setShiftDown(boolean flag) {
        shiftDown = flag;
        shiftDownChanged();
    }

    public void shiftDownChanged() {
    }

    public abstract void reset();

    public abstract void drawCurrentlyAddedElement(Graphics2D g);

    public abstract GraphElement getElementFromPosition(int mouseX, int mouseY);

    public abstract void addNewElement(int mouseX, int mouseY);

    public void removeElement(int mouseX, int mouseY) {
        GraphElement temp = getElementFromPosition(mouseX, mouseY);
        if (temp != null) {
            ModeType mode = generalController.getModeController().getMode();
            new CreationRemovalOperation(generalController, temp, OperationType.REMOVE_OPERATION(mode),
                    StringLiterals.INFO_REMOVE_ELEMENT(mode, 1), false);
        } else {
            generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_NOTHING_TO_REMOVE));
        }
    }

    public abstract void moveSelection(int mouseX, int mouseY);

    public abstract void finishMoving();
}
