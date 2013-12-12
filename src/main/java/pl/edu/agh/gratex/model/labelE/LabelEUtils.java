package pl.edu.agh.gratex.model.labelE;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.utils.DrawingTools;
import pl.edu.agh.gratex.utils.Geometry;

import java.awt.*;

public class LabelEUtils {
    public static boolean intersects(LabelE labelE, int x, int y) {
        return labelE.getOutline().contains(x, y);
    }

    public static void draw(LabelE labelE, Graphics2D g2d, boolean dummy) {
        Graphics2D g = (Graphics2D) g2d.create();

        updatePosition(labelE, g);

        if (labelE.getGraph().getGeneralController().getSelectionController().selectionContains(labelE)) {
            g.setColor(Const.SELECTION_COLOR);
            g.fillPolygon(labelE.getOutline());
        }

        g.setColor(labelE.getFontColor());
        if (dummy) {
            g.setColor(DrawingTools.getDummyColor(labelE.getFontColor()));
        }
        g.setFont(labelE.getFont());
        if (labelE.isHorizontalPlacement()) {
            g.drawString(labelE.getText(), labelE.getDrawX(), labelE.getDrawY());
        } else {
            g.translate(labelE.getDrawX(), labelE.getDrawY());
            g.rotate(Math.toRadians(360 - labelE.getAngle()), 0, 0);
            g.drawString(labelE.getText(), 0, 0);
        }

        g.dispose();
    }

    public static void updatePosition(LabelE labelE, Graphics2D g) {
        g.setFont(labelE.getFont());
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(labelE.getText());
        int height = fm.getAscent();
        int descent = fm.getDescent();

        double ellipseShortRadius = 0.75;

        Edge owner = labelE.getOwner();
        int position = labelE.getPosition();
        int spacing = labelE.getSpacing();
        int posX = labelE.getPosX();
        int posY = labelE.getPosY();
        int drawX = labelE.getDrawX();
        int angle = labelE.getAngle();


        if (owner.getVertexA() == owner.getVertexB()) {
            labelE.setHorizontalPlacement(true);
            labelE.setTopPlacement(true);
            if (position < 34) {
                labelE.setPosition(25);
            } else if (position < 67) {
                labelE.setPosition(50);
            } else {
                labelE.setPosition(75);
            }

            double offsetRate = 0.75;
            if (owner.getVertexA().getShape() == 2) {
                offsetRate = 0.375;
                if (owner.getRelativeEdgeAngle() == 270) {
                    ellipseShortRadius /= 2;
                }
            } else if (owner.getVertexA().getShape() == 3) {
                offsetRate = 0.5;
            } else if (owner.getVertexA().getShape() == 4) {
                offsetRate = 0.4375;
            } else if (owner.getVertexA().getShape() == 5) {
                offsetRate = 0.625;
            }

            int r = owner.getVertexA().getRadius() + owner.getVertexA().getLineWidth() / 2;
            switch (owner.getRelativeEdgeAngle()) {
                case 0: {
                    if (position == 25) {
                        labelE.setPosX(owner.getVertexA().getPosX() + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        labelE.setPosY(owner.getVertexA().getPosY() - height / 2 - spacing - (int) Math.round(r * ellipseShortRadius / 2));
                    } else if (position == 50) {
                        labelE.setPosX(owner.getVertexA().getPosX() + spacing + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                        labelE.setPosY(owner.getVertexA().getPosY());
                    } else {
                        labelE.setPosX(owner.getVertexA().getPosX() + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        labelE.setPosY(owner.getVertexA().getPosY() + height / 2 + spacing + (int) Math.round(r * ellipseShortRadius / 2));
                    }
                    break;
                }
                case 90: {
                    if (position == 25) {
                        labelE.setPosX(owner.getVertexA().getPosX() - spacing - width / 2 - (int) Math.round(r * ellipseShortRadius / 2));
                        labelE.setPosY(owner.getVertexA().getPosY() - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                    } else if (position == 50) {
                        labelE.setPosX(owner.getVertexA().getPosX());
                        labelE.setPosY(owner.getVertexA().getPosY() - spacing - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                    } else {
                        labelE.setPosX(owner.getVertexA().getPosX() + spacing + width / 2 + (int) Math.round(r * ellipseShortRadius / 2));
                        labelE.setPosY(owner.getVertexA().getPosY() - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                    }
                    break;
                }
                case 180: {
                    if (position == 25) {
                        labelE.setPosX(owner.getVertexA().getPosX() - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        labelE.setPosY(owner.getVertexA().getPosY() + spacing + height / 2 + (int) Math.round(r * ellipseShortRadius / 2));
                    } else if (position == 50) {
                        labelE.setPosX(owner.getVertexA().getPosX() - spacing - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                        labelE.setPosY(owner.getVertexA().getPosY());
                    } else {
                        labelE.setPosX(owner.getVertexA().getPosX() - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        labelE.setPosY(owner.getVertexA().getPosY() - spacing - height / 2 - (int) Math.round(r * ellipseShortRadius / 2));
                    }
                    break;
                }
                case 270: {
                    if (position == 25) {
                        labelE.setPosX(owner.getVertexA().getPosX() + spacing + width / 2 + (int) Math.round(r * ellipseShortRadius / 2));
                        labelE.setPosY(owner.getVertexA().getPosY() + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                    } else if (position == 50) {
                        labelE.setPosX(owner.getVertexA().getPosX());
                        labelE.setPosY(owner.getVertexA().getPosY() + spacing + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                    } else {
                        labelE.setPosX(owner.getVertexA().getPosX() - spacing - width / 2 - (int) Math.round(r * ellipseShortRadius / 2));
                        labelE.setPosY(owner.getVertexA().getPosY() + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        ;
                    }
                    break;
                }
            }
            labelE.setDrawX(posX - width / 2);
            labelE.setDrawY(posY + height / 2 - descent);
            int[] xp = new int[]{drawX, drawX + width, drawX + width, drawX};
            int[] yp = new int[]{posY - height / 2, posY - height / 2, posY + height / 2, posY + height / 2};
            labelE.setOutline(new Polygon(xp, yp, 4));
        } else {
            if (owner.getRelativeEdgeAngle() == 0) // straight edge
            {
                int ax = owner.getInPoint().x;
                int ay = owner.getInPoint().y;
                int bx = owner.getOutPoint().x;
                int by = owner.getOutPoint().y;
                int dx = bx - ax;
                int dy = by - ay;

                double distance = Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by));

                double baselinePart = (owner.getLineWidth() + spacing) / distance;
                double ascentPart = (owner.getLineWidth() + spacing + height) / distance;

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
                if (labelE.isTopPlacement()) {
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

                if (labelE.isHorizontalPlacement()) {
                    if (!labelE.isTopPlacement()) {
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
                            + (position * (distance) * (endPoint.x - startPoint.x) / (100 * distance))), (int) Math.round(startPoint.y
                            + (position * (distance) * (endPoint.y - startPoint.y) / (100 * distance))));

                    if (labelE.isTopPlacement()) {
                        if ((ax - bx) * (ay - by) > 0) {
                            labelE.setDrawX(stringClosestCorner.x);
                            labelE.setDrawY(stringClosestCorner.y - descent);
                        } else {
                            labelE.setDrawX(stringClosestCorner.x - width);

                            labelE.setDrawY(stringClosestCorner.y - descent);
                        }
                        labelE.setPosY(stringClosestCorner.y - height / 2);
                    } else {
                        if ((ax - bx) * (ay - by) > 0) {
                            labelE.setDrawX(stringClosestCorner.x - width);
                            labelE.setDrawY(stringClosestCorner.y - descent + height);
                        } else {
                            labelE.setDrawX(stringClosestCorner.x);
                            labelE.setDrawY(stringClosestCorner.y - descent + height);
                        }
                        labelE.setPosY(stringClosestCorner.y + height / 2);
                    }

                    if (height % 2 == 1) {
                        height++;
                    }
                    labelE.setAngle(0);
                    labelE.setPosX(drawX + width / 2);
                    int[] xp = new int[]{drawX, drawX + width, drawX + width, drawX};
                    int[] yp = new int[]{posY - height / 2, posY - height / 2, posY + height / 2, posY + height / 2};
                    labelE.setOutline(new Polygon(xp, yp, 4));
                } else {

                    Point stringStartBottom = new Point((int) Math.round(startPoint.x
                            + (position * (distance - width) * (endPoint.x - startPoint.x) / (100 * distance))), (int) Math.round(startPoint.y
                            + (position * (distance - width) * (endPoint.y - startPoint.y) / (100 * distance))));

                    Point stringEndBottom = new Point((int) Math.round(startPoint.x
                            + ((width + (position * (distance - width) / 100)) * (endPoint.x - startPoint.x) / distance)),
                            (int) Math.round(startPoint.y
                                    + ((width + (position * (distance - width) / 100)) * (endPoint.y - startPoint.y) / distance)));

                    Point stringStartTop = null;
                    Point stringEndTop = null;

                    if (startPoint.y < endPoint.y) {
                        stringStartTop = new Point(stringStartBottom.x + Math.abs(baseline1p1.x - ascent1p1.x), stringStartBottom.y
                                - Math.abs(baseline1p1.y - ascent1p1.y));

                        stringEndTop = new Point(stringEndBottom.x + Math.abs(baseline1p1.x - ascent1p1.x), stringEndBottom.y
                                - Math.abs(baseline1p1.y - ascent1p1.y));

                        labelE.setDrawX(stringStartBottom.x + (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.x - ascent1p1.x)));
                        labelE.setDrawY(stringStartBottom.y - (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.y - ascent1p1.y)));
                    } else {
                        stringStartTop = new Point(stringStartBottom.x - Math.abs(baseline1p1.x - ascent1p1.x), stringStartBottom.y
                                - Math.abs(baseline1p1.y - ascent1p1.y));

                        stringEndTop = new Point(stringEndBottom.x - Math.abs(baseline1p1.x - ascent1p1.x), stringEndBottom.y
                                - Math.abs(baseline1p1.y - ascent1p1.y));

                        labelE.setDrawX(stringStartBottom.x - (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.x - ascent1p1.x)));
                        labelE.setDrawY(stringStartBottom.y - (int) Math.round((double) descent / (double) height * Math.abs(baseline1p1.y - ascent1p1.y)));
                    }

                    int[] xp = new int[]{stringStartBottom.x, stringEndBottom.x, stringEndTop.x, stringStartTop.x};
                    int[] yp = new int[]{stringStartBottom.y, stringEndBottom.y, stringEndTop.y, stringStartTop.y};
                    labelE.setOutline(new Polygon(xp, yp, 4));

                    labelE.setPosX((stringStartBottom.x + stringEndTop.x) / 2);
                    labelE.setPosY((stringStartBottom.y + stringEndTop.y) / 2);

                    double angled = Math
                            .toDegrees(Math.asin((stringStartBottom.y - stringEndBottom.y) / stringStartBottom.distance(stringEndBottom)));
                    if (angled < 0) {
                        angled += 360;
                    }

                    labelE.setAngle((int) Math.round(angled));
                }
            } else
            // curved edge
            {
                if (labelE.isHorizontalPlacement()) {
                    double r = 0.0;
                    if (labelE.isTopPlacement()) {
                        r = spacing + owner.getArcRadius();
                    } else {
                        r = owner.getArcRadius() - spacing;
                    }
                    double startAngle = (Math.toDegrees(Math.atan2(owner.getOutPoint().x - owner.getArcMiddle().x, owner.getOutPoint().y - owner.getArcMiddle().y)) + 270) % 360;
                    double endAngle = (Math.toDegrees(Math.atan2(owner.getInPoint().x - owner.getArcMiddle().x, owner.getInPoint().y - owner.getArcMiddle().y)) + 270) % 360;
                    double stringAngle = 0.0;
                    if (owner.getRelativeEdgeAngle() <= 60) {
                        stringAngle = (360 + startAngle - (position * ((startAngle - endAngle + 360) % 360)) / 100) % 360;
                    } else {
                        stringAngle = (startAngle + (position * ((endAngle - startAngle + 360) % 360)) / 100) % 360;
                    }

                    labelE.setPosX(owner.getArcMiddle().x + (int) Math.round(r * Math.cos(Math.toRadians(stringAngle))));
                    labelE.setPosY(owner.getArcMiddle().y - (int) Math.round(r * Math.sin(Math.toRadians(stringAngle))));

                    labelE.setAngle(0);
                    if (!labelE.isTopPlacement()) {
                        stringAngle = (stringAngle + 180) % 360;
                    }
                    if (stringAngle < 90) {
                        labelE.setDrawX(posX);
                        labelE.setDrawY(posY - descent);
                        labelE.setPosX(posX + width / 2);
                        labelE.setPosY(posY - height / 2);
                    } else if (stringAngle < 180) {
                        labelE.setDrawX(posX - width);
                        labelE.setDrawY(posY - descent);
                        labelE.setPosX(posX - width / 2);
                        labelE.setPosY(posY - height / 2);
                    } else if (stringAngle < 270) {
                        labelE.setDrawX(posX - width);
                        labelE.setDrawY(posY + height - descent);
                        labelE.setPosX(posX - width / 2);
                        labelE.setPosY(posY + height / 2);
                    } else {
                        labelE.setDrawX(posX);
                        labelE.setDrawY(posY + height - descent);
                        labelE.setPosX(posX + width / 2);
                        labelE.setPosY(posY + height / 2);
                    }

                    int xpoints[] = new int[]{posX - width / 2, posX + width / 2, posX + width / 2, posX - width / 2};
                    int ypoints[] = new int[]{posY - height / 2, posY - height / 2, posY + height / 2, posY + height / 2};
                    labelE.setOutline(new Polygon(xpoints, ypoints, 4));
                } else {
                    double r = 0.0;
                    if (labelE.isTopPlacement()) {
                        r = spacing + height / 2 + owner.getArcRadius();
                    } else {
                        r = owner.getArcRadius() - spacing - height / 2;
                    }
                    double alpha = Math.toDegrees(Math.atan(width / (2 * r)));
                    double startAngle = (Math.toDegrees(Math.atan2(owner.getOutPoint().x - owner.getArcMiddle().x, owner.getOutPoint().y - owner.getArcMiddle().y)) + 270) % 360;
                    double endAngle = (Math.toDegrees(Math.atan2(owner.getInPoint().x - owner.getArcMiddle().x, owner.getInPoint().y - owner.getArcMiddle().y)) + 270) % 360;
                    double stringAngle = 0.0;
                    if (owner.getRelativeEdgeAngle() <= 60) {
                        startAngle = (startAngle - alpha) % 360;
                        endAngle = (endAngle + alpha) % 360;
                        stringAngle = (360 + startAngle - (position * ((startAngle - endAngle + 360) % 360)) / 100) % 360;
                    } else {
                        startAngle = (startAngle + alpha) % 360;
                        endAngle = (endAngle - alpha) % 360;
                        stringAngle = startAngle + (position * ((endAngle - startAngle + 360) % 360)) / 100;
                    }

                    labelE.setAngle((int) Math.round((stringAngle + 270) % 360));
                    if (angle > 90 && angle < 271) {
                        labelE.setAngle((180 + angle) % 360);
                    }

                    labelE.setPosX(owner.getArcMiddle().x + (int) Math.round(r * Math.cos(Math.toRadians(stringAngle))));
                    labelE.setPosY(owner.getArcMiddle().y - (int) Math.round(r * Math.sin(Math.toRadians(stringAngle))));

                    Point middle = new Point(posX, posY);
                    Point draw = new Point(posX - width / 2, posY + height / 2 - descent);
                    draw = Geometry.rotatePoint(middle, draw, angle);
                    Point p1 = new Point(posX - width / 2, posY - height / 2);
                    p1 = Geometry.rotatePoint(middle, p1, angle);
                    Point p2 = new Point(posX + width / 2, posY - height / 2);
                    p2 = Geometry.rotatePoint(middle, p2, angle);
                    Point p3 = new Point(posX + width / 2, posY + height / 2);
                    p3 = Geometry.rotatePoint(middle, p3, angle);
                    Point p4 = new Point(posX - width / 2, posY + height / 2);
                    p4 = Geometry.rotatePoint(middle, p4, angle);

                    labelE.setDrawX(draw.x);
                    labelE.setDrawY(draw.y);
                    int xpoints[] = new int[]{p1.x, p2.x, p3.x, p4.x};
                    int ypoints[] = new int[]{p1.y, p2.y, p3.y, p4.y};
                    labelE.setOutline(new Polygon(xpoints, ypoints, 4));
                }
            }
        }
    }
}