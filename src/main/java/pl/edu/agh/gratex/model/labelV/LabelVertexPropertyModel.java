package pl.edu.agh.gratex.model.labelV;

import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.LabelPosition;

import java.awt.*;

public class LabelVertexPropertyModel extends PropertyModel {
    private String text = "";
    private Color fontColor = null;
    private LabelPosition labelPosition = LabelPosition.EMPTY;
    private int spacing = PropertyModel.EMPTY;

    @Override
    public PropertyModel getCopy() {
        LabelVertexPropertyModel result = new LabelVertexPropertyModel();
        if (text != null) {
            result.setText(text);
        }
        if (fontColor != null) {
            result.setFontColor(new Color(fontColor.getRGB()));
        }
        result.setLabelPosition(labelPosition);
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

        if (model.labelPosition != labelPosition) {
            labelPosition = LabelPosition.EMPTY;
        }

        if (model.spacing != spacing) {
            spacing = PropertyModel.EMPTY;
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

        if (!model.labelPosition.isEmpty()) {
            labelPosition = model.labelPosition;
        }

        if (model.spacing != PropertyModel.EMPTY) {
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

    public LabelPosition getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(LabelPosition labelPosition) {
        this.labelPosition = labelPosition;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }
}
