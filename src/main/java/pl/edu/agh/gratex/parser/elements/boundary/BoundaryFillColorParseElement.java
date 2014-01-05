package pl.edu.agh.gratex.parser.elements.boundary;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.ColorParseElement;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.awt.*;

/**
 *
 */
public class BoundaryFillColorParseElement extends ColorParseElement {
    public BoundaryFillColorParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Boundary boundary = (Boundary) element;
        Color color = getColorPropertyValue(match);
        boundary.setFillColor(color);
    }

    @Override
    public String getProperty(GraphElement element) {
        Boundary boundary = (Boundary) element;
        Color boundaryColor = boundary.getFillColor();
        return getProperty(boundaryColor);
    }
}
