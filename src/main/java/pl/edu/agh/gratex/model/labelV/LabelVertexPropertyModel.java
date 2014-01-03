package pl.edu.agh.gratex.model.labelV;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;

import java.awt.*;
import java.io.Serializable;

public class LabelVertexPropertyModel extends PropertyModel {
    private String text;
    private Color fontColor;
    private int position;
    private int spacing;

    public LabelVertexPropertyModel() {
        super(null);
        init();
    }

    public LabelVertexPropertyModel(GraphElement owner) {
        super(owner);
        init();
    }

    private void init() {
        text = "";
        fontColor = null;
        position = PropertyModel.EMPTY;
        spacing = PropertyModel.EMPTY;
    }

    public LabelVertexPropertyModel(LabelVertexPropertyModel pm) {
        super(null);
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

    @Override
    public void mergeWithModel(PropertyModel pm) {
        LabelVertexPropertyModel model = (LabelVertexPropertyModel) pm;

        if (model.text != null) {
            text = new String(model.text);
        }

        if (model.fontColor != null) {
            fontColor = new Color(model.fontColor.getRGB());
        }

        if (model.position > -1) {
            spacing = model.position;
        }

        if (model.spacing > -1) {
            spacing = model.spacing;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }
}
