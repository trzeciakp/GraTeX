package pl.edu.agh.gratex.parser.elements.edge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LineTypeEdgeParser extends ParseElement {

    private static final String REGEX = ", (dashed|dotted|double)";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    public static final int GROUPS = 1;
    public static final int LINE_TYPE_GROUP = 1;

    @Override
    public boolean isOptional() {
        return true;
    }

    public int groups() {
        return super.groups() + GROUPS;
    }

    @Override
    public String regex() {
        return REGEX;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Edge edge = (Edge) element;

        if(match == null) {
            edge.setLineType(LineType.SOLID);
        } else {
            Matcher matcher = PATTERN.matcher(match);
            matcher.matches();
            edge.setLineType(LineType.valueOf(matcher.group(LINE_TYPE_GROUP).toUpperCase()));
        }
    }

    @Override
    public String getProperty(GraphElement element) {
        Edge edge = (Edge) element;
        LineType lineType = edge.getLineType();
        if(LineType.SOLID != edge.getLineType()) {
            return ", "+lineType.toString();
        } else {
            return "";
        }
    }
}
