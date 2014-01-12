package pl.edu.agh.gratex.parser.elements.hyperedge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.LineColorParseElement;

import java.awt.*;

/**
 *
 */
public class HyperedgeLineColorParseElement extends LineColorParseElement {
    public HyperedgeLineColorParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        Color color = getLineColor(match);
        if(color != null) {
            hyperedge.setLineColor(color);
        }
    }

    @Override
    public String getProperty(GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        return getProperty(hyperedge.getLineColor());
    }
}
