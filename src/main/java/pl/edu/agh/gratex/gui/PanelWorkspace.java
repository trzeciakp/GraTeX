package pl.edu.agh.gratex.gui;

import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.ToolController;
import pl.edu.agh.gratex.controller.ToolListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.EnumMap;

public class PanelWorkspace extends JPanel implements MouseListener, MouseMotionListener, ToolListener {
    private static final long serialVersionUID = -7736096439630674213L;

    private Cursor addToolCursor;
    private Cursor removeToolCursor;
    private Cursor selectToolCursor;

    private JScrollPane parent;
    private ToolController toolController;
    private int pageWidth;
    private int pageHeight;
    private int mouseDragX = -1;
    private int mouseDragY = -1;
    private EnumMap<ToolType, Cursor> cursors = new EnumMap<ToolType, Cursor>(ToolType.class);


    private boolean mouseInWorkspace;

    public PanelWorkspace(JScrollPane _parent, ToolController toolController, int pageWidth, int pageHeight) {
        super();
        parent = _parent;
        this.toolController = toolController;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        addMouseListener(this);
        addMouseMotionListener(this);
        toolController.addToolListener(this);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            for(ToolCursor toolCursor : ToolCursor.values()) {
                URL url = this.getClass().getClassLoader().getResource(toolCursor.getImageName());
                Image image = ImageIO.read(url);
                Cursor cursor = toolkit.createCustomCursor(image, new Point(0, 0), toolCursor.getDescription());
                cursors.put(toolCursor.getToolType(), cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(this.pageWidth, this.pageHeight));
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
        g.fillRect(0, 0, this.pageWidth, this.pageHeight);

        if (ControlManager.graph != null) {
            ControlManager.paintGrid(g2d);

            if (mouseInWorkspace) {
                ControlManager.paintCurrentlyAddedElement(g2d);
                ControlManager.paintCopiedSubgraph(g2d);
            }

            ControlManager.graph.drawAll(g2d);
            ControlManager.paintSelectionArea(g2d);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getX() <= this.pageWidth && e.getY() <= this.pageHeight && SwingUtilities.isLeftMouseButton(e)) {
            ControlManager.processMouseClicking(e);
            repaint();
        } else if (SwingUtilities.isRightMouseButton(e)) {
            ControlManager.cancelCurrentOperation();
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
            ControlManager.processMouseReleasing(e);
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getX() <= this.pageWidth && e.getY() <= this.pageHeight && SwingUtilities.isLeftMouseButton(e)) {
            ControlManager.processMousePressing(e);
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
            ControlManager.processMouseDragging(e);
            repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (e.getX() <= this.pageWidth && e.getY() <= this.pageHeight) {
            ControlManager.processMouseMoving(e);
            repaint();
        }
    }

    @Override
    public void fireToolChanged(ToolType previousToolType, ToolType currentToolType) {
        updateCursor(currentToolType);
    }

    private enum ToolCursor {
        ADD(ToolType.ADD, "images/addtoolcursor.png", "add"),
        REMOVE(ToolType.REMOVE, "images/removetoolcursor.png", "remove"),
        SELECT(ToolType.SELECT, "images/selecttoolcursor.png", "select");

        private ToolType toolType;
        private String imageName;
        private String description;

        ToolCursor(ToolType toolType, String imageName, String description) {
            this.toolType = toolType;
            this.imageName = imageName;
            this.description = description;
        }

        private ToolType getToolType() {
            return toolType;
        }

        private String getImageName() {
            return imageName;
        }

        private String getDescription() {
            return description;
        }
    }
}

