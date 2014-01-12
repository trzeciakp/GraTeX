package pl.edu.agh.gratex.parser.elements.edge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.LineColorParseElement;

import java.awt.*;

/**
 *
 */
public class EdgeLineColorParseElement extends LineColorParseElement {

    public EdgeLineColorParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Edge edge = (Edge) element;
        Color color = getLineColor(match);
        if(color != null) {
            edge.setLineColor(color);
        }
    }

    @Override
    public String getProperty(GraphElement element) {
        Edge edge = (Edge) element;
        Color lineColor = edge.getLineColor();
        return getProperty(lineColor);
    }

}
