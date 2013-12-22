package pl.edu.agh.gratex.prototype.model;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CustomShapeElement extends Element {

    private List<Point> points = new ArrayList<Point>();

    public CustomShapeElement(ElementListener editorPanel) {
        this.addListener(editorPanel);
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
        informListeners();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(points.size() > 2) {
            Path2D.Double path = new Path2D.Double();
            path.moveTo(points.get(0).getX(), points.get(0).getY());
            for (Point point : points) {
                path.lineTo(point.getX(), point.getY());
            }
            path.closePath();
            g2.setColor(Color.blue);
            g2.fill(path);
            g2.setColor(Color.black);
            g2.draw(path);
        }
    }
}
