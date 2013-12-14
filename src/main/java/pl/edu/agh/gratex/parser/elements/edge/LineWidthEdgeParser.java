package pl.edu.agh.gratex.parser.elements.edge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.parser.elements.LineWidthParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;

/**
 *
 */
public class LineWidthEdgeParser extends LineWidthParseElement {

    @Override
    public void setProperty(String match, GraphElement element) {
        Edge edge = (Edge) element;
        edge.setLineWidth(super.getX(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Edge edge = (Edge) element;
        return super.getProperty(edge.getLineWidth());
    }
}
