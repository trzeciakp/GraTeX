package pl.edu.agh.gratex.parser.elements.link;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.parser.elements.LineWidthParseElement;
/**
 *
 */
public class LinkBoundaryLineWidthParseElement extends LineWidthParseElement {
    @Override
    public void setProperty(String match, GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        linkBoundary.setLineWidth(getX(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        return getProperty(linkBoundary.getLineWidth());
    }
}
