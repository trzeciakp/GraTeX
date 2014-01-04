package pl.edu.agh.gratex.parser.elements.vertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.ShapeType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class ShapeVertexParser extends ParseElement {
    private static final String REGEX = "(circle|regular polygon, regular polygon sides=(\\d+))";
    public static final int GROUPS = 2;
    public static final int SIDES_GROUP = 2;
    public static final int CIRCLE_POLYGON_GROUP = 1;
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
        Matcher matcher = PATTERN.matcher(match);
        matcher.matches();
        String group = matcher.group(SIDES_GROUP);
        if(group != null) {
            int sides = Integer.parseInt(group);
            vertex.setShape(ShapeType.getShapeBySidesNumber(sides));
            return;
        }
        if(matcher.group(CIRCLE_POLYGON_GROUP) != null) {
            vertex.setShape(ShapeType.CIRCLE);
        }
    }

    @Override
    public String getProperty(GraphElement element) {
        Vertex vertex = (Vertex) element;
        ShapeType shape = vertex.getShape();
        return (shape == ShapeType.CIRCLE?"circle":"regular polygon, regular polygon sides="+shape.getSides());
    }
}
