package pl.edu.agh.gratex.parser.elements.link;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.parser.elements.DirectionParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;

/**
 *
 */
public class LinkBoundaryDirectionParseElement extends DirectionParseElement {
    @Override
    public void setProperty(String match, GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        linkBoundary.setDirected(getIsDirected(match));
        ArrowType arrowType = getArrowType(match);
        if(arrowType != null) {
            linkBoundary.setArrowType(arrowType);
        }
    }

    @Override
    public String getProperty(GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        boolean directed = linkBoundary.isDirected();
        ArrowType arrowType = linkBoundary.getArrowType();
        return getProperty(directed, arrowType);
    }
}
