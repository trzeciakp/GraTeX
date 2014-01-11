package pl.edu.agh.gratex.parser.elements.link;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.parser.elements.LineTypeParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;

/**
 *
 */
public class LinkBoundaryLineTypeParseElement extends LineTypeParseElement {
    @Override
    public void setProperty(String match, GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        linkBoundary.setLineType(getLineType(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        return getProperty(linkBoundary.getLineType());
    }
}
