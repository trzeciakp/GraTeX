package pl.edu.agh.gratex.parser.elements.vertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class SizeVertexParser extends ParseElement {
    private static final String REGEX = ", minimum size=(\\d+\\.?\\d*)pt";
    public static final int GROUPS = 1;
    public static final int SIZE_GROUP = 1;
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public String regex() {
        return REGEX;
    }

    @Override
    public int groups() {
        return super.groups() + GROUPS;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Vertex vertex = (Vertex) element;
        vertex.setRadius(getRadius(match));
    }

    protected int getRadius(String match) {
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        int size = getIntFromDoublePt(matcher.group(SIZE_GROUP))/2;
        return size;
    }

    @Override
    public String getProperty(GraphElement element) {
        Vertex vertex = (Vertex) element;
        int radius = vertex.getRadius();
        return getProperty(radius);
    }

    protected String getProperty(int radius) {
        return ", minimum size="+getDoublePt(2* radius);
    }
}
