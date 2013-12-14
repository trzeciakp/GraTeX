package pl.edu.agh.gratex.parser.elements.vertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LineColorTypeVertexParser extends ParseElement {
    public static final int COLOR_GROUP = 2;
    private static final String REGEX = ", draw(=([a-zA-Z]+))?(, (dashed|double|dotted))?";
    public static final int GROUPS = 4;
    public static final int LINE_GROUP = 4;
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private ColorMapper colorMapper;

    public LineColorTypeVertexParser(ColorMapper colorMapper) {
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
        Vertex vertex = (Vertex) element;
        if(match == null) {
            vertex.setLineType(LineType.NONE);
            vertex.setLineColor(null);
            return;
        }
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        String colorGroup = matcher.group(COLOR_GROUP);
        if(colorGroup != null) {
            vertex.setLineColor(colorMapper.getColor(colorGroup));
        }
        String lineGroup = matcher.group(LINE_GROUP);
        LineType lineType;
        if(lineGroup == null) {
            lineType = LineType.SOLID;
        } else {
            lineType = LineType.valueOf(lineGroup.toUpperCase());
        }
        vertex.setLineType(lineType);
    }

    @Override
    public String getProperty(GraphElement element) {
        Vertex vertex = (Vertex) element;
        LineType lineType = vertex.getLineType();
        if(lineType == LineType.NONE) {
            return "";
        }
        Color lineColor = vertex.getLineColor();
        String result = ", draw" + (lineColor != null?"=" + colorMapper.getColorText(lineColor):"")
                + (lineType != LineType.SOLID?", " + lineType:"");
        return result;
    }
}
