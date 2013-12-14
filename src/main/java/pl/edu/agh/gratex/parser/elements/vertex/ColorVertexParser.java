package pl.edu.agh.gratex.parser.elements.vertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.awt.*;
import java.util.regex.Matcher;

/**
 *
 */
public class ColorVertexParser extends ParseElement {

    private static final String REGEX = ", fill=([a-zA-Z]+)";
    public static final int COLOR_GROUP = 1;
    public static final int GROUPS = 1;
    private ColorMapper colorMapper;

    public ColorVertexParser(ColorMapper colorMapper) {
        this.colorMapper = colorMapper;
    }

    @Override
    public boolean isOptional() {
        return true;
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
        if(match == null) {
            vertex.setVertexColor(null);
            return;
        }
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        String colorText = matcher.group(COLOR_GROUP);
        vertex.setVertexColor(colorMapper.getColor(colorText));
    }

    @Override
    public String getProperty(GraphElement element) {
        Vertex vertex = (Vertex) element;
        Color vertexColor = vertex.getVertexColor();
        return (vertexColor != null?", fill=" + colorMapper.getColorText(vertexColor):"");
    }
}
