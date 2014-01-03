package pl.edu.agh.gratex.model.labelV;

import pl.edu.agh.gratex.model.PropertyModel;

import java.awt.*;

public class LabelVertexPropertyModel extends PropertyModel {
    private String text = "";
    private Color fontColor = null;
    private int position = PropertyModel.EMPTY;
    private int spacing = PropertyModel.EMPTY;

    @Override
    public PropertyModel getCopy() {
        LabelVertexPropertyModel result = new LabelVertexPropertyModel();
        if (text != null) {
            result.setText(new String(text));
        }
        if (fontColor != null) {
            result.setFontColor(new Color(fontColor.getRGB()));
        }
        result.setPosition(position);
        result.setSpacing(spacing);
        return result;
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
    public void updateWithModel(PropertyModel pm) {
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
