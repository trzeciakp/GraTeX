package pl.edu.agh.gratex.model.labelE;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.utils.DrawingTools;
import pl.edu.agh.gratex.utils.Geometry;
import pl.edu.agh.gratex.view.ControlManager;
import pl.edu.agh.gratex.model.PropertyModel;

import java.awt.*;
import java.io.Serializable;


public class LabelE extends GraphElement implements Serializable {
    private static final long serialVersionUID = 7816741486248743237L;

    // TEST NOWEJ OPERACJI CTRL Z
    @Override
    public boolean equals(Object o2) {
        if (!(o2 instanceof LabelE)) {
            return false;
        }
        LabelE l = (LabelE) o2;

        if (!text.equals(l.text)) {
            return false;
        }
        if (!fontColor.equals(l.fontColor)) {
            return false;
        }
        if (position != l.position) {
            return false;
        }
        if (spacing != l.spacing) {
            return false;
        }
        if (topPlacement != l.topPlacement) {
            return false;
        }
        if (horizontalPlacement != l.horizontalPlacement) {
            return false;
        }
        if (posX != l.posX) {
            return false;
        }
        if (posY != l.posY) {
            return false;
        }
        if (angle != l.angle) {
            return false;
        }
        if (!owner.equals(l.owner)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return owner.hashCode() * 1000000 + posX * 1000 + posY;
    }

    // Wartości edytowalne przez użytkowanika
    private String text;
    private Font font = Const.DEFAULT_FONT;
    private Color fontColor;
    private int position;                                        // Procent przesunięcia na krawędzie
    private int spacing;                                        // odleglość napisu od krawędzi
    private boolean topPlacement;                                    // Czy etykieta jest nad krawędzią
    private boolean horizontalPlacement;                            // Czy etykieta jest zawsze poziomo (nie = równolegle do krawędzi)

    // Wartości potrzebne do parsowania
    private int posX;                                            // Środek stringa x
    private int posY;                                            // Środek stringa y
    private int angle;                                            // Nachylenie (0 stopni gdy pozioma)

    // Pozostałe
    private Edge owner;
    private Polygon outline;
    private int drawX;
    private int drawY;
    private Graph graph;

    public LabelE(Edge element, Graph graph) {
        setOwner(element);
        setText("Label");
        this.graph = graph;
    }

    public LabelE getCopy(Edge owner) {
        LabelE result = new LabelE(owner, graph);
        result.setModel(getModel());

        return result;
    }

    public void setModel(PropertyModel pm) {
        LabelEdgePropertyModel model = (LabelEdgePropertyModel) pm;

        if (model.text != null) {
            setText(model.text);
        }

        if (model.fontColor != null) {
            setFontColor(new Color(model.fontColor.getRGB()));
        }

        if (model.position > -1) {
            setPosition(model.position);
        }

        if (model.spacing > -1) {
            setSpacing(model.spacing);
        }

        if (model.topPlacement > -1) {
            setTopPlacement((model.topPlacement == 1));
        }

        if (model.horizontalPlacement > -1) {
            setHorizontalPlacement((model.horizontalPlacement == 1));
        }
    }

    public PropertyModel getModel() {
        LabelEdgePropertyModel result = new LabelEdgePropertyModel();

        result.text = new String(getText());
        result.fontColor = new Color(getFontColor().getRGB());
        result.position = getPosition();
        result.spacing = getSpacing();
        result.topPlacement = 0;
        result.isLoop = PropertyModel.NO;
        if (getOwner().getVertexA() == getOwner().getVertexB()) {
            result.isLoop = PropertyModel.YES;
        }
        if (isTopPlacement()) {
            result.topPlacement = 1;
        }
        result.horizontalPlacement = 0;
        if (isHorizontalPlacement()) {
            result.horizontalPlacement = 1;
        }

        return result;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.LABEL_EDGE;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    public boolean intersects(int x, int y) {
        return getOutline().contains(x, y);
    }

    public void updatePosition(Graphics2D g) {
        g.setFont(getFont());
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(getText());
        int height = fm.getAscent();
        int descent = fm.getDescent();

        double ellipseShortRadius = 0.75;

        if (getOwner().getVertexA() == getOwner().getVertexB()) {
            setHorizontalPlacement(true);
            setTopPlacement(true);
            if (getPosition() < 34) {
                setPosition(25);
            } else if (getPosition() < 67) {
                setPosition(50);
            } else {
                setPosition(75);
            }

            double offsetRate = 0.75;
            if (getOwner().getVertexA().getShape() == 2) {
                offsetRate = 0.375;
                if (getOwner().getRelativeEdgeAngle() == 270) {
                    ellipseShortRadius /= 2;
                }
            } else if (getOwner().getVertexA().getShape() == 3) {
                offsetRate = 0.5;
            } else if (getOwner().getVertexA().getShape() == 4) {
                offsetRate = 0.4375;
            } else if (getOwner().getVertexA().getShape() == 5) {
                offsetRate = 0.625;
            }

            int r = getOwner().getVertexA().getRadius() + getOwner().getVertexA().getLineWidth() / 2;
            switch (getOwner().getRelativeEdgeAngle()) {
                case 0: {
                    if (getPosition() == 25) {
                        setPosX(getOwner().getVertexA().getPosX() + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        setPosY(getOwner().getVertexA().getPosY() - height / 2 - getSpacing() - (int) Math.round(r * ellipseShortRadius / 2));
                    } else if (getPosition() == 50) {
                        setPosX(getOwner().getVertexA().getPosX() + getSpacing() + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                        setPosY(getOwner().getVertexA().getPosY());
                    } else {
                        setPosX(getOwner().getVertexA().getPosX() + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        setPosY(getOwner().getVertexA().getPosY() + height / 2 + getSpacing() + (int) Math.round(r * ellipseShortRadius / 2));
                    }
                    break;
                }
                case 90: {
                    if (getPosition() == 25) {
                        setPosX(getOwner().getVertexA().getPosX() - getSpacing() - width / 2 - (int) Math.round(r * ellipseShortRadius / 2));
                        setPosY(getOwner().getVertexA().getPosY() - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                    } else if (getPosition() == 50) {
                        setPosX(getOwner().getVertexA().getPosX());
                        setPosY(getOwner().getVertexA().getPosY() - getSpacing() - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                    } else {
                        setPosX(getOwner().getVertexA().getPosX() + getSpacing() + width / 2 + (int) Math.round(r * ellipseShortRadius / 2));
                        setPosY(getOwner().getVertexA().getPosY() - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                    }
                    break;
                }
                case 180: {
                    if (getPosition() == 25) {
                        setPosX(getOwner().getVertexA().getPosX() - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        setPosY(getOwner().getVertexA().getPosY() + getSpacing() + height / 2 + (int) Math.round(r * ellipseShortRadius / 2));
                    } else if (getPosition() == 50) {
                        setPosX(getOwner().getVertexA().getPosX() - getSpacing() - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                        setPosY(getOwner().getVertexA().getPosY());
                    } else {
                        setPosX(getOwner().getVertexA().getPosX() - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        setPosY(getOwner().getVertexA().getPosY() - getSpacing() - height / 2 - (int) Math.round(r * ellipseShortRadius / 2));
                    }
                    break;
                }
                case 270: {
                    if (getPosition() == 25) {
                        setPosX(getOwner().getVertexA().getPosX() + getSpacing() + width / 2 + (int) Math.round(r * ellipseShortRadius / 2));
                        setPosY(getOwner().getVertexA().getPosY() + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                    } else if (getPosition() == 50) {
                        setPosX(getOwner().getVertexA().getPosX());
                        setPosY(getOwner().getVertexA().getPosY() + getSpacing() + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                    } else {
                        setPosX(getOwner().getVertexA().getPosX() - getSpacing() - width / 2 - (int) Math.round(r * ellipseShortRadius / 2));
                        setPosY(getOwner().getVertexA().getPosY() + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        ;
                    }
                    break;
                }
            }
            setDrawX(getPosX() - width / 2);
            setDrawY(getPosY() + height / 2 - descent);
            int[] xp = new int[]{getDrawX(), getDrawX() + width, getDrawX() + width, getDrawX()};
            int[] yp = new int[]{getPosY() - height / 2, getPosY() - height / 2, getPosY() + height / 2, getPosY() + height / 2};
            setOutline(new Polygon(xp, yp, 4));
        } else {
            if (getOwner().getRelativeEdgeAngle() == 0) // straight edge
            {
                int ax = getOwner().getInPoint().x;
                int ay = getOwner().getInPoint().y;
                int bx = getOwner().getOutPoint().x;
                int by = getOwner().getOutPoint().y;
                int dx = bx - ax;
                int dy = by - ay;

                double distance = Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by));

                double baselinePart = (getOwner().getLineWidth() + getSpacing()) / distance;
                double ascentPart = (getOwner().getLineWidth() + getSpacing() + height) / distance;

                Point baseline1p1 = new Point((int) Math.round(ax + dy * baselinePart), (int) Math.round(ay - dx * baselinePart));
                Point baseline1p2 = new Point((int) Math.round(bx + dy * baselinePart), (int) Math.round(by - dx * baselinePart));
                Point baseline2p1 = new Point((int) Math.round(ax - dy * baselinePart), (int) Math.round(ay + dx * baselinePart));
                Point baseline2p2 = new Point((int) Math.round(bx - dy * baselinePart), (int) Math.round(by + dx * baselinePart));

                Point ascent1p1 = new Point((int) Math.round(ax + dy * ascentPart), (int) Math.round(ay - dx * ascentPart));
                Point ascent1p2 = new Point((int) Math.round(bx + dy * ascentPart), (int) Math.round(by - dx * ascentPart));
                Point ascent2p1 = new Point((int) Math.round(ax - dy * ascentPart), (int) Math.round(ay + dx * ascentPart));
                Point ascent2p2 = new Point((int) Math.round(bx - dy * ascentPart), (int) Math.round(by + dx * ascentPart));

                Point startPoint = null;
                Point endPoint = null;
                if (isTopPlacement()) {
                    if (baseline2p1.x < baseline2p2.x) {
                        startPoint = baseline2p1;
                        endPoint = baseline2p2;
                        if (ax < bx) {
                            startPoint = baseline1p1;
                            endPoint = baseline1p2;
                        }
                    } else {
                        startPoint = baseline2p2;
                        endPoint = baseline2p1;
                        if (ax < bx) {
                            startPoint = baseline1p2;
                            endPoint = baseline1p1;
                        }
                    }
                } else {
                    if (ascent2p1.x < ascent2p2.x) {
                        startPoint = ascent2p1;
                        endPoint = ascent2p2;
                        if (ax >= bx) {
                            startPoint = ascent1p1;
                            endPoint = ascent1p2;
                        }
                    } else {
                        startPoint = ascent2p2;
                        endPoint = ascent2p1;
                        if (ax >= bx) {
                            startPoint = ascent1p2;
                            endPoint = ascent1p1;
                        }
                    }
                }

                if (isHorizontalPlacement()) {
                    if (!isTopPlacement()) {
                        if (baseline2p1.x < baseline2p2.x) {
                            startPoint = baseline2p1;
                            endPoint = baseline2p2;
                            if (ax > bx) {
                                startPoint = baseline1p1;
                                endPoint = baseline1p2;
                            }
                        } else {
                            startPoint = baseline2p2;
                            endPoint = baseline2p1;
                            if (ax > bx) {
                                startPoint = baseline1p2;
                                endPoint = baseline1p1;
                            }
                        }
                    }

                    Point stringClosestCorner = new Point((int) Math.round(startPoint.x
                            + (getPosition() * (distance) * (endPoint.x - startPoint.x) / (100 * distance))), (int) Math.round(startPoint.y
                            + (getPosition() * (distance) * (endPoint.y - startPoint.y) / (100 * distance))));

                    if (isTopPlacement()) {
                        if ((ax - bx) * (ay - by) > 0) {
                            setDrawX(stringClosestCorner.x);
                            setDrawY(stringClosestCorner.y - descent);
                        } else {
                            setDrawX(stringClosestCorner.x - width);

                            setDrawY(stringClosestCorner.y - descent);
                        }
                        setPosY(stringClosestCorner.y - height / 2);
                    } else {
                        if ((ax - bx) * (ay - by) > 0) {
                            setDrawX(stringClosestCorner.x - width);
                            setDrawY(stringClosestCorner.y - descent + height);
                        } else {
                            setDrawX(stringClosestCorner.x);
                            setDrawY(stringClosestCorner.y - descent + height);
                        }
                        setPosY(stringClosestCorner.y + height / 2);
                    }

                    if (height % 2 == 1) {
                        height++;
                    }
                    setAngle(0);
                    setPosX(getDrawX() + width / 2);
                    int[] xp = new int[]{getDrawX(), getDrawX() + width, getDrawX() + width, getDrawX()};
                    int[] yp = new int[]{getPosY() - height / 2, getPosY() - height / 2, getPosY() + height / 2, getPosY() + height / 2};
                    setOutline(new Polygon(xp, yp, 4));
                } else {

                    Point stringStartBottom = new Point((int) Math.round(startPoint.x
                            + (getPosition() * (distance - width) * (endPoint.x - startPoint.x) / (100 * distance))), (int) Math.round(startPoint.y
                            + (getPosition() * (distance - width) * (endPoint.y - startPoint.y) / (100 * distance))));

                    Point stringEndBottom = new Point((int) Math.round(startPoint.x
                            + ((width + (getPosition() * (distance - width) / 100)) * (endPoint.x - startPoint.x) / distance)),
                            (int) Math.round(startPoint.y
                                    + ((width + (getPosition() * (distance - width) / 100)) * (endPoint.y - startPoint.y) / distance)));

                    Point stringStartTop = null;
                    Point stringEndTop = null;

                    if (startPoint.y < endPoint.y) {
                        stringStartTop = new Point(stringStartBottom.x + Math.abs(baseline1p1.x - ascent1p1.x), stringStartBottom.y
                                - Math.abs(baseline1p1.y - ascent1p1.y));

                        stringEndTop = new Point(stringEndBottom.x + Math.abs(baseline1p1.x - ascent1p1.x), stringEndBottom.y
                                - Math.abs(baseline1p1.y - ascent1p1.y));

                        setDrawX(stringStartBottom.x + (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.x - ascent1p1.x)));
                        setDrawY(stringStartBottom.y - (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.y - ascent1p1.y)));
                    } else {
                        stringStartTop = new Point(stringStartBottom.x - Math.abs(baseline1p1.x - ascent1p1.x), stringStartBottom.y
                                - Math.abs(baseline1p1.y - ascent1p1.y));

                        stringEndTop = new Point(stringEndBottom.x - Math.abs(baseline1p1.x - ascent1p1.x), stringEndBottom.y
                                - Math.abs(baseline1p1.y - ascent1p1.y));

                        setDrawX(stringStartBottom.x - (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.x - ascent1p1.x)));
                        setDrawY(stringStartBottom.y - (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.y - ascent1p1.y)));
                    }

                    int[] xp = new int[]{stringStartBottom.x, stringEndBottom.x, stringEndTop.x, stringStartTop.x};
                    int[] yp = new int[]{stringStartBottom.y, stringEndBottom.y, stringEndTop.y, stringStartTop.y};
                    setOutline(new Polygon(xp, yp, 4));

                    setPosX((stringStartBottom.x + stringEndTop.x) / 2);
                    setPosY((stringStartBottom.y + stringEndTop.y) / 2);

                    double angled = Math
                            .toDegrees(Math.asin((stringStartBottom.y - stringEndBottom.y) / stringStartBottom.distance(stringEndBottom)));
                    if (angled < 0) {
                        angled += 360;
                    }

                    setAngle((int) Math.round(angled));
                }
            } else
            // curved edge
            {
                if (isHorizontalPlacement()) {
                    double r = 0.0;
                    if (isTopPlacement()) {
                        r = getSpacing() + getOwner().getArcRadius();
                    } else {
                        r = getOwner().getArcRadius() - getSpacing();
                    }
                    double startAngle = (Math.toDegrees(Math.atan2(getOwner().getOutPoint().x - getOwner().getArcMiddle().x, getOwner().getOutPoint().y - getOwner().getArcMiddle().y)) + 270) % 360;
                    double endAngle = (Math.toDegrees(Math.atan2(getOwner().getInPoint().x - getOwner().getArcMiddle().x, getOwner().getInPoint().y - getOwner().getArcMiddle().y)) + 270) % 360;
                    double stringAngle = 0.0;
                    if (getOwner().getRelativeEdgeAngle() <= 60) {
                        stringAngle = (360 + startAngle - (getPosition() * ((startAngle - endAngle + 360) % 360)) / 100) % 360;
                    } else {
                        stringAngle = (startAngle + (getPosition() * ((endAngle - startAngle + 360) % 360)) / 100) % 360;
                    }

                    setPosX(getOwner().getArcMiddle().x + (int) Math.round(r * Math.cos(Math.toRadians(stringAngle))));
                    setPosY(getOwner().getArcMiddle().y - (int) Math.round(r * Math.sin(Math.toRadians(stringAngle))));

                    setAngle(0);
                    if (!isTopPlacement()) {
                        stringAngle = (stringAngle + 180) % 360;
                    }
                    if (stringAngle < 90) {
                        setDrawX(getPosX());
                        setDrawY(getPosY() - descent);
                        setPosX(getPosX() + width / 2);
                        setPosY(getPosY() - height / 2);
                    } else if (stringAngle < 180) {
                        setDrawX(getPosX() - width);
                        setDrawY(getPosY() - descent);
                        setPosX(getPosX() - width / 2);
                        setPosY(getPosY() - height / 2);
                    } else if (stringAngle < 270) {
                        setDrawX(getPosX() - width);
                        setDrawY(getPosY() + height - descent);
                        setPosX(getPosX() - width / 2);
                        setPosY(getPosY() + height / 2);
                    } else {
                        setDrawX(getPosX());
                        setDrawY(getPosY() + height - descent);
                        setPosX(getPosX() + width / 2);
                        setPosY(getPosY() + height / 2);
                    }

                    int xpoints[] = new int[]{getPosX() - width / 2, getPosX() + width / 2, getPosX() + width / 2, getPosX() - width / 2};
                    int ypoints[] = new int[]{getPosY() - height / 2, getPosY() - height / 2, getPosY() + height / 2, getPosY() + height / 2};
                    setOutline(new Polygon(xpoints, ypoints, 4));
                } else {
                    double r = 0.0;
                    if (isTopPlacement()) {
                        r = getSpacing() + height / 2 + getOwner().getArcRadius();
                    } else {
                        r = getOwner().getArcRadius() - getSpacing() - height / 2;
                    }
                    double alpha = Math.toDegrees(Math.atan(width / (2 * r)));
                    double startAngle = (Math.toDegrees(Math.atan2(getOwner().getOutPoint().x - getOwner().getArcMiddle().x, getOwner().getOutPoint().y - getOwner().getArcMiddle().y)) + 270) % 360;
                    double endAngle = (Math.toDegrees(Math.atan2(getOwner().getInPoint().x - getOwner().getArcMiddle().x, getOwner().getInPoint().y - getOwner().getArcMiddle().y)) + 270) % 360;
                    double stringAngle = 0.0;
                    if (getOwner().getRelativeEdgeAngle() <= 60) {
                        startAngle = (startAngle - alpha) % 360;
                        endAngle = (endAngle + alpha) % 360;
                        stringAngle = (360 + startAngle - (getPosition() * ((startAngle - endAngle + 360) % 360)) / 100) % 360;
                    } else {
                        startAngle = (startAngle + alpha) % 360;
                        endAngle = (endAngle - alpha) % 360;
                        stringAngle = startAngle + (getPosition() * ((endAngle - startAngle + 360) % 360)) / 100;
                    }

                    setAngle((int) Math.round((stringAngle + 270) % 360));
                    if (getAngle() > 90 && getAngle() < 271) {
                        setAngle((180 + getAngle()) % 360);
                    }

                    setPosX(getOwner().getArcMiddle().x + (int) Math.round(r * Math.cos(Math.toRadians(stringAngle))));
                    setPosY(getOwner().getArcMiddle().y - (int) Math.round(r * Math.sin(Math.toRadians(stringAngle))));

                    Point middle = new Point(getPosX(), getPosY());
                    Point draw = new Point(getPosX() - width / 2, getPosY() + height / 2 - descent);
                    draw = Geometry.rotatePoint(middle, draw, getAngle());
                    Point p1 = new Point(getPosX() - width / 2, getPosY() - height / 2);
                    p1 = Geometry.rotatePoint(middle, p1, getAngle());
                    Point p2 = new Point(getPosX() + width / 2, getPosY() - height / 2);
                    p2 = Geometry.rotatePoint(middle, p2, getAngle());
                    Point p3 = new Point(getPosX() + width / 2, getPosY() + height / 2);
                    p3 = Geometry.rotatePoint(middle, p3, getAngle());
                    Point p4 = new Point(getPosX() - width / 2, getPosY() + height / 2);
                    p4 = Geometry.rotatePoint(middle, p4, getAngle());

                    setDrawX(draw.x);
                    setDrawY(draw.y);
                    int xpoints[] = new int[]{p1.x, p2.x, p3.x, p4.x};
                    int ypoints[] = new int[]{p1.y, p2.y, p3.y, p4.y};
                    setOutline(new Polygon(xpoints, ypoints, 4));
                }
            }
        }
    }

    public void draw(Graphics2D g2d, boolean dummy) {
        Graphics2D g = (Graphics2D) g2d.create();

        updatePosition(g);

        if (ControlManager.selection.contains(this)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fillPolygon(getOutline());
        }

        g.setColor(getFontColor());
        if (dummy) {
            g.setColor(DrawingTools.getDummyColor(getFontColor()));
        }
        g.setFont(getFont());
        if (isHorizontalPlacement()) {
            g.drawString(getText(), getDrawX(), getDrawY());
        } else {
            g.translate(getDrawX(), getDrawY());
            g.rotate(Math.toRadians(360 - getAngle()), 0, 0);
            g.drawString(getText(), 0, 0);
        }

        g.dispose();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public boolean isTopPlacement() {
        return topPlacement;
    }

    public void setTopPlacement(boolean topPlacement) {
        this.topPlacement = topPlacement;
    }

    public boolean isHorizontalPlacement() {
        return horizontalPlacement;
    }

    public void setHorizontalPlacement(boolean horizontalPlacement) {
        this.horizontalPlacement = horizontalPlacement;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public Edge getOwner() {
        return owner;
    }

    public void setOwner(Edge owner) {
        this.owner = owner;
    }

    public Polygon getOutline() {
        return outline;
    }

    public void setOutline(Polygon outline) {
        this.outline = outline;
    }

    public int getDrawX() {
        return drawX;
    }

    public void setDrawX(int drawX) {
        this.drawX = drawX;
    }

    public int getDrawY() {
        return drawY;
    }

    public void setDrawY(int drawY) {
        this.drawY = drawY;
    }
}
