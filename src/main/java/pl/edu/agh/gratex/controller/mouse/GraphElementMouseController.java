package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.editor.RemoveOperation;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.GraphUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.view.ControlManager;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 *
 */
public abstract class GraphElementMouseController {
    protected int mouseX;
    protected int mouseY;
    private GeneralController generalController;

    protected GraphElementMouseController(GeneralController generalController) {
        this.generalController = generalController;
    }

    public void processMouseMoving(ToolType toolType, MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }


    public abstract void processShiftPressing(boolean flag);

    public abstract void clear();

    public abstract void paintCurrentlyAddedElement(Graphics2D g);

    public abstract GraphElement getElementFromPosition(MouseEvent e);

    public abstract void addNewElement(MouseEvent e);

    public void removeElement(MouseEvent e) {
        GraphElement temp = getElementFromPosition(e);
        if (temp != null) {
            ControlManager.operations.addNewOperation(new RemoveOperation(generalController, temp));
            generalController.publishInfo(ControlManager.operations.redo());
        } else {
            generalController.publishInfo(StringLiterals.INFO_NOTHING_TO_REMOVE);
        }
    }

    //TODO maybe it should it be changed to handle moving whole selection not single element like it is now
    public abstract void moveSelection(MouseEvent e);

    public abstract void finishMovingElement();
}
