package pl.edu.agh.gratex.model.labelE;

import pl.edu.agh.gratex.model.PropertyModel;

import java.awt.*;
import java.util.LinkedList;

public class LabelEdgePropertyModel extends PropertyModel {
    private String text = "";
    private Color fontColor = null;
    private int position = PropertyModel.EMPTY;
    private int spacing = PropertyModel.EMPTY;
    private int topPlacement = PropertyModel.EMPTY;
    private int horizontalPlacement = PropertyModel.EMPTY;
    private int isLoop = PropertyModel.EMPTY;

    @Override
    public PropertyModel getCopy() {
        LabelEdgePropertyModel result = new LabelEdgePropertyModel();

        if (text != null) {
            result.setText(new String(text));
        }
        if (fontColor != null) {
            result.setFontColor(new Color(fontColor.getRGB()));
        }
        result.setPosition(position);
        result.setSpacing(spacing);
        result.setLoop(isLoop);
        result.setTopPlacement(topPlacement);
        result.setHorizontalPlacement(horizontalPlacement);

        return result;
    }

    @Override
    public void updateWithModel(PropertyModel pm) {
        LabelEdgePropertyModel model = (LabelEdgePropertyModel) pm;

        if (model.getText() != null) {
            text = new String(model.getText());
        }

        if (model.getFontColor() != null) {
            fontColor = new Color(model.getFontColor().getRGB());
        }

        if (model.getPosition() > -1) {
            position = model.getPosition();
        }

        if (model.getSpacing() > -1) {
            spacing = model.getSpacing();
        }

        if (model.getTopPlacement() > -1) {
            topPlacement = model.getTopPlacement();
        }

        if (model.getHorizontalPlacement() > -1) {
            horizontalPlacement = model.getHorizontalPlacement();
        }
    }

    @Override
    public void andOperator(PropertyModel pm) {
        LabelEdgePropertyModel model = (LabelEdgePropertyModel) pm;

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

        if (model.topPlacement != topPlacement) {
            topPlacement = -1;
        }

        if (model.horizontalPlacement != horizontalPlacement) {
            horizontalPlacement = -1;
        }

        if (model.isLoop != isLoop) {
            isLoop = -1;
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

    public int getTopPlacement() {
        return topPlacement;
    }

    public void setTopPlacement(int topPlacement) {
        this.topPlacement = topPlacement;
    }

    public int getHorizontalPlacement() {
        return horizontalPlacement;
    }

    public void setHorizontalPlacement(int horizontalPlacement) {
        this.horizontalPlacement = horizontalPlacement;
    }

    public int getLoop() {
        return isLoop;
    }

    public void setLoop(int loop) {
        isLoop = loop;
    }
}
