package pl.edu.agh.gratex.controller.mouse;

import java.awt.*;
import java.awt.event.MouseEvent;

public interface MouseController {
    public void processMouseClicking(MouseEvent e);
    public void processMousePressing(MouseEvent e);
    public void processMouseReleasing(MouseEvent e);
    public void processMouseMoving(MouseEvent e);
    public void processMouseDragging(MouseEvent e);
    public void processShiftPressing(boolean flag);
    public void processCtrlPressing(boolean flag);

    public Rectangle getSelectionArea();

    public void drawCurrentlyAddedElement(Graphics2D g);
    public void drawCopiedSubgraph(Graphics2D g);

    public void duplicateSubgraph();
    public void cancelCurrentOperation();
}
