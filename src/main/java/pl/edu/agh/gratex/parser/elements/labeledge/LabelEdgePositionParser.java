package pl.edu.agh.gratex.parser.elements.labeledge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.parser.elements.PositionParseElement;

/**
 *
 */
public class LabelEdgePositionParser extends PositionParseElement {
    @Override
    public void setProperty(String match, GraphElement element) {
        LabelE labelE = (LabelE) element;
        labelE.setPosX(getX(match));
        labelE.setPosY(getY(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        LabelE labelE = (LabelE) element;
        return getProperty(labelE.getPosX(), labelE.getPosY());
    }
}
