package pl.edu.agh.gratex.model.labelE;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.properties.LabelRotation;
import pl.edu.agh.gratex.model.properties.LabelTopPlacement;
import pl.edu.agh.gratex.utils.Geometry;

import java.awt.*;
import java.awt.geom.Line2D;

public class LabelEUtils {
    // Returns true if (x, y) is in the area occupied by the label
    public static boolean intersects(LabelE labelE, int x, int y) {
        return labelE.getOutline().contains(x, y);
    }
/*

    public static void draw(LabelE labelE, Graphics2D g2d, boolean dummy) {
        Graphics2D g = (Graphics2D) g2d.createEmptyModel();

        updateLocation(labelE);

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
*/

    // Calculates the position of LabelE (in % of bias) according to vertex and cursor location
    // Will return (-1 * position) if the label was placed below the edge
    public static int getPositionFromCursorLocation(Edge owner, int mouseX, int mouseY) {
        int bias;
        boolean topPlacement;

        if (owner.getVertexA() == owner.getVertexB()) {
            // Owner edge is a loop
            double angle = -Math.toDegrees(Math.atan2(mouseX - owner.getArcMiddle().x, mouseY - owner.getArcMiddle().y)) + 270 + owner.getRelativeEdgeAngle();
            return (int) Math.round((angle % 360) / 3.6);

        } else {
            // Owner edge is not a loop
            if (owner.getRelativeEdgeAngle() == 0) {
                // Owner edge is not curved
                Point p1 = owner.getInPoint();
                Point p2 = owner.getOutPoint();

                if (p2.x < p1.x) {
                    Point tempP = p2;
                    p2 = p1;
                    p1 = tempP;
                }

                Point c = new Point(mouseX, mouseY);
                double p1p2 = p1.distance(p2);
                double p1c = p1.distance(c);
                double p2c = p2.distance(c);
                double mc = Line2D.ptLineDist((double) p1.x, (double) p1.y, (double) p2.x, (double) p2.y, (double) mouseX, (double) mouseY);

                topPlacement = ((p2.x - p1.x) * (c.y - p1.y) - (p2.y - p1.y) * (c.x - p1.x) < 0);

                if (p1c * p1c > p2c * p2c + p1p2 * p1p2) {
                    bias = 100;
                } else if (p2c * p2c > p1c * p1c + p1p2 * p1p2) {
                    bias = 0;
                } else {
                    bias = (int) Math.round(100 * Math.sqrt(p1c * p1c - mc * mc) / p1p2);
                }
                return (topPlacement ? 1 : -1) * bias;

            } else {
                // Owner edge is curved
                double startAngle = (Math.toDegrees(Math
                        .atan2(owner.getOutPoint().x - owner.getArcMiddle().x, owner.getOutPoint().y - owner.getArcMiddle().y)) + 270) % 360;
                double endAngle = (Math.toDegrees(Math.atan2(owner.getInPoint().x - owner.getArcMiddle().x, owner.getInPoint().y - owner.getArcMiddle().y)) + 270) % 360;
                double mouseAngle = (Math.toDegrees(Math.atan2(mouseX - owner.getArcMiddle().x, mouseY - owner.getArcMiddle().y)) + 270) % 360;

                int position;
                double alpha = (startAngle - mouseAngle + 360) % 360;
                if (alpha > 180) {
                    alpha -= 360;
                }
                double beta = (startAngle - endAngle + 360) % 360;
                if (beta > 180) {
                    beta -= 360;
                }

                position = (int) Math.round(100 * (alpha / beta));
                position = Math.min(100, position);
                position = Math.max(0, position);
                topPlacement = (owner.getArcMiddle().distance(new Point(mouseX, mouseY)) > owner.getArcRadius());
                return (topPlacement ? 1 : -1) * position;
            }
        }
    }

    // Calculates the location of LabelE and attributes needed for parsing
    public static void updateLocation(LabelE labelE) {
        FontMetrics fm = new Canvas().getFontMetrics(Const.DEFAULT_FONT);
        int width = fm.stringWidth(labelE.getText());
        int height = fm.getAscent();
        int descent = fm.getDescent();

        double ellipseShortRadius = 0.75;

        Edge owner = labelE.getOwner();

        if (owner.getVertexA() == owner.getVertexB()) {
            labelE.setHorizontalPlacement(LabelRotation.LEVEL);
            labelE.setTopPlacement(LabelTopPlacement.ABOVE);
            if (labelE.getPosition() < 34) {
                labelE.setPosition(25);
            } else if (labelE.getPosition() < 67) {
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
                    if (labelE.getPosition() == 25) {
                        labelE.setPosX(owner.getVertexA().getPosX() + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        labelE.setPosY(owner.getVertexA().getPosY() - height / 2 - labelE.getSpacing() - (int) Math.round(r * ellipseShortRadius / 2));
                    } else if (labelE.getPosition() == 50) {
                        labelE.setPosX(owner.getVertexA().getPosX() + labelE.getSpacing() + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                        labelE.setPosY(owner.getVertexA().getPosY());
                    } else {
                        labelE.setPosX(owner.getVertexA().getPosX() + width / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        labelE.setPosY(owner.getVertexA().getPosY() + height / 2 + labelE.getSpacing() + (int) Math.round(r * ellipseShortRadius / 2));
                    }
                    break;
                }
                case 90: {
                    if (labelE.getPosition() == 25) {
                        labelE.setPosX(owner.getVertexA().getPosX() - labelE.getSpacing() - width / 2 - (int) Math.round(r * ellipseShortRadius / 2));
                        labelE.setPosY(owner.getVertexA().getPosY() - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                    } else if (labelE.getPosition() == 50) {
                        labelE.setPosX(owner.getVertexA().getPosX());
                        labelE.setPosY(owner.getVertexA().getPosY() - labelE.getSpacing() - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                    } else {
                        labelE.setPosX(owner.getVertexA().getPosX() + labelE.getSpacing() + width / 2 + (int) Math.round(r * ellipseShortRadius / 2));
                        labelE.setPosY(owner.getVertexA().getPosY() - height / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                    }
                    break;
                }
                case 180: {
                    if (labelE.getPosition() == 25) {
                        labelE.setPosX(owner.getVertexA().getPosX() - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        labelE.setPosY(owner.getVertexA().getPosY() + labelE.getSpacing() + height / 2 + (int) Math.round(r * ellipseShortRadius / 2));
                    } else if (labelE.getPosition() == 50) {
                        labelE.setPosX(owner.getVertexA().getPosX() - labelE.getSpacing() - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                        labelE.setPosY(owner.getVertexA().getPosY());
                    } else {
                        labelE.setPosX(owner.getVertexA().getPosX() - width / 2 - (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                        labelE.setPosY(owner.getVertexA().getPosY() - labelE.getSpacing() - height / 2 - (int) Math.round(r * ellipseShortRadius / 2));
                    }
                    break;
                }
                case 270: {
                    if (labelE.getPosition() == 25) {
                        labelE.setPosX(owner.getVertexA().getPosX() + labelE.getSpacing() + width / 2 + (int) Math.round(r * ellipseShortRadius / 2));
                        labelE.setPosY(owner.getVertexA().getPosY() + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                    } else if (labelE.getPosition() == 50) {
                        labelE.setPosX(owner.getVertexA().getPosX());
                        labelE.setPosY(owner.getVertexA().getPosY() + labelE.getSpacing() + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius * 2)));
                    } else {
                        labelE.setPosX(owner.getVertexA().getPosX() - labelE.getSpacing() - width / 2 - (int) Math.round(r * ellipseShortRadius / 2));
                        labelE.setPosY(owner.getVertexA().getPosY() + height / 2 + (int) Math.round(r * (offsetRate + ellipseShortRadius)));
                    }
                    break;
                }
            }
            labelE.setDrawX(labelE.getPosX() - width / 2);
            labelE.setDrawY(labelE.getPosY() + height / 2 - descent);
            int[] xp = new int[]{labelE.getDrawX(), labelE.getDrawX() + width, labelE.getDrawX() + width, labelE.getDrawX()};
            int[] yp = new int[]{labelE.getPosY() - height / 2, labelE.getPosY() - height / 2, labelE.getPosY() + height / 2, labelE.getPosY() + height / 2};
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

                double baselinePart = (owner.getLineWidth() + labelE.getSpacing()) / distance;
                double ascentPart = (owner.getLineWidth() + labelE.getSpacing() + height) / distance;

                Point baseline1p1 = new Point((int) Math.round(ax + dy * baselinePart), (int) Math.round(ay - dx * baselinePart));
                Point baseline1p2 = new Point((int) Math.round(bx + dy * baselinePart), (int) Math.round(by - dx * baselinePart));
                Point baseline2p1 = new Point((int) Math.round(ax - dy * baselinePart), (int) Math.round(ay + dx * baselinePart));
                Point baseline2p2 = new Point((int) Math.round(bx - dy * baselinePart), (int) Math.round(by + dx * baselinePart));

                Point ascent1p1 = new Point((int) Math.round(ax + dy * ascentPart), (int) Math.round(ay - dx * ascentPart));
                Point ascent1p2 = new Point((int) Math.round(bx + dy * ascentPart), (int) Math.round(by - dx * ascentPart));
                Point ascent2p1 = new Point((int) Math.round(ax - dy * ascentPart), (int) Math.round(ay + dx * ascentPart));
                Point ascent2p2 = new Point((int) Math.round(bx - dy * ascentPart), (int) Math.round(by + dx * ascentPart));

                Point startPoint;
                Point endPoint;
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
                            + (labelE.getPosition() * (distance) * (endPoint.x - startPoint.x) / (100 * distance))), (int) Math.round(startPoint.y
                            + (labelE.getPosition() * (distance) * (endPoint.y - startPoint.y) / (100 * distance))));

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
                    labelE.setPosX(labelE.getDrawX() + width / 2);
                    int[] xp = new int[]{labelE.getDrawX(), labelE.getDrawX() + width, labelE.getDrawX() + width, labelE.getDrawX()};
                    int[] yp = new int[]{labelE.getPosY() - height / 2, labelE.getPosY() - height / 2, labelE.getPosY() + height / 2, labelE.getPosY() + height / 2};
                    labelE.setOutline(new Polygon(xp, yp, 4));
                } else {

                    Point stringStartBottom = new Point((int) Math.round(startPoint.x
                            + (labelE.getPosition() * (distance - width) * (endPoint.x - startPoint.x) / (100 * distance))), (int) Math.round(startPoint.y
                            + (labelE.getPosition() * (distance - width) * (endPoint.y - startPoint.y) / (100 * distance))));

                    Point stringEndBottom = new Point((int) Math.round(startPoint.x
                            + ((width + (labelE.getPosition() * (distance - width) / 100)) * (endPoint.x - startPoint.x) / distance)),
                            (int) Math.round(startPoint.y
                                    + ((width + (labelE.getPosition() * (distance - width) / 100)) * (endPoint.y - startPoint.y) / distance)));

                    Point stringStartTop;
                    Point stringEndTop;

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

                    double angle = Math.toDegrees(Math.asin((stringStartBottom.y - stringEndBottom.y) / stringStartBottom.distance(stringEndBottom)));
                    angle = (angle + 360) % 360;

                    labelE.setAngle((int) Math.round(angle));
                }
            } else
            // curved edge
            {
                if (labelE.isHorizontalPlacement()) {
                    double r;
                    if (labelE.isTopPlacement()) {
                        r = labelE.getSpacing() + owner.getArcRadius();
                    } else {
                        r = owner.getArcRadius() - labelE.getSpacing();
                    }
                    double startAngle = (Math.toDegrees(Math.atan2(owner.getOutPoint().x - owner.getArcMiddle().x, owner.getOutPoint().y - owner.getArcMiddle().y)) + 270) % 360;
                    double endAngle = (Math.toDegrees(Math.atan2(owner.getInPoint().x - owner.getArcMiddle().x, owner.getInPoint().y - owner.getArcMiddle().y)) + 270) % 360;
                    double stringAngle;
                    if (owner.getRelativeEdgeAngle() <= 60) {
                        stringAngle = (360 + startAngle - (labelE.getPosition() * ((startAngle - endAngle + 360) % 360)) / 100) % 360;
                    } else {
                        stringAngle = (startAngle + (labelE.getPosition() * ((endAngle - startAngle + 360) % 360)) / 100) % 360;
                    }

                    labelE.setPosX(owner.getArcMiddle().x + (int) Math.round(r * Math.cos(Math.toRadians(stringAngle))));
                    labelE.setPosY(owner.getArcMiddle().y - (int) Math.round(r * Math.sin(Math.toRadians(stringAngle))));

                    labelE.setAngle(0);
                    if (!labelE.isTopPlacement()) {
                        stringAngle = (stringAngle + 180) % 360;
                    }
                    if (stringAngle < 90) {
                        labelE.setDrawX(labelE.getPosX());
                        labelE.setDrawY(labelE.getPosY() - descent);
                        labelE.setPosX(labelE.getPosX() + width / 2);
                        labelE.setPosY(labelE.getPosY() - height / 2);
                    } else if (stringAngle < 180) {
                        labelE.setDrawX(labelE.getPosX() - width);
                        labelE.setDrawY(labelE.getPosY() - descent);
                        labelE.setPosX(labelE.getPosX() - width / 2);
                        labelE.setPosY(labelE.getPosY() - height / 2);
                    } else if (stringAngle < 270) {
                        labelE.setDrawX(labelE.getPosX() - width);
                        labelE.setDrawY(labelE.getPosY() + height - descent);
                        labelE.setPosX(labelE.getPosX() - width / 2);
                        labelE.setPosY(labelE.getPosY() + height / 2);
                    } else {
                        labelE.setDrawX(labelE.getPosX());
                        labelE.setDrawY(labelE.getPosY() + height - descent);
                        labelE.setPosX(labelE.getPosX() + width / 2);
                        labelE.setPosY(labelE.getPosY() + height / 2);
                    }

                    int xPoints[] = new int[]{labelE.getPosX() - width / 2, labelE.getPosX() + width / 2, labelE.getPosX() + width / 2, labelE.getPosX() - width / 2};
                    int yPoints[] = new int[]{labelE.getPosY() - height / 2, labelE.getPosY() - height / 2, labelE.getPosY() + height / 2, labelE.getPosY() + height / 2};
                    labelE.setOutline(new Polygon(xPoints, yPoints, 4));
                } else {
                    double r;
                    if (labelE.isTopPlacement()) {
                        r = labelE.getSpacing() + height / 2 + owner.getArcRadius();
                    } else {
                        r = owner.getArcRadius() - labelE.getSpacing() - height / 2;
                    }
                    double alpha = Math.toDegrees(Math.atan(width / (2 * r)));
                    double startAngle = (Math.toDegrees(Math.atan2(owner.getOutPoint().x - owner.getArcMiddle().x, owner.getOutPoint().y - owner.getArcMiddle().y)) + 270) % 360;
                    double endAngle = (Math.toDegrees(Math.atan2(owner.getInPoint().x - owner.getArcMiddle().x, owner.getInPoint().y - owner.getArcMiddle().y)) + 270) % 360;
                    double stringAngle;
                    if (owner.getRelativeEdgeAngle() <= 60) {
                        startAngle = (startAngle - alpha) % 360;
                        endAngle = (endAngle + alpha) % 360;
                        stringAngle = (360 + startAngle - (labelE.getPosition() * ((startAngle - endAngle + 360) % 360)) / 100) % 360;
                    } else {
                        startAngle = (startAngle + alpha) % 360;
                        endAngle = (endAngle - alpha) % 360;
                        stringAngle = startAngle + (labelE.getPosition() * ((endAngle - startAngle + 360) % 360)) / 100;
                    }

                    labelE.setAngle((int) Math.round((stringAngle + 270) % 360));
                    if (labelE.getAngle() > 90 && labelE.getAngle() < 271) {
                        labelE.setAngle((180 + labelE.getAngle()) % 360);
                    }

                    labelE.setPosX(owner.getArcMiddle().x + (int) Math.round(r * Math.cos(Math.toRadians(stringAngle))));
                    labelE.setPosY(owner.getArcMiddle().y - (int) Math.round(r * Math.sin(Math.toRadians(stringAngle))));

                    Point middle = new Point(labelE.getPosX(), labelE.getPosY());
                    Point draw = new Point(labelE.getPosX() - width / 2, labelE.getPosY() + height / 2 - descent);
                    draw = Geometry.rotatePoint(middle, draw, labelE.getAngle());
                    Point p1 = new Point(labelE.getPosX() - width / 2, labelE.getPosY() - height / 2);
                    p1 = Geometry.rotatePoint(middle, p1, labelE.getAngle());
                    Point p2 = new Point(labelE.getPosX() + width / 2, labelE.getPosY() - height / 2);
                    p2 = Geometry.rotatePoint(middle, p2, labelE.getAngle());
                    Point p3 = new Point(labelE.getPosX() + width / 2, labelE.getPosY() + height / 2);
                    p3 = Geometry.rotatePoint(middle, p3, labelE.getAngle());
                    Point p4 = new Point(labelE.getPosX() - width / 2, labelE.getPosY() + height / 2);
                    p4 = Geometry.rotatePoint(middle, p4, labelE.getAngle());

                    labelE.setDrawX(draw.x);
                    labelE.setDrawY(draw.y);
                    int xPoints[] = new int[]{p1.x, p2.x, p3.x, p4.x};
                    int yPoints[] = new int[]{p1.y, p2.y, p3.y, p4.y};
                    labelE.setOutline(new Polygon(xPoints, yPoints, 4));
                }
            }
        }
    }
}
