package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.CursorType;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.controller.mouse.MouseController;
import pl.edu.agh.gratex.controller.operation.Operation;
import pl.edu.agh.gratex.controller.operation.OperationListener;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;

@SuppressWarnings("serial")
public class PanelWorkspace extends JPanel implements MouseListener, MouseMotionListener, OperationListener, ToolListener {

    private GeneralController generalController;
    private MouseController mouseController;

    private JScrollPane parent;

    private int mouseDragX = -1;
    private int mouseDragY = -1;
    private boolean mouseInWorkspace;

    private EnumMap<ToolType, Cursor> cursors = new EnumMap<>(ToolType.class);
    private ToolType tool;
    private Image cannotCopyImage = Application.loadImage("cannotcopy.png");

    public PanelWorkspace(JScrollPane parent, GeneralController generalController) {
        super();
        this.parent = parent;
        this.generalController = generalController;
        this.mouseController = generalController.getMouseController();

        generalController.getOperationController().addOperationListener(this);
        generalController.getToolController().addToolListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        for (CursorType cursorType : CursorType.values()) {
            Image image = Application.loadImage(cursorType.getImageName());
            Cursor cursor = toolkit.createCustomCursor(image, new Point(0, 0), cursorType.getDescription());
            cursors.put(cursorType.getToolType(), cursor);
        }

        setPreferredSize(new Dimension(Const.PAGE_WIDTH, Const.PAGE_HEIGHT));
        updateCursor(generalController.getToolController().getTool());
    }

    private void updateCursor(ToolType toolType) {
        setCursor(cursors.get(toolType));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = g2d.getRenderingHints();
        rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, Const.PAGE_WIDTH, Const.PAGE_HEIGHT);


        if (generalController.getGraph() != null) {
            paintGrid(g2d);

            List<GraphElement> allElements = generalController.getGraph().getAllElements();
            allElements.addAll(mouseController.getCopiedSubgraph());
            GraphElement currentlyAddedElement = mouseController.getCurrentlyAddedElement();
            if (currentlyAddedElement != null) {
                allElements.add(currentlyAddedElement);
            }
            GraphElement.sortByDrawingPriorities(allElements);
            for (GraphElement element : allElements) {
                element.draw(g2d);
            }

            paintSelectionArea(g2d);

            Point iconLocation = mouseController.locationOfCannotCopyIcon();
            if (iconLocation != null) {
                g.drawImage(cannotCopyImage, iconLocation.x - cannotCopyImage.getWidth(null), iconLocation.y - cannotCopyImage.getHeight(null), null);
            }
        }
    }

    private void paintGrid(Graphics2D g) {
        Graph graph = generalController.getGraph();
        if (graph.isGridOn()) {
            g.setColor(Const.GRID_COLOR);

            for (int i = graph.getGridResolutionX(); i < Const.PAGE_WIDTH; i += graph.getGridResolutionX())
                g.drawLine(i, 0, i, Const.PAGE_HEIGHT);

            for (int i = graph.getGridResolutionY(); i < Const.PAGE_HEIGHT; i += graph.getGridResolutionY())
                g.drawLine(0, i, Const.PAGE_WIDTH, i);
        }
    }

    public void paintSelectionArea(Graphics2D g) {
        Rectangle selectionArea = mouseController.getSelectionArea();
        if (selectionArea != null) {
            if (tool == ToolType.REMOVE) {
                g.setColor(Const.DELETION_RECT_INSIDE_COLOR);
                g.fill(selectionArea);
                g.setColor(Const.DELETION_RECT_BORDER_COLOR);
                g.draw(selectionArea);
            } else if (tool == ToolType.SELECT) {
                g.setColor(Const.SELECTION_RECT_INSIDE_COLOR);
                g.fill(selectionArea);
                g.setColor(Const.SELECTION_RECT_BORDER_COLOR);
                g.draw(selectionArea);
            }
        }
    }


    // ===================================
    // mouse listeners implementation
    public void mouseClicked(MouseEvent e) {
        if (e.getX() <= Const.PAGE_WIDTH && e.getY() <= Const.PAGE_HEIGHT && SwingUtilities.isLeftMouseButton(e)) {
            mouseController.processMouseClicking(e);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            mouseController.cancelCurrentOperation();
        }
    }

    public void mouseEntered(MouseEvent e) {
        mouseInWorkspace = true;
    }

    public void mouseExited(MouseEvent e) {
        mouseInWorkspace = false;
        // Repaint needed to hide currently added element when navigating out of workspace
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        updateCursor(tool);
        mouseDragX = -1;
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseController.processMouseReleasing(e);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getX() <= Const.PAGE_WIDTH && e.getY() <= Const.PAGE_HEIGHT && SwingUtilities.isLeftMouseButton(e)) {
            mouseController.processMousePressing(e);
        } else if (!SwingUtilities.isLeftMouseButton(e)) {
            mouseDragX = MouseInfo.getPointerInfo().getLocation().x;
            mouseDragY = MouseInfo.getPointerInfo().getLocation().y;
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (mouseDragX > -1) {
            int mouseX = MouseInfo.getPointerInfo().getLocation().x;
            int mouseY = MouseInfo.getPointerInfo().getLocation().y;

            Point vp = parent.getViewport().getViewPosition();
            vp.x -= mouseX - mouseDragX;
            vp.y -= mouseY - mouseDragY;
            vp.x = Math.max(0, vp.x);
            vp.x = Math.min(parent.getViewport().getView().getWidth() - parent.getViewport().getWidth(), vp.x);
            vp.y = Math.max(0, vp.y);
            vp.y = Math.min(parent.getViewport().getView().getHeight() - parent.getViewport().getHeight(), vp.y);
            parent.getViewport().setViewPosition(vp);

            mouseDragX = mouseX;
            mouseDragY = mouseY;
        } else {
            if (mouseInWorkspace) {
                mouseController.processMouseDragging(e);
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (e.getX() <= Const.PAGE_WIDTH && e.getY() <= Const.PAGE_HEIGHT) {
            mouseInWorkspace = true;
            mouseController.processMouseMoving(e);
        } else {
            mouseInWorkspace = false;
            // Repaint needed to hide currently added element when navigating out of page
            repaint();
        }
    }


    // ===================================
    // OperationListener implementation


    @Override
    public void operationEvent(Operation operation) {
        repaint();
    }

    // ===================================
    // ToolListener implementation
    @Override
    public void toolChanged(ToolType previousTool, ToolType currentTool) {
        tool = currentTool;
        updateCursor(currentTool);
    }

    @Override
    public int toolUpdatePriority() {
        return 0;
    }
}

