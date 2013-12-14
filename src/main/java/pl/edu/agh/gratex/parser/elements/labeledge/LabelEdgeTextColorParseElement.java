package pl.edu.agh.gratex.parser.elements.labeledge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.TextColorParseElement;

/**
 *
 */
public class LabelEdgeTextColorParseElement extends TextColorParseElement {

    public LabelEdgeTextColorParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        LabelE labelE = (LabelE) element;
        labelE.setFontColor(getFontColor(match));
        labelE.setText(getText(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        LabelE labelE = (LabelE) element;
        return getProperty(labelE.getFontColor(), labelE.getText());
    }
}
