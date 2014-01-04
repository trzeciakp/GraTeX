package pl.edu.agh.gratex.draw;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.awt.*;
import java.awt.geom.Arc2D;

/**
 *
 */
public class DummyEdgeDrawable implements Drawable {

    private Drawable delegatedDrawable;

    public DummyEdgeDrawable(Drawable drawable) {
        delegatedDrawable = drawable;
    }

    @Override
    public void draw(GraphElement graphElement, Graphics g) {
        delegatedDrawable.draw(graphElement, g);
        drawAngleVisualisation((Edge) graphElement, g);
    }

    //TODO temporarily duplicated with EdgeDrawable method. Unifying could be considered
    private void drawAngleVisualisation(Edge edge, Graphics g2d) {
        Vertex vertexA = edge.getVertexA();
        Vertex vertexB = edge.getVertexB();

        Graphics2D g = (Graphics2D) g2d.create();

        g.setColor(Const.ANGLE_VISUALIZATION_COLOR);
        g.setStroke(Const.ANGLE_VISUALIZATION_STROKE);
        g.drawLine(vertexA.getPosX(), vertexA.getPosY(), vertexB.getPosX(), vertexB.getPosY());

        Point m = new Point((vertexA.getPosX() + vertexB.getPosX()) / 2, (vertexA.getPosY() + vertexB.getPosY()) / 2);
        double d = 2 * m.distance(vertexA.getPosX(), vertexB.getPosY());

        Point p1 = new Point((int) Math.round(vertexA.getPosX() + (d / (2.5 * vertexA.getRadius())) * (edge.getOutPoint().x - vertexA.getPosX())),
                (int) Math.round(vertexA.getPosY() + (d / (2.5 * vertexA.getRadius())) * (edge.getOutPoint().y - vertexA.getPosY())));
        Point p2 = new Point((int) Math.round(vertexB.getPosX() + (d / (2.5 * vertexB.getRadius())) * (edge.getInPoint().x - vertexB.getPosX())),
                (int) Math.round(vertexB.getPosY() + (d / (2.5 * vertexB.getRadius())) * (edge.getInPoint().y - vertexB.getPosY())));
        g.drawLine(vertexA.getPosX(), vertexA.getPosY(), p1.x, p1.y);
        g.drawLine(vertexB.getPosX(), vertexB.getPosY(), p2.x, p2.y);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 60));
        FontMetrics fm = g.getFontMetrics();
        String alphaText = StringLiterals.ALPHA;

        int arcAngle = edge.getRelativeEdgeAngle();
        if (arcAngle > 180) {
            arcAngle -= 360;
            alphaText = "-" + StringLiterals.ALPHA;
        }
        double angle = Math.toRadians(edge.getOutAngle() - arcAngle / 2);
        int x = vertexA.getPosX() - fm.stringWidth(alphaText) / 2 + (int) Math.round((d / 4) * Math.cos(angle));
        int y = vertexA.getPosY() + fm.getAscent() / 4 - (int) Math.round((d / 4) * Math.sin(angle));
        g.drawString(alphaText, x, y);
        angle = Math.toRadians(edge.getInAngle() + arcAngle / 2);
        x = vertexB.getPosX() - fm.stringWidth(alphaText) / 2 + (int) Math.round((d / 4) * Math.cos(angle));
        y = vertexB.getPosY() + fm.getAscent() / 4 - (int) Math.round((d / 4) * Math.sin(angle));
        g.drawString(alphaText, x, y);
        g.draw(new Arc2D.Double(vertexA.getPosX() - (d / 3), vertexA.getPosY() - (d / 3), d / 1.5, d / 1.5, edge.getOutAngle(), -arcAngle, Arc2D.OPEN));
        g.draw(new Arc2D.Double(vertexB.getPosX() - (d / 3), vertexB.getPosY() - (d / 3), d / 1.5, d / 1.5, edge.getInAngle(), arcAngle, Arc2D.OPEN));
    }
}
