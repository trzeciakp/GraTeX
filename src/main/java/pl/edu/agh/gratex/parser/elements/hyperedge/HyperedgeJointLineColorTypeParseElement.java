package pl.edu.agh.gratex.parser.elements.hyperedge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.LineColorTypeParseElement;

import java.awt.*;

/**
 *
 */
public class HyperedgeJointLineColorTypeParseElement extends LineColorTypeParseElement {

    public HyperedgeJointLineColorTypeParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        if(match == null) {
            hyperedge.setJointLineType(LineType.NONE);
            hyperedge.setJointLineColor(colorMapper.getTemplateColor());
            return;
        }
        Color color = getColorPropertyValue(match);
        if(color != null) {
            hyperedge.setJointLineColor(color);
        }
        hyperedge.setJointLineType(getLineTypePropertyValue(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        Hyperedge hyperedge = (Hyperedge) element;
        LineType lineType = hyperedge.getJointLineType();
        Color lineColor = hyperedge.getJointLineColor();
        return getProperty(lineType, lineColor);
    }
}
