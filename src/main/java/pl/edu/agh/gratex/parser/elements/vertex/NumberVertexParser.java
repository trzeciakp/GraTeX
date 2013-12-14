package pl.edu.agh.gratex.parser.elements.vertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class NumberVertexParser extends ParseElement {
    private static final String REGEX = "\\((\\d+)\\)";
    public static final int GROUPS = 1;
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    public static final int NUMBER_GROUP = 1;
    public static final String STRING_FORMAT = "(%d)";

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public int groups() {
        return super.groups() + GROUPS;
    }

    @Override
    public String regex() {
        return REGEX;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Vertex vertex = (Vertex) element;
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        vertex.setNumber(Integer.parseInt(matcher.group(NUMBER_GROUP)));
    }

    @Override
    public String getProperty(GraphElement element) {
        Vertex vertex = (Vertex) element;
        return String.format(STRING_FORMAT, vertex.getNumber());
    }
}
