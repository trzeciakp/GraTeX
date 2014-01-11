package pl.edu.agh.gratex.parser.elements.link;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.parser.elements.PositionParseElement;

/**
 *
 */
public class LinkBoundaryOutPositionParseElement extends PositionParseElement {
    @Override
    public void setProperty(String match, GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        linkBoundary.setOutPointX(getX(match));
        linkBoundary.setOutPointY(getY(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        return getProperty(linkBoundary.getOutPointX(), linkBoundary.getOutPointY());
    }
}
