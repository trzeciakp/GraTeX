package pl.edu.agh.gratex.model.edge;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.utils.DrawingTools;
import pl.edu.agh.gratex.utils.Geometry;
import pl.edu.agh.gratex.view.ControlManager;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;


public class Edge extends GraphElement implements Serializable {
    private static final long serialVersionUID = -7941761380307220731L;

    // TEST NOWEJ OPERACJI CTRL Z
    @Override
    public boolean equals(Object o2) {
        if (!(o2 instanceof Edge)) {
            return false;
        }
        Edge e = (Edge) o2;
        if (lineType != e.lineType) {
            return false;
        }
        if (lineWidth != e.lineWidth) {
            return false;
        }
        if (directed != e.directed) {
            return false;
        }
        if (arrowType != e.arrowType) {
            return false;
        }
        if (!lineColor.equals(e.lineColor)) {
            return false;
        }
        if (relativeEdgeAngle != e.relativeEdgeAngle) {
            return false;
        }
        if (!vertexA.equals(e.vertexA)) {
            return false;
        }
        if (!vertexB.equals(e.vertexB)) {
            return false;
        }
        if (!vertexB.equals(e.vertexB)) {
            return false;
        }
        if (label != e.label) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return vertexA.hashCode() * 1000000 + vertexB.hashCode();
    }

    // Wartości edytowalne przez użytkowanika
    private LineType lineType;
    private int lineWidth;
    private boolean directed;                                        // Z shiftem rysujemy skierowaną
    private int arrowType;
    private Color lineColor;
    private int relativeEdgeAngle;

    // Wartości potrzebne do parsowania
    private Vertex vertexA;
    private Vertex vertexB;
    private LabelE label;
    private int inAngle;
    private int outAngle;

    // Pozostałe
    private Point arcMiddle = new Point();
    private int arcRadius;
    private Point outPoint;
    private Point inPoint;
    private Arc2D.Double arc;
    private int[] arrowLine1 = null;
    private int[] arrowLine2 = null;
    private Graph graph;

    public Edge(Graph graph) {
        this.graph = graph;
    }

    public Edge getCopy(LinkedList<Vertex> vertices) {
        Vertex _vertexA = null;
        Vertex _vertexB = null;
        Iterator<Vertex> itv = vertices.listIterator();
        Vertex tempV = null;
        while (itv.hasNext()) {
            tempV = itv.next();
            if (tempV.getPosX() == getVertexA().getPosX() && tempV.getPosY() == getVertexA().getPosY()) {
                _vertexA = tempV;
            }
            if (tempV.getPosX() == getVertexB().getPosX() && tempV.getPosY() == getVertexB().getPosY()) {
                _vertexB = tempV;
            }
        }

        Edge result = new Edge(this.graph);
        result.setModel(getModel());
        if (_vertexA == null || _vertexB == null) {
            return null;
        }
        result.setVertexA(_vertexA);
        result.setVertexB(_vertexB);

        if (getLabel() != null) {
            result.setLabel(getLabel().getCopy(result));
        }

        return result;
    }

    public void setModel(PropertyModel pm) {
        EdgePropertyModel model = (EdgePropertyModel) pm;

        if (model.lineType != LineType.EMPTY) {
            setLineType(model.lineType);
        }

        if (model.lineWidth > -1) {
            setLineWidth(model.lineWidth);
        }

        if (model.directed > -1) {
            setDirected((model.directed == 1));
        }

        if (model.lineColor != null) {
            setLineColor(new Color(model.lineColor.getRGB()));
        }

        if (model.relativeEdgeAngle > -1) {
            setRelativeEdgeAngle(model.relativeEdgeAngle);
        }

        if (model.arrowType > -1) {
            setArrowType(model.arrowType);
        }
    }

    public PropertyModel getModel() {
        EdgePropertyModel result = new EdgePropertyModel();

        result.lineWidth = getLineWidth();
        result.lineType = getLineType();
        result.arrowType = getArrowType();
        result.lineColor = new Color(getLineColor().getRGB());
        result.relativeEdgeAngle = getRelativeEdgeAngle();
        if (getVertexA() == getVertexB()) {
            result.isLoop = PropertyModel.YES;
        } else
            result.isLoop = PropertyModel.NO;
        result.directed = 0;
        if (isDirected()) {
            result.directed = 1;
        }

        return result;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.VERTEX;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    public boolean intersects(int x, int y) {
        Point2D va = new Point(getVertexA().getPosX(), getVertexA().getPosY());
        Point2D vb = new Point(getVertexB().getPosX(), getVertexB().getPosY());
        Point2D click = new Point(x, y);

        if (getVertexA() == getVertexB()) {
            int r = getVertexA().getRadius() + getVertexA().getLineWidth() / 2;
            double dx = getArcMiddle().x - x;
            double dy = getArcMiddle().y - y;
            double a = r * 0.75;
            double b = r * 0.375;
            if (getRelativeEdgeAngle() == 90 || getRelativeEdgeAngle() == 270) {
                a = r * 0.375;
                b = r * 0.75;
            }

            double distance = (dx * dx) / (a * a) + (dy * dy) / (b * b);
            return Math.abs(distance - 1) < 1;
        } else {
            if (getRelativeEdgeAngle() != 0) {
                if (Math.abs(click.distance(getArcMiddle()) - getArcRadius()) < 10 + getLineWidth() / 2) {
                    double clickAngle = (Math.toDegrees(Math.atan2(click.getX() - getArcMiddle().x, click.getY() - getArcMiddle().y)) + 270) % 360;
                    if (getArc().extent < 0) {
                        double endAngle = (getArc().start + getArc().extent + 720) % 360;

                        return (clickAngle - endAngle + 720) % 360 < (getArc().start - endAngle + 720) % 360;
                    } else {
                        return (getArc().extent) % 360 > (clickAngle - getArc().start + 720) % 360;
                    }
                }
            } else {
                if ((Math.min(getVertexA().getPosX(), getVertexB().getPosX()) <= x && Math.max(getVertexA().getPosX(), getVertexB().getPosX()) >= x)
                        || (Math.min(getVertexA().getPosY(), getVertexB().getPosY()) <= y && Math.max(getVertexA().getPosY(), getVertexB().getPosY()) >= y)) {
                    if (click.distance(va) > getVertexA().getRadius() && click.distance(vb) > getVertexB().getRadius()) {
                        double distance = Line2D.ptLineDist((double) getVertexA().getPosX(), (double) getVertexA().getPosY(), (double) getVertexB().getPosX(),
                                (double) getVertexB().getPosY(), (double) x, (double) y);
                        return distance < 12;
                    }
                }
            }
        }

        return false;
    }

    private void calculateInOutPoints() {
        double beta = (Math.toDegrees(Math.atan2(getVertexB().getPosX() - getVertexA().getPosX(), getVertexB().getPosY() - getVertexA().getPosY()))) + 270;
        setOutPoint(Geometry.calculateEdgeExitPoint(getVertexA(), getRelativeEdgeAngle() + beta));
        setInPoint(Geometry.calculateEdgeExitPoint(getVertexB(), 180 - getRelativeEdgeAngle() + beta));
        setOutAngle((int) Math.round(Math.toDegrees(Math.atan2(getOutPoint().x - getVertexA().getPosX(), getOutPoint().y - getVertexA().getPosY())) + 270) % 360);
        setInAngle((int) Math.round(Math.toDegrees(Math.atan2(getInPoint().x - getVertexB().getPosX(), getInPoint().y - getVertexB().getPosY())) + 270) % 360);
    }

    private void calculateArcParameters() {
        if (getRelativeEdgeAngle() != 0) {
            double mx = (getOutPoint().x + getInPoint().x) / 2;
            double my = (getOutPoint().y + getInPoint().y) / 2;
            double dx = (getInPoint().x - getOutPoint().x) / 2;
            double dy = (getInPoint().y - getOutPoint().y) / 2;
            getArcMiddle().x = (int) Math.round(mx - dy * Math.cos(Math.toRadians(getRelativeEdgeAngle())) / Math.sin(Math.toRadians(getRelativeEdgeAngle())));
            getArcMiddle().y = (int) Math.round(my + dx * Math.cos(Math.toRadians(getRelativeEdgeAngle())) / Math.sin(Math.toRadians(getRelativeEdgeAngle())));
            setArcRadius((int) Math.round(Math.sqrt((getArcMiddle().x - getOutPoint().x) * (getArcMiddle().x - getOutPoint().x) + (getArcMiddle().y - getOutPoint().y)
                    * (getArcMiddle().y - getOutPoint().y))));
        }
    }

    private void updateArrowPosition() {
        int ax = 0;
        int ay = 0;
        int bx = getInPoint().x;
        int by = getInPoint().y;

        if (getVertexA() == getVertexB()) {
            ax = 2 * bx - getVertexA().getPosX();
            ay = 2 * by - getVertexA().getPosY();
        } else if (getRelativeEdgeAngle() != 0) {
            int x1 = bx - (getArcMiddle().y - by);
            int y1 = by + (getArcMiddle().x - bx);
            int x2 = bx + (getArcMiddle().y - by);
            int y2 = by - (getArcMiddle().x - bx);
            if (Point.distance(x1, y1, getVertexB().getPosX(), getVertexB().getPosY()) > Point.distance(x2, y2, getVertexB().getPosX(), getVertexB().getPosY())) {
                ax = x1;
                ay = y1;
            } else {
                ax = x2;
                ay = y2;
            }
        } else {
            ax = getVertexA().getPosX();
            ay = getVertexA().getPosY();
        }

        int dx = bx - ax;
        int dy = by - ay;

        int[] p1 = new int[]{ax - dy, ay + dx};
        int[] p2 = new int[]{ax + dy, ay - dx};

        if (getArrowType() == ArrowType.FILLED.getValue()) {

            p1 = new int[]{ax - dy / 2, ay + dx / 2};
            p2 = new int[]{ax + dy / 2, ay - dx / 2};
        }

        double part = (Const.ARROW_LENGTH_BASIC + Const.ARROW_LENGTH_FACTOR * getLineWidth())
                / Math.sqrt((p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1]));

        p1[0] = bx - (int) Math.round(part * (bx - p1[0]));
        p1[1] = by - (int) Math.round(part * (by - p1[1]));
        p2[0] = bx - (int) Math.round(part * (bx - p2[0]));
        p2[1] = by - (int) Math.round(part * (by - p2[1]));

        setArrowLine1(new int[]{p1[0], p1[1], bx, by});
        setArrowLine2(new int[]{p2[0], p2[1], bx, by});
    }

    private void drawAngleVisualisation(Graphics2D g) {
        g.setColor(Color.gray);
        g.setStroke(DrawingTools.getStroke(2, LineType.DASHED, 0.0));
        g.drawLine(getVertexA().getPosX(), getVertexA().getPosY(), getVertexB().getPosX(), getVertexB().getPosY());

        Point m = new Point((getVertexA().getPosX() + getVertexB().getPosX()) / 2, (getVertexA().getPosY() + getVertexB().getPosY()) / 2);
        double d = 2 * m.distance(getVertexA().getPosX(), getVertexB().getPosY());

        Point p1 = new Point((int) Math.round(getVertexA().getPosX() + (d / (2.5 * getVertexA().getRadius())) * (getOutPoint().x - getVertexA().getPosX())),
                (int) Math.round(getVertexA().getPosY() + (d / (2.5 * getVertexA().getRadius())) * (getOutPoint().y - getVertexA().getPosY())));
        Point p2 = new Point((int) Math.round(getVertexB().getPosX() + (d / (2.5 * getVertexB().getRadius())) * (getInPoint().x - getVertexB().getPosX())),
                (int) Math.round(getVertexB().getPosY() + (d / (2.5 * getVertexB().getRadius())) * (getInPoint().y - getVertexB().getPosY())));
        g.drawLine(getVertexA().getPosX(), getVertexA().getPosY(), p1.x, p1.y);
        g.drawLine(getVertexB().getPosX(), getVertexB().getPosY(), p2.x, p2.y);

        g.setFont(new Font("Times New Roman", Font.PLAIN, 60));
        FontMetrics fm = g.getFontMetrics();
        String alphaText = Character.toString('\u03B1');

        int arcAngle = getRelativeEdgeAngle();
        if (arcAngle > 180) {
            arcAngle -= 360;
            alphaText = '-' + Character.toString('\u03B1');
        }
        double angle = Math.toRadians(getOutAngle() - arcAngle / 2);
        int x = getVertexA().getPosX() - fm.stringWidth(alphaText) / 2 + (int) Math.round((d / 4) * Math.cos(angle));
        int y = getVertexA().getPosY() + fm.getAscent() / 4 - (int) Math.round((d / 4) * Math.sin(angle));
        g.drawString(alphaText, x, y);
        angle = Math.toRadians(getInAngle() + arcAngle / 2);
        x = getVertexB().getPosX() - fm.stringWidth(alphaText) / 2 + (int) Math.round((d / 4) * Math.cos(angle));
        y = getVertexB().getPosY() + fm.getAscent() / 4 - (int) Math.round((d / 4) * Math.sin(angle));
        g.drawString(alphaText, x, y);
        g.draw(new Arc2D.Double(getVertexA().getPosX() - (d / 3), getVertexA().getPosY() - (d / 3), d / 1.5, d / 1.5, getOutAngle(), -arcAngle, Arc2D.OPEN));
        g.draw(new Arc2D.Double(getVertexB().getPosX() - (d / 3), getVertexB().getPosY() - (d / 3), d / 1.5, d / 1.5, getInAngle(), arcAngle, Arc2D.OPEN));
    }

    public void updatePosition() {
        if (getVertexA() != getVertexB()) {
            calculateInOutPoints();
            calculateArcParameters();

            if (getRelativeEdgeAngle() != 0) {
                int x1 = getOutPoint().x;
                int y1 = getOutPoint().y;
                int x2 = getInPoint().x;
                int y2 = getInPoint().y;
                int x0 = getArcMiddle().x;
                int y0 = getArcMiddle().y;

                double x = x0 - getArcRadius();
                double y = y0 - getArcRadius();
                double width = 2 * getArcRadius();
                double height = 2 * getArcRadius();
                double startAngle = (Math.toDegrees(Math.atan2(x1 - x0, y1 - y0)) + 270) % 360;
                double endAngle = (Math.toDegrees(Math.atan2(x2 - x0, y2 - y0)) + 270) % 360;

                if (getRelativeEdgeAngle() > 180) {
                    endAngle = (endAngle - startAngle + 360) % 360;
                } else {
                    endAngle = ((endAngle - startAngle + 360) % 360) - 360;
                }

                setArc(new Arc2D.Double(x, y, width, height, startAngle, endAngle, Arc2D.OPEN));
            }
        } else {
            int r = getVertexA().getRadius() + getVertexA().getLineWidth() / 2;
            double x = 0;
            double y = 0;
            double width = r * 1.5;
            double height = r * 0.75;
            if (getRelativeEdgeAngle() == 90 || getRelativeEdgeAngle() == 270) {
                width = r * 0.75;
                height = r * 1.5;
            }

            double offsetRate = 0.75;
            double arrowPosHorizRate = 0.965;
            double arrowPosVertRate = 0.265;

            if (getVertexA().getShape() == 2) {
                offsetRate = 0.375;
                arrowPosHorizRate = 0.84;
                arrowPosVertRate = 0.325;

                if (getRelativeEdgeAngle() == 90) {
                    arrowPosHorizRate = 0.595;
                    arrowPosVertRate = 0.26;
                } else if (getRelativeEdgeAngle() == 180) {
                    arrowPosHorizRate = 0.475;
                    arrowPosVertRate = 0.195;
                } else if (getRelativeEdgeAngle() == 270) {
                    arrowPosHorizRate = 0.505;
                    arrowPosVertRate = 0.15;
                    width /= 2;
                    height /= 2;
                }
            } else if (getVertexA().getShape() == 3) {
                offsetRate = 0.5;
                arrowPosHorizRate = 0.72;
                arrowPosVertRate = 0.265;
            } else if (getVertexA().getShape() == 4) {
                offsetRate = 0.4375;
                arrowPosHorizRate = 0.755;
                arrowPosVertRate = 0.295;

                if (getRelativeEdgeAngle() == 90) {
                    arrowPosHorizRate = 0.795;
                    arrowPosVertRate = 0.305;
                } else if (getRelativeEdgeAngle() == 180) {
                    arrowPosHorizRate = 0.93;
                    arrowPosVertRate = 0.355;
                } else if (getRelativeEdgeAngle() == 270) {
                    arrowPosHorizRate = 0.815;
                    arrowPosVertRate = 0.335;
                }
            } else if (getVertexA().getShape() == 5) {
                offsetRate = 0.625;
                arrowPosHorizRate = 0.85;
                arrowPosVertRate = 0.27;
                if (getRelativeEdgeAngle() == 90 || getRelativeEdgeAngle() == 270) {
                    arrowPosHorizRate = 0.88;
                    arrowPosVertRate = 0.283;
                }
            }

            switch (getRelativeEdgeAngle()) {
                case 0: {
                    x = getVertexA().getPosX() + r * offsetRate;
                    y = getVertexA().getPosY() - height / 2;
                    setInPoint(new Point((int) Math.round(getVertexA().getPosX() + r * arrowPosHorizRate), (int) Math.round(getVertexA().getPosY() + r * arrowPosVertRate)));
                    setOutPoint(new Point((int) Math.round(getVertexA().getPosX() + r * arrowPosHorizRate),
                            (int) Math.round(getVertexA().getPosY() - r * arrowPosVertRate)));
                    break;
                }
                case 90: {
                    x = getVertexA().getPosX() - width / 2;
                    y = getVertexA().getPosY() - r * (offsetRate + height / r);
                    setInPoint(new Point((int) Math.round(getVertexA().getPosX() + r * arrowPosVertRate), (int) Math.round(getVertexA().getPosY() - r * arrowPosHorizRate)));
                    setOutPoint(new Point((int) Math.round(getVertexA().getPosX() - r * arrowPosVertRate),
                            (int) Math.round(getVertexA().getPosY() - r * arrowPosHorizRate)));
                    break;
                }
                case 180: {
                    x = getVertexA().getPosX() - r * (offsetRate + width / r);
                    y = getVertexA().getPosY() - height / 2;
                    setInPoint(new Point((int) Math.round(getVertexA().getPosX() - r * arrowPosHorizRate), (int) Math.round(getVertexA().getPosY() - r * arrowPosVertRate)));
                    setOutPoint(new Point((int) Math.round(getVertexA().getPosX() - r * arrowPosHorizRate),
                            (int) Math.round(getVertexA().getPosY() + r * arrowPosVertRate)));
                    break;
                }
                case 270: {
                    x = getVertexA().getPosX() - width / 2;
                    y = getVertexA().getPosY() + r * offsetRate;
                    setInPoint(new Point((int) Math.round(getVertexA().getPosX() - r * arrowPosVertRate), (int) Math.round(getVertexA().getPosY() + r * arrowPosHorizRate)));
                    setOutPoint(new Point((int) Math.round(getVertexA().getPosX() + r * arrowPosVertRate),
                            (int) Math.round(getVertexA().getPosY() + r * arrowPosHorizRate)));
                    break;
                }
            }
            setArcMiddle(new Point((int) Math.round(x + width / 2), (int) Math.round(y + height / 2)));
            setArc(new Arc2D.Double(x, y, width, height, 0, 360, Arc2D.OPEN));
        }

        if (isDirected()) {
            updateArrowPosition();
        }
    }

    public void draw(Graphics2D g2d, boolean dummy) {
        Graphics2D g = (Graphics2D) g2d.create();

        updatePosition();

        if (getVertexA() != getVertexB()) {
            if (getRelativeEdgeAngle() != 0) {
                if (ControlManager.mainWindow.getSelectionController().contains(this)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(getLineWidth() * 2 + 5);
                    g.setStroke(drawingStroke);
                    g.draw(getArc());
                }

                g.setColor(getLineColor());
                if (dummy) {
                    g.setColor(DrawingTools.getDummyColor(getLineColor()));
                }
                g.setStroke(DrawingTools.getStroke(getLineWidth(), getLineType(), 0.0));
                g.draw(getArc());
            } else {
                if (ControlManager.mainWindow.getSelectionController().contains(this)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(getLineWidth() * 2 + 5);
                    g.setStroke(drawingStroke);
                    g.drawLine(getVertexA().getPosX(), getVertexA().getPosY(), getVertexB().getPosX(), getVertexB().getPosY());
                }

                g.setColor(getLineColor());
                if (dummy) {
                    g.setColor(DrawingTools.getDummyColor(getLineColor()));
                }
                g.setStroke(DrawingTools.getStroke(getLineWidth(), getLineType(), 0.0));
                g.drawLine(getVertexA().getPosX(), getVertexA().getPosY(), getVertexB().getPosX(), getVertexB().getPosY());
            }
            if ((ControlManager.mainWindow.getSelectionController().contains(this) || ControlManager.currentlyAddedEdge == this) && getRelativeEdgeAngle() != 0) {
                drawAngleVisualisation(g);
            }
        } else {
            if (ControlManager.mainWindow.getSelectionController().contains(this)) {
                g.setColor(Const.SELECTION_COLOR);
                Stroke drawingStroke = new BasicStroke(getLineWidth() * 2 + 5);
                g.setStroke(drawingStroke);
                g.draw(getArc());
            }

            g.setColor(getLineColor());
            if (dummy) {
                g.setColor(DrawingTools.getDummyColor(getLineColor()));
            }
            g.setStroke(DrawingTools.getStroke(getLineWidth(), getLineType(), 0.0));
            g.draw(getArc());
        }

        if (isDirected()) {
            if (getArrowType() == ArrowType.BASIC.getValue()) {
                if (ControlManager.mainWindow.getSelectionController().contains(this)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(getLineWidth() * 2 + 5);
                    g.setStroke(drawingStroke);
                    g.drawLine(getArrowLine1()[0], getArrowLine1()[1], getArrowLine1()[2], getArrowLine1()[3]);
                    g.drawLine(getArrowLine2()[0], getArrowLine2()[1], getArrowLine2()[2], getArrowLine2()[3]);
                }

                g.setColor(getLineColor());
                if (dummy) {
                    g.setColor(DrawingTools.getDummyColor(getLineColor()));
                }
                g.setStroke(new BasicStroke(getLineWidth()));
                g.drawLine(getArrowLine1()[0], getArrowLine1()[1], getArrowLine1()[2], getArrowLine1()[3]);
                g.drawLine(getArrowLine2()[0], getArrowLine2()[1], getArrowLine2()[2], getArrowLine2()[3]);
            } else {
                if (ControlManager.mainWindow.getSelectionController().contains(this)) {
                    g.setColor(Const.SELECTION_COLOR);
                    Stroke drawingStroke = new BasicStroke(getLineWidth() * 2 + 5);
                    g.setStroke(drawingStroke);
                    g.fillPolygon(new Polygon(new int[]{getArrowLine1()[0], getArrowLine1()[2], getArrowLine2()[0]}, new int[]{getArrowLine1()[1], getArrowLine1()[3], getArrowLine2()[1]}, 3));
                }

                g.setColor(getLineColor());
                if (dummy) {
                    g.setColor(DrawingTools.getDummyColor(getLineColor()));
                }
                g.setStroke(new BasicStroke(getLineWidth()));
                g.fillPolygon(new Polygon(new int[]{getArrowLine1()[0], getArrowLine1()[2], getArrowLine2()[0]}, new int[]{getArrowLine1()[1], getArrowLine1()[3], getArrowLine2()[1]}, 3));
            }
        }

        g.dispose();
    }

    public void drawLabel(Graphics2D g, boolean dummy) {
        if (getLabel() != null) {
            getLabel().draw(g, dummy);
        }
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public int getArrowType() {
        return arrowType;
    }

    public void setArrowType(int arrowType) {
        this.arrowType = arrowType;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public int getRelativeEdgeAngle() {
        return relativeEdgeAngle;
    }

    public void setRelativeEdgeAngle(int relativeEdgeAngle) {
        this.relativeEdgeAngle = relativeEdgeAngle;
    }

    public Vertex getVertexA() {
        return vertexA;
    }

    public void setVertexA(Vertex vertexA) {
        this.vertexA = vertexA;
    }

    public Vertex getVertexB() {
        return vertexB;
    }

    public void setVertexB(Vertex vertexB) {
        this.vertexB = vertexB;
    }

    public LabelE getLabel() {
        return label;
    }

    public void setLabel(LabelE label) {
        this.label = label;
    }

    public int getInAngle() {
        return inAngle;
    }

    public void setInAngle(int inAngle) {
        this.inAngle = inAngle;
    }

    public int getOutAngle() {
        return outAngle;
    }

    public void setOutAngle(int outAngle) {
        this.outAngle = outAngle;
    }

    public Point getArcMiddle() {
        return arcMiddle;
    }

    public void setArcMiddle(Point arcMiddle) {
        this.arcMiddle = arcMiddle;
    }

    public int getArcRadius() {
        return arcRadius;
    }

    public void setArcRadius(int arcRadius) {
        this.arcRadius = arcRadius;
    }

    public Point getOutPoint() {
        return outPoint;
    }

    public void setOutPoint(Point outPoint) {
        this.outPoint = outPoint;
    }

    public Point getInPoint() {
        return inPoint;
    }

    public void setInPoint(Point inPoint) {
        this.inPoint = inPoint;
    }

    public Arc2D.Double getArc() {
        return arc;
    }

    public void setArc(Arc2D.Double arc) {
        this.arc = arc;
    }

    public int[] getArrowLine1() {
        return arrowLine1;
    }

    public void setArrowLine1(int[] arrowLine1) {
        this.arrowLine1 = arrowLine1;
    }

    public int[] getArrowLine2() {
        return arrowLine2;
    }

    public void setArrowLine2(int[] arrowLine2) {
        this.arrowLine2 = arrowLine2;
    }
}
