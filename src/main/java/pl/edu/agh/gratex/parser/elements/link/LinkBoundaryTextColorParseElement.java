package pl.edu.agh.gratex.parser.elements.link;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.TextColorParseElement;

import java.awt.*;

/**
 *
 */
public class LinkBoundaryTextColorParseElement extends TextColorParseElement {
    public LinkBoundaryTextColorParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        Color color = getFontColor(match);
        if(color != null) {
            linkBoundary.setLabelColor(color);
        }
        linkBoundary.setText(getText(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        return getProperty(linkBoundary.getLabelColor(), linkBoundary.getText());
    }
}
