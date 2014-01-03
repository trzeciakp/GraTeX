package pl.edu.agh.gratex.prototype.view;

import pl.edu.agh.gratex.prototype.controller.LineController;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class DrawingLineListener implements MouseMotionListener, MouseListener {

    private List<Point> points = new ArrayList<>();
    private LineController controller;
    private List<Point> smoothedPoints = new ArrayList<>();

    public DrawingLineListener(LineController controller) {
        this.controller = controller;
    }

    public List<Point> getPoints() {
        return points;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        points.add(new Point(e.getX(), e.getY()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        points = new ArrayList<Point>();
        points.add(new Point(e.getX(), e.getY()));

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.fireLineDrawn(points);
        points = new ArrayList<Point>();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}