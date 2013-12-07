package pl.edu.agh.gratex.model;

import java.awt.*;
import java.io.Serializable;

public class LabelVertexPropertyModel extends PropertyModel implements Serializable {
    private static final long serialVersionUID = -848169351667747331L;

    public String text;
    public Color fontColor;
    public int position;
    public int spacing;

    public LabelVertexPropertyModel() {
        text = "";
        fontColor = null;
        position = PropertyModel.EMPTY;
        spacing = PropertyModel.EMPTY;
    }

    public LabelVertexPropertyModel(LabelVertexPropertyModel pm) {
        copy(pm);

    }

    private void copy(LabelVertexPropertyModel pm) {
        if (pm.text != null)
            text = new String(pm.text);
        if (pm.fontColor != null)
            fontColor = new Color(pm.fontColor.getRGB());
        else
            fontColor = null;
        position = pm.position;
        spacing = pm.spacing;
    }

    public void andOperator(PropertyModel pm) {
        LabelVertexPropertyModel model = (LabelVertexPropertyModel) pm;

        if (!model.text.equals(text)) {
            text = null;
        }

        if (fontColor != null) {
            if (!model.fontColor.equals(fontColor)) {
                fontColor = null;
            }
        }

        if (model.position != position) {
            position = -1;
        }

        if (model.spacing != spacing) {
            spacing = -1;
        }
    }
}
