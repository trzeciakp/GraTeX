package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.controller.operation.GenericOperation;
import pl.edu.agh.gratex.model.GraphElement;

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

    protected GraphElementMouseController(GeneralController generalController) {
        this.generalController = generalController;
    }

    public void processMouseMoving(ToolType toolType, MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    //to fix dummy vertex while moving after vertex added
    public void processMouseDragging(ToolType toolType, MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void processShiftPressing(boolean flag) {
        shiftDown = flag;
        shiftDownChanged();
    }

    public abstract void shiftDownChanged();

    public abstract void reset();

    public abstract void paintCurrentlyAddedElement(Graphics2D g);

    public abstract GraphElement getElementFromPosition(MouseEvent e);

    public abstract void addNewElement(MouseEvent e);

    public void removeElement(MouseEvent e) {
        GraphElement temp = getElementFromPosition(e);
        if (temp != null) {
            ModeType mode = generalController.getModeController().getMode();
            new CreationRemovalOperation(generalController, temp, OperationType.REMOVE_OPERATION(mode),
                    StringLiterals.INFO_REMOVE_ELEMENT(mode, 1), false);
        } else {
            generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_NOTHING_TO_REMOVE));
        }
    }

    //TODO maybe it should it be changed to handle moving whole selection not single element like it is now
    public abstract void moveSelection(MouseEvent e);

    public abstract void finishMovingElement();
}
