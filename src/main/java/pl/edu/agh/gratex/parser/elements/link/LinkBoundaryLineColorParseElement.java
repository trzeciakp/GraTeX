package pl.edu.agh.gratex.parser.elements.link;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.LineColorParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;

/**
 *
 */
public class LinkBoundaryLineColorParseElement extends LineColorParseElement {
    public LinkBoundaryLineColorParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        linkBoundary.setLineColor(getLineColor(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        return getProperty(linkBoundary.getLineColor());
    }

}
