package pl.edu.agh.gratex.parser.elements.hyperedge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.parser.elements.LineWidthParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;

/**
 *
 */
public class HyperedgeJointLineWidthParseElement extends LineWidthParseElement {

    public String regex() {
        return ", "+super.regex();
    }
    @Override
    public void setProperty(String match, GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        hyperedge.setJointLineWidth(getX(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        return ", "+getProperty(hyperedge.getJointLineWidth());
    }
}
