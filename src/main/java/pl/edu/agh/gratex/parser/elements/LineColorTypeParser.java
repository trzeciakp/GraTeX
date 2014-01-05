package pl.edu.agh.gratex.parser.elements;

import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class LineColorTypeParser extends ParseElement {
    public static final int COLOR_GROUP = 2;
    public static final int GROUPS = 4;
    public static final int LINE_GROUP = 4;
    private static final String REGEX = ", draw(=([a-zA-Z]+))?(, (dashed|double|dotted))?";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    protected ColorMapper colorMapper;

    public LineColorTypeParser(ColorMapper colorMapper) {
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

    protected Color getColorPropertyValue(String match) {
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        String colorGroup = matcher.group(COLOR_GROUP);
        return (colorGroup != null ? colorMapper.getColor(colorGroup) : colorMapper.getTemplateColor());
    }

    protected LineType getLineTypePropertyValue(String match) {
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        String lineGroup = matcher.group(LINE_GROUP);
        if(lineGroup == null) {
            return LineType.SOLID;
        } else {
            return LineType.valueOf(lineGroup.toUpperCase());
        }
    }

    protected String getProperty(LineType lineType, Color lineColor) {
        if(lineType == LineType.NONE) {
            return "";
        }
        String result = ", draw" + (lineColor != null?"=" + colorMapper.getColorText(lineColor):"")
                + (lineType != LineType.SOLID?", " + lineType:"");
        return result;
    }
}
