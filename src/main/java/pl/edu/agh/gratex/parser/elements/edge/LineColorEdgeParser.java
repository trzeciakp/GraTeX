package pl.edu.agh.gratex.parser.elements.edge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LineColorEdgeParser extends ParseElement {
    public static final int GROUPS = 1;
    public static final int COLOR_GROUP = 1;
    private ColorMapper colorMapper;
    private static final String REGEX = ", color=([a-zA-z]+)";
    protected static final Pattern PATTERN = Pattern.compile(REGEX);

    public LineColorEdgeParser(ColorMapper colorMapper) {
        this.colorMapper = colorMapper;
    }

    @Override
    public boolean isOptional() {
        return true;
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
        Edge edge = (Edge) element;
        if(match == null) {
            colorMapper.getTemplateColor();
        }
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        edge.setLineColor(colorMapper.getColor(matcher.group(COLOR_GROUP)));
    }

    @Override
    public String getProperty(GraphElement element) {
        Edge edge = (Edge) element;
        if(edge.getLineColor() == null) {
            return "";
        }
        return ", color="+colorMapper.getColorText(edge.getLineColor());
    }
}
