package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.model.edge.Edge;

import java.awt.*;
import java.awt.event.MouseEvent;

public interface MouseController {
    public void processMouseClicking(MouseEvent e);
    public void processMousePressing(MouseEvent e);
    public void processMouseReleasing(MouseEvent e);
    public void processMouseMoving(MouseEvent e);
    public void processMouseDragging(MouseEvent e);
    public void processShiftPressing(boolean flag);

    public Rectangle getSelectionArea();

    public void drawCurrentlyAddedElement(Graphics2D g);
    public void drawCopiedSubgraph(Graphics2D g);

    public void duplicateSubgraph();

    // TODO Nie wiadomo czy to potrzebne, patrz github -> issues -> #11
    public boolean isEdgeCurrentlyAdded(Edge edge);

    public void cancelCurrentOperation();
}
