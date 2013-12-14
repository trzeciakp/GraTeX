package pl.edu.agh.gratex.parser.elements.labelvertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.parser.elements.ColorMapper;
import pl.edu.agh.gratex.parser.elements.vertex.TextColorParseElement;

/**
 *
 */
public class LabelVertexTextColorParseElement extends TextColorParseElement {

    public LabelVertexTextColorParseElement(ColorMapper colorMapper) {
        super(colorMapper);
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        LabelV labelV = (LabelV) element;
        labelV.setFontColor(getFontColor(match));
        labelV.setText(getText(match));
    }

    @Override
    public String getProperty(GraphElement element) {
        LabelV labelV = (LabelV) element;
        return getProperty(labelV.getFontColor(), labelV.getText());
    }
}
