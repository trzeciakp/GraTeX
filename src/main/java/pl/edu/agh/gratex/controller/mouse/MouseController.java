package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.model.GraphElement;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

public interface MouseController {
    public void processMouseClicking(MouseEvent e);

    public void processMousePressing(MouseEvent e);

    public void processMouseReleasing(MouseEvent e);

    public void processMouseMoving(MouseEvent e);

    public void processMouseDragging(MouseEvent e);

    public void processShiftPressing(boolean flag);

    public void processCtrlPressing(boolean flag);

    public Rectangle getSelectionArea();

    public List<GraphElement> getCurrentlyAddedElements();

    public List<GraphElement> getCopiedSubgraph();

    public Point locationOfCannotCopyIcon();

    public void duplicateSubgraph();

    public void cancelCurrentOperation();
}
