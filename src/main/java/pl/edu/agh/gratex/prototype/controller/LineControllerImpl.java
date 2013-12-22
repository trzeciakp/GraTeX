package pl.edu.agh.gratex.prototype.controller;

import pl.edu.agh.gratex.prototype.model.ElementFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class LineControllerImpl implements LineController {

    public static final int N_AVG = 10;
    private ElementFactory elementFactory;

    public LineControllerImpl(ElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    @Override
    public void fireLineDrawn(List<Point> points) {
        List<Point> smoothedPoints = new ArrayList<Point>();
        int size = points.size();
        smoothedPoints.clear();
        List<Point> stack = new LinkedList<Point>();
        if (size > N_AVG) {
            int j = 0;
            while (j < N_AVG) {
                stack.add(points.get(stackIndex(j, size)));
                j++;
            }
            for (Point point : points) {
                stack.remove(0);
                stack.add(points.get(stackIndex(j++, size)));
                //stack.add(point)
                Point smoothedPoint = averagePoint(stack);
                smoothedPoints.add(smoothedPoint);

                //System.out.println("[" + point.x + ", " + point.y + "] -> ["+ smoothedPoint.x+", "+smoothedPoint.y+"]");
            }
            elementFactory.createElement().setPoints(new ArrayList<Point>(smoothedPoints));
        }
    }

    private static int stackIndex(int j, int size) {
        return (size - N_AVG / 2 + j) % size;
    }

    private Point averagePoint(List<Point> stack) {
        Point result = new Point(0,0);
        for (Point point : stack) {
            result.x += point.x;
            result.y += point.y;
        }
        result.x = result.x/stack.size();
        result.y = result.y/stack.size();
        return result;
    }

    public static void main(String[] args) {
        for(int j = 0; j < 5; j++) {
            System.out.println(stackIndex(j, 10));

        }
    }
}
