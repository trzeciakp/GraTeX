package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.CursorType;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.MouseController;
import pl.edu.agh.gratex.controller.ToolController;
import pl.edu.agh.gratex.controller.ToolListener;
import pl.edu.agh.gratex.model.graph.Graph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.EnumMap;

@SuppressWarnings("serial")
public class PanelWorkspace extends JPanel implements MouseListener, MouseMotionListener, ToolListener {

    private GeneralController generalController;
    private MouseController mouseController;

    private JScrollPane parent;
    private int mouseDragX = -1;
    private int mouseDragY = -1;
    private EnumMap<ToolType, Cursor> cursors = new EnumMap<>(ToolType.class);


    private boolean mouseInWorkspace;

    public PanelWorkspace(JScrollPane parent, GeneralController generalController, MouseController mouseController, ToolController toolController) {
        super();
        this.generalController = generalController;
        this.mouseController = mouseController;
        this.parent = parent;
        addMouseListener(this);
        addMouseMotionListener(this);
        toolController.addToolListener(this);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            for (CursorType cursorType : CursorType.values()) {
                URL url = this.getClass().getClassLoader().getResource(cursorType.getImageName());
                Image image = ImageIO.read(url);
                Cursor cursor = toolkit.createCustomCursor(image, new Point(0, 0), cursorType.getDescription());
                cursors.put(cursorType.getToolType(), cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(Const.PAGE_WIDTH, Const.PAGE_HEIGHT));
        updateCursor(toolController.getTool());
    }

    private void updateCursor(ToolType toolType) {
        setCursor(cursors.get(toolType));
    }

    //TODO maybe introduce WorkspaceController?
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = g2d.getRenderingHints();
        rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Const.PAGE_WIDTH, Const.PAGE_HEIGHT);

        if (generalController.getGraph() != null) {
            paintGrid(g2d, generalController.getGraph());

            if (mouseInWorkspace) {
                mouseController.paintCurrentlyAddedElement(g2d);
                paintCopiedSubgraph(g2d);
            }

            generalController.getGraph().drawAll(g2d);
            paintSelectionArea(g2d);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getX() <= Const.PAGE_WIDTH && e.getY() <= Const.PAGE_HEIGHT && SwingUtilities.isLeftMouseButton(e)) {
            mouseController.processMouseClicking(e);
            repaint();
        } else if (SwingUtilities.isRightMouseButton(e)) {
            generalController.getMouseController().cancelCurrentOperation();
            repaint();
        }
    }

    public void mouseEntered(MouseEvent e) {
        mouseInWorkspace = true;
    }

    public void mouseExited(MouseEvent e) {
        mouseInWorkspace = false;
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        //TODO why was it here?
        //updateCursor();
        mouseDragX = -1;
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseController.processMouseReleasing(e);
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getX() <= Const.PAGE_WIDTH && e.getY() <= Const.PAGE_HEIGHT && SwingUtilities.isLeftMouseButton(e)) {
            mouseController.processMousePressing(e);
            repaint();
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
            mouseController.processMouseDragging(e);
            repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (e.getX() <= Const.PAGE_WIDTH && e.getY() <= Const.PAGE_HEIGHT) {
            mouseController.processMouseMoving(e);
            repaint();
        }
    }


    private void paintGrid(Graphics2D g, Graph graph) {
        if (graph.gridOn) {
            g.setColor(Const.GRID_COLOR);
            int i = 0;
            while ((i += graph.gridResolutionX) < Const.PAGE_WIDTH) {
                g.drawLine(i, 0, i, Const.PAGE_HEIGHT);
            }
            i = 0;
            while ((i += graph.gridResolutionY) < Const.PAGE_HEIGHT) {
                g.drawLine(0, i, Const.PAGE_WIDTH, i);
            }
        }
    }


    public void paintSelectionArea(Graphics2D g) {
        Rectangle selectionArea = mouseController.getSelectionArea();
        if (selectionArea != null) {
            g.setColor(Const.SELECTION_RECT_INSIDE_COLOR);
            g.fill(selectionArea);

            g.setColor(Const.SELECTION_RECT_BORDER_COLOR);
            g.draw(selectionArea);
        }
    }


    public void paintCopiedSubgraph(Graphics2D g) {
        generalController.getMouseController().paintCopiedSubgraph(g);
    }

    @Override
    public void toolChanged(ToolType previousToolType, ToolType currentToolType) {
        updateCursor(currentToolType);
    }

    @Override
    public int toolUpdatePriority() {
        return 0;
    }
}

