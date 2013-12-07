package pl.edu.agh.gratex.model;

import java.awt.*;
import java.io.Serializable;

public class LabelEdgePropertyModel extends PropertyModel implements Serializable {
    private static final long serialVersionUID = 5419280325369941060L;

    public String text;
    public Color fontColor;
    public int position;
    public int spacing;
    public int topPlacement;
    public int horizontalPlacement;
    public int isLoop;

    public LabelEdgePropertyModel() {
        text = "";
        fontColor = null;
        position = PropertyModel.EMPTY;
        spacing = PropertyModel.EMPTY;
        topPlacement = PropertyModel.EMPTY;
        horizontalPlacement = PropertyModel.EMPTY;
        isLoop = PropertyModel.EMPTY;
    }

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

    public LabelEdgePropertyModel(LabelEdgePropertyModel pm) {
        copy(pm);
    }

    private void copy(LabelEdgePropertyModel pm) {
        if (pm.text != null)
            text = new String(pm.text);
        if (pm.fontColor != null)
            fontColor = new Color(pm.fontColor.getRGB());
        else
            fontColor = null;
        position = pm.position;
        spacing = pm.spacing;
        topPlacement = pm.topPlacement;
        horizontalPlacement = pm.horizontalPlacement;
        isLoop = pm.isLoop;
    }
}
