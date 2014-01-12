package pl.edu.agh.gratex.parser.elements;

import java.awt.*;
import java.util.regex.Matcher;

/**
 *
 */
public abstract class ColorParseElement extends ParseElement {
    public static final int COLOR_GROUP = 1;
    public static final int GROUPS = 1;
    private static final String REGEX = ", fill=([a-zA-Z]+)";
    protected ColorMapper colorMapper;

    public ColorParseElement(ColorMapper colorMapper) {
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

    protected Color getColorPropertyValue(String match) {
        if(match == null) {
            //return colorMapper.getTemplateColor();
            return null;
        }
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        String colorText = matcher.group(COLOR_GROUP);
        return colorMapper.getColor(colorText);
    }

    protected String getProperty(Color vertexColor) {
        return (vertexColor != null?", fill=" + colorMapper.getColorText(vertexColor):"");
    }
}
