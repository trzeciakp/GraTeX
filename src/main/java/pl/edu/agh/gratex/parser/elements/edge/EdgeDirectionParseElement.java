package pl.edu.agh.gratex.parser.elements.edge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.IsDirected;
import pl.edu.agh.gratex.parser.elements.DirectionParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class EdgeDirectionParseElement extends pl.edu.agh.gratex.parser.elements.DirectionParseElement {

    @Override
    public void setProperty(String match, GraphElement element) {
        Edge edge = (Edge) element;
        edge.setDirected(getIsDirected(match));
        ArrowType arrowType = getArrowType(match);
        if(arrowType != null) {
            edge.setArrowType(arrowType);
        }
    }

    @Override
    public String getProperty(GraphElement element) {
        Edge edge = (Edge) element;
        boolean directed = edge.isDirected();
        ArrowType arrowType = edge.getArrowType();
        return getProperty(directed, arrowType);
    }

}
