package pl.edu.agh.gratex.parser.elements.hyperedge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.TextColorParseElement;

import java.awt.*;

/**
 *
 */
public class HyperedgeLabelTextColorParseElement extends TextColorParseElement {
    public HyperedgeLabelTextColorParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        Color color = getFontColor(match);
        if(color != null) {
            hyperedge.setJointLabelColor(color);
        }
        hyperedge.setText(getText(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        return getProperty(hyperedge.getJointLabelColor(), hyperedge.getText());
    }
}
