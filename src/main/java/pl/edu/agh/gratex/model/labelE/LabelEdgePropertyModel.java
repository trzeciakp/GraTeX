package pl.edu.agh.gratex.model.labelE;

import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.IsLoop;
import pl.edu.agh.gratex.model.properties.LabelHorizontalPlacement;
import pl.edu.agh.gratex.model.properties.LabelTopPlacement;

import java.awt.*;

public class LabelEdgePropertyModel extends PropertyModel {
    private String text = "";
    private Color fontColor = null;
    private int position = PropertyModel.EMPTY;
    private int spacing = PropertyModel.EMPTY;
    private LabelHorizontalPlacement horizontalPlacement = LabelHorizontalPlacement.EMPTY;
    private IsLoop isLoop = IsLoop.EMPTY;
    private LabelTopPlacement topPlacement = LabelTopPlacement.EMPTY;

    @Override
    public PropertyModel getCopy() {
        LabelEdgePropertyModel result = new LabelEdgePropertyModel();

        if (text != null) {
            result.setText(text);
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
            text = model.getText();
        }

        if (model.getFontColor() != null) {
            fontColor = new Color(model.getFontColor().getRGB());
        }

        if (model.getPosition() != PropertyModel.EMPTY) {
            position = model.getPosition();
        }

        if (model.getSpacing() != PropertyModel.EMPTY) {
            spacing = model.getSpacing();
        }

        if (!model.getTopPlacement().isEmpty()) {
            topPlacement = model.getTopPlacement();
        }

        if (!model.getHorizontalPlacement().isEmpty()) {
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
            position = PropertyModel.EMPTY;
        }

        if (model.spacing != spacing) {
            spacing = PropertyModel.EMPTY;
        }

        if (model.topPlacement != topPlacement) {
            topPlacement = LabelTopPlacement.EMPTY;
        }

        if (model.horizontalPlacement != horizontalPlacement) {
            horizontalPlacement = LabelHorizontalPlacement.EMPTY;
        }

        if (model.isLoop != isLoop) {
            isLoop = IsLoop.EMPTY;
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

    public LabelTopPlacement getTopPlacement() {
        return topPlacement;
    }

    public void setTopPlacement(LabelTopPlacement topPlacement) {
        this.topPlacement = topPlacement;
    }

    public void setHorizontalPlacement(LabelHorizontalPlacement horizontalPlacement) {
       this.horizontalPlacement = horizontalPlacement;
    }

    public LabelHorizontalPlacement getHorizontalPlacement() {
        return horizontalPlacement;

    }

    public IsLoop getLoop() {
        return isLoop;
    }

    public void setLoop(IsLoop isLoop) {
        this.isLoop = isLoop;
    }
}
