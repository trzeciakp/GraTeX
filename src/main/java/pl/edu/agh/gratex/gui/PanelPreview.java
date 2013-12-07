package pl.edu.agh.gratex.gui;


import pl.edu.agh.gratex.graph.Graph;

import javax.swing.*;
import java.awt.*;

public class PanelPreview extends JPanel {
    private static final long serialVersionUID = -8882633408669094896L;

    private Graph graph;

    public PanelPreview(Graph _graph) {
        graph = _graph;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = g2d.getRenderingHints();
        rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

        graph.drawAll(g2d);

        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g.setColor(Color.gray);
        g.setFont(new Font("Cambria", Font.ITALIC, 24));
        g.drawString("Preview", 5, 23);
    }
}
