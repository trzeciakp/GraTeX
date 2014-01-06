package pl.edu.agh.gratex.parser.elements.hyperedge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.parser.elements.NumberParseElement;

/**
 *
 */
public class HyperedgeNumberParseElement extends NumberParseElement {
    @Override
    public void setProperty(String match, GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        hyperedge.setNumber(getNumber(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        return getProperty(hyperedge.getNumber());
    }
}
