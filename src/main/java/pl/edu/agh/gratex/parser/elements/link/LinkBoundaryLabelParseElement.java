package pl.edu.agh.gratex.parser.elements.link;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.model.properties.LinkLabelPosition;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;
import pl.edu.agh.gratex.parser.elements.StaticParseElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LinkBoundaryLabelParseElement extends ParseElement {
    private List<ParseElement> parseList;
    private int groups;
    private String regex;
    private Pattern pattern;

    public LinkBoundaryLabelParseElement(ColorMapper colorMapper) {

        StringBuilder sb = new StringBuilder();

        parseList = new ArrayList<>();
        parseList.add(new StaticParseElement(" \\node ", false));
        parseList.add(new LinkBoundaryRelativePositionParseElement());
        parseList.add(new StaticParseElement(" at ", false));
        parseList.add(new LinkBoundaryPositionParseElement());
        parseList.add(new LinkBoundaryTextColorParseElement(colorMapper));
        parseList.add(new StaticParseElement(";", false));

        for (ParseElement parseElement : parseList) {
            this.groups += parseElement.groups();
            sb.append("(").append(parseElement.regex()).append(")");
        }
        this.regex = sb.toString();
        this.pattern = Pattern.compile(this.regex);
    }

    public void init() {}

    @Override
    public boolean isOptional() {
        return true;
    }

    public int groups() {
        return super.groups() + groups;
    }

    @Override
    public String regex() {
        return regex;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        LinkBoundary hyperedge = (LinkBoundary) element;
        if (match == null) {
            hyperedge.setLabelPosition(LinkLabelPosition.HIDDEN);
        } else {
            Matcher matcher = pattern.matcher(match);
            int currentGroup = 1;
            if(matcher.matches()) {
                for (ParseElement parseElement : parseList) {
                    String foundGroup = matcher.group(currentGroup);
                    parseElement.setProperty(foundGroup, hyperedge);
                    currentGroup += parseElement.groups();
                }
            }
        }
    }

    @Override
    public String getProperty(GraphElement element) {
        if (((LinkBoundary) element).getLabelPosition() == LinkLabelPosition.HIDDEN) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (ParseElement parseElement : parseList) {
                sb.append(parseElement.getProperty(element));
            }
            return sb.toString();
        }
    }
}
