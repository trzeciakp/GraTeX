package pl.edu.agh.gratex.parser.elements.edge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.parser.elements.LineTypeParseElement;

/**
 *
 */
public class EdgeLineTypeParseElement extends LineTypeParseElement {

    @Override
    public void setProperty(String match, GraphElement element) {
        Edge edge = (Edge) element;
        edge.setLineType(getLineType(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Edge edge = (Edge) element;
        LineType lineType = edge.getLineType();
        return getProperty(lineType);
    }

}
