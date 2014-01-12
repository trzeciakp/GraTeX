package pl.edu.agh.gratex.parser.elements.hyperedge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.PositionParseElement;

/**
 *
 */
public class HyperedgeLabelPositionParseElement extends PositionParseElement {
    @Override
    public void setProperty(String match, GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        hyperedge.setLabelPosX(getX(match));
        hyperedge.setLabelPosY(getY(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        return getProperty(hyperedge.getLabelPosX(), hyperedge.getLabelPosY());
    }
}
