package pl.edu.agh.gratex.parser.elements.boundary;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.LineColorTypeParseElement;

import java.awt.*;

/**
 *
 */
public class BoundaryLineColorTypeParseElement extends LineColorTypeParseElement {
    public BoundaryLineColorTypeParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Boundary boundary = (Boundary) element;
        if(match == null) {
            boundary.setLineType(LineType.NONE);
            boundary.setLineColor(colorMapper.getTemplateColor());
            return;
        }
        Color color = getColorPropertyValue(match);
        if(color != null) {
            boundary.setLineColor(color);
        }
        boundary.setLineType(getLineTypePropertyValue(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Boundary boundary = (Boundary) element;
        LineType lineType = boundary.getLineType();
        Color lineColor = boundary.getLineColor();
        return getProperty(lineType, lineColor);
    }
}
