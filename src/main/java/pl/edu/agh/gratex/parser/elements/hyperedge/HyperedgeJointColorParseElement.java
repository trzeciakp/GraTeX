package pl.edu.agh.gratex.parser.elements.hyperedge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ColorParseElement;

import java.awt.*;

/**
 *
 */
public class HyperedgeJointColorParseElement extends ColorParseElement {
    public HyperedgeJointColorParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        Color jointColor = getColorPropertyValue(match);
        if(jointColor != null) {
            hyperedge.setJointColor(jointColor);
        }

    }

    @Override
    public String getProperty(GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        return getProperty(hyperedge.getJointColor());
    }
}
