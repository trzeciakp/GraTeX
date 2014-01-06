package pl.edu.agh.gratex.parser.elements.vertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.LineColorTypeParseElement;

import java.awt.*;

/**
 *
 */
public class VertexLineColorTypeParseElement extends LineColorTypeParseElement {

    public VertexLineColorTypeParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Vertex vertex = (Vertex) element;
        if(match == null) {
            vertex.setLineType(LineType.NONE);
            vertex.setLineColor(colorMapper.getTemplateColor());
            return;
        }
        Color color = getColorPropertyValue(match);
        if(color != null) {
            vertex.setLineColor(color);
        }
        vertex.setLineType(getLineTypePropertyValue(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Vertex vertex = (Vertex) element;
        LineType lineType = vertex.getLineType();
        Color lineColor = vertex.getLineColor();
        return getProperty(lineType, lineColor);
    }
}
