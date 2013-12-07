package pl.edu.agh.gratex.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;

public class PanelWorkspace extends JPanel implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = -7736096439630674213L;

    private Cursor addToolCursor;
    private Cursor removeToolCursor;
    private Cursor selectToolCursor;

    private JScrollPane parent;
    private int mouseDragX = -1;
    private int mouseDragY = -1;

    private boolean mouseInWorkspace;

    public PanelWorkspace(JScrollPane _parent) {
        super();
        parent = _parent;
        addMouseListener(this);
        addMouseMotionListener(this);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            URL url = this.getClass().getClassLoader().getResource("images/addtoolcursor.png");
            Image image = ImageIO.read(url);
            addToolCursor = toolkit.createCustomCursor(image, new Point(0, 0), "add");
            url = this.getClass().getClassLoader().getResource("images/removetoolcursor.png");
            image = ImageIO.read(url);
            removeToolCursor = toolkit.createCustomCursor(image, new Point(0, 0), "remove");
            url = this.getClass().getClassLoader().getResource("images/selecttoolcursor.png");
            image = ImageIO.read(url);
            selectToolCursor = toolkit.createCustomCursor(image, new Point(0, 0), "select");
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateCursor();
        setPreferredSize(new Dimension(ControlManager.graph.pageWidth, ControlManager.graph.pageHeight));
    }

    public void updateCursor() {
        if (ControlManager.tool == 1) {
            setCursor(addToolCursor);
        } else if (ControlManager.tool == 2) {
            setCursor(removeToolCursor);
        } else {
            setCursor(selectToolCursor);
        }
    }

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
        g.fillRect(0, 0, ControlManager.graph.pageWidth, ControlManager.graph.pageHeight);

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
        if (e.getX() <= ControlManager.graph.pageWidth && e.getY() <= ControlManager.graph.pageHeight && SwingUtilities.isLeftMouseButton(e)) {
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
        updateCursor();
        mouseDragX = -1;
        if (SwingUtilities.isLeftMouseButton(e)) {
            ControlManager.processMouseReleasing(e);
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getX() <= ControlManager.graph.pageWidth && e.getY() <= ControlManager.graph.pageHeight && SwingUtilities.isLeftMouseButton(e)) {
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
        if (e.getX() <= ControlManager.graph.pageWidth && e.getY() <= ControlManager.graph.pageHeight) {
            ControlManager.processMouseMoving(e);
            repaint();
        }
    }
}
