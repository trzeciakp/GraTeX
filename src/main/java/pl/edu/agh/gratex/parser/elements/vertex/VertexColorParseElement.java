package pl.edu.agh.gratex.parser.elements.vertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ColorParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.awt.*;
import java.util.regex.Matcher;

/**
 *
 */
public class VertexColorParseElement extends ColorParseElement {

    public VertexColorParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Vertex vertex = (Vertex) element;
        Color color = getColorPropertyValue(match);
        vertex.setVertexColor(color);
    }

    @Override
    public String getProperty(GraphElement element) {
        Vertex vertex = (Vertex) element;
        Color vertexColor = vertex.getVertexColor();
        return getProperty(vertexColor);
    }


}
