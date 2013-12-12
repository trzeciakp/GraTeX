package pl.edu.agh.gratex.parser.elements.labelvertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.parser.elements.PositionParseElement;

/**
 *
 */
public class LabelVertexPositionParseElement extends PositionParseElement {
    @Override
    public void setProperty(String match, GraphElement element) {
        LabelV labelV = (LabelV) element;
        labelV.setPosX(getX(match));
        labelV.setPosY(getY(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        LabelV labelV = (LabelV) element;
        return getProperty(labelV.getPosX(), labelV.getPosY());
    }
}
