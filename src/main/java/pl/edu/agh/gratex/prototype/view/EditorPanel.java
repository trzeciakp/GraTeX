package pl.edu.agh.gratex.prototype.view;

import pl.edu.agh.gratex.prototype.controller.Drawable;
import pl.edu.agh.gratex.prototype.model.Element;
import pl.edu.agh.gratex.prototype.model.ElementListener;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class EditorPanel extends JPanel implements MouseMotionListener, ElementListener {


    private DrawingLineListener listener;
    private Set<Drawable> elementsToDraw = new HashSet<Drawable>();
    private int x1;
    private int y1;

    public EditorPanel(DrawingLineListener listener,int width, int height) {
        super();
        this.listener = listener;

        this.setLayout(null);
        this.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        this.setBounds(10,10, width, height);
        addMouseListener(listener);
        addMouseMotionListener(this);
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (Drawable drawable : elementsToDraw) {
            drawable.draw(g);
        }
        g.setColor(Color.black);
        for (Point point : listener.getPoints()) {
            g.fillOval(point.x,point.y,2,2);
        }
    }

    // this method is invoked when we drag the mouse
    public void mouseDragged(MouseEvent me)
    {
        listener.mouseDragged(me);
        x1=me.getX(); // to save the mouse pointer x-coordinate while dragging.
        y1=me.getY(); // to save the mouse pointer y-coordinate while dragging.

        repaint();
    }
    public void mouseMoved(MouseEvent me)
    {
        listener.mouseMoved(me);

    }

    @Override
    public void fireElementChanged(Element element) {
        elementsToDraw.add(element);
        repaint();
    }

    @Override
    public void fireElementDeleted(Element element) {
        elementsToDraw.remove(element);
        repaint();
    }
}
