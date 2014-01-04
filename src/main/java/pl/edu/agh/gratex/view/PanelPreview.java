package pl.edu.agh.gratex.view;


import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

@SuppressWarnings("serial")
public class PanelPreview extends JPanel {

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

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

        List<GraphElement> allElements = graph.getAllElements();
        GraphElement.sortByDrawingPriorities(allElements);
        for (GraphElement element : allElements) {
            element.draw(g2d);
        }

        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g2d.setColor(Color.gray);
        g2d.setFont(new Font("Cambria", Font.ITALIC, 24));
        g2d.drawString(StringLiterals.LABEL_PANEL_PREVIEW_TITLE, 5, 23);
    }
}
