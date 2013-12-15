package pl.edu.agh.gratex.parser.elements.vertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class TextColorVertexParser extends ParseElement {
    public static final int COLOR_GROUP = 2;
    private static final String REGEX = " ?\\{(\\\\textcolor\\{([^}]+)\\})?(\\{([^}]+)\\})?\\}";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    public static final int NUMBER_GROUP = 4;
    public static final int GROUPS = 4;
    private ColorMapper colorMapper;

    public TextColorVertexParser(ColorMapper colorMapper) {
        this.colorMapper = colorMapper;
    }

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
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        String textColor = matcher.group(COLOR_GROUP);
        String textNumber = matcher.group(NUMBER_GROUP);
        if(textColor != null) {
            vertex.setFontColor(colorMapper.getColor(textColor));
        } else {
            //TODO
            vertex.setFontColor(Color.black);
        }
        if(textNumber != null) {
            //TODO
            vertex.setNumber(Integer.parseInt(textNumber));
            vertex.setLabelInside(true);
        }
        else {
            vertex.setLabelInside(false);
        }
    }

    @Override
    public String getProperty(GraphElement element) {
        Vertex vertex = (Vertex) element;
        Color color = vertex.getFontColor();
        int number = vertex.getNumber();
        String line;
        if(vertex.isLabelInside()) {
            if(color != null) {
                line = "{\\textcolor{"+colorMapper.getColorText(color)+"}{"+vertex.getNumber()+"}}";
            } else {
                line = "{"+vertex.getNumber()+"}";
            }
        } else {
            line = "{}";
        }
        return line;
    }
}
