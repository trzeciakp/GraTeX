package pl.edu.agh.gratex.parser.elements.link;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.model.properties.JointLabelPosition;
import pl.edu.agh.gratex.model.properties.LinkLabelPosition;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LinkBoundaryRelativePositionParseElement extends ParseElement {
    public static final int GROUPS = 1;
    public static final int POSITION_GROUP = 1;
    private static final String REGEX = "\\((above|below|through)\\)";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public int groups() {
        return super.groups() + GROUPS;
    }

    @Override
    public String regex() {
        return REGEX;
    }
    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        linkBoundary.setLabelPosition(LinkLabelPosition.valueOf(matcher.group(POSITION_GROUP).toUpperCase()));
    }

    @Override
    public String getProperty(GraphElement element) {
        LinkBoundary linkBoundary = (LinkBoundary) element;
        return "(" +linkBoundary.getLabelPosition().name().toLowerCase()+")";
    }
}
