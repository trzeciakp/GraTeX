package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.io.Serializable;

public class VertexPropertyModel extends PropertyModel implements Serializable {
    private static final long serialVersionUID = -987679256500934963L;

    public int number;
    public int radius;
    public int type;
    public Color vertexColor;
    public LineType lineType;
    public int lineWidth;
    public Color lineColor;
    public Color fontColor;
    public int labelInside;

    public VertexPropertyModel() {
        radius = PropertyModel.EMPTY;
        type = PropertyModel.EMPTY;
        vertexColor = null;
        lineWidth = PropertyModel.EMPTY;
        lineType = LineType.EMPTY;
        lineColor = null;
        labelInside = PropertyModel.EMPTY;
        number = -1;
    }

    public VertexPropertyModel(VertexPropertyModel pm) {
        copy(pm);
    }

    private void copy(VertexPropertyModel pm) {
        number = pm.number;
        radius = pm.radius;
        type = pm.type;
        if (pm.vertexColor != null)
            vertexColor = new Color(pm.vertexColor.getRGB());
        else
            vertexColor = null;
        lineType = pm.lineType;
        lineWidth = pm.lineWidth;
        if (pm.lineColor != null)
            lineColor = new Color(pm.lineColor.getRGB());
        else
            lineColor = null;
        labelInside = pm.labelInside;
        if (pm.fontColor != null)
            fontColor = new Color(pm.fontColor.getRGB());
        else
            fontColor = null;
    }

    public void andOperator(PropertyModel pm) {
        VertexPropertyModel model = (VertexPropertyModel) pm;

        if (model.number != number) {
            number = -1;
        }

        if (model.radius != radius) {
            radius = -1;
        }

        if (model.type != type) {
            type = -1;
        }

        if (vertexColor != null) {
            if (!model.vertexColor.equals(vertexColor)) {
                vertexColor = null;
            }
        }

        if (model.lineWidth != lineWidth) {
            lineWidth = -1;
        }

        if (model.lineType != lineType) {
            lineType = LineType.EMPTY;
        }

        if (lineColor != null) {
            if (!model.lineColor.equals(lineColor)) {
                lineColor = null;
            }
        }

        if (fontColor != null) {
            if (!model.fontColor.equals(fontColor)) {
                fontColor = null;
            }
        }

        if (model.labelInside != labelInside) {
            labelInside = -1;
        }
    }
}
