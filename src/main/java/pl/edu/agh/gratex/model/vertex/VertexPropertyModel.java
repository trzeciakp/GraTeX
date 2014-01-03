package pl.edu.agh.gratex.model.vertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.io.Serializable;

public class VertexPropertyModel extends PropertyModel {
    public int number;
    public int radius;
    public int shape;
    public Color vertexColor;
    public LineType lineType;
    public int lineWidth;
    public Color lineColor;
    public Color fontColor;
    public int labelInside;

    public VertexPropertyModel() {
        super(null);
        init();
    }

    public VertexPropertyModel(GraphElement owner) {
        super(owner);
        init();
    }

    private void init() {
        radius = PropertyModel.EMPTY;
        shape = PropertyModel.EMPTY;
        vertexColor = null;
        lineWidth = PropertyModel.EMPTY;
        lineType = LineType.EMPTY;
        lineColor = null;
        labelInside = PropertyModel.EMPTY;
        number = -1;
    }

    public VertexPropertyModel(VertexPropertyModel pm) {
        super(null);
        copy(pm);
    }

    private void copy(VertexPropertyModel pm) {
        number = pm.number;
        radius = pm.radius;
        shape = pm.shape;
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

        if (model.shape != shape) {
            shape = -1;
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

    @Override
    public void mergeWithModel(PropertyModel pm) {
        VertexPropertyModel model = (VertexPropertyModel) pm;

        if (model.number > -1) {
            VertexUtils.setPartOfNumeration((Vertex) owner, false);
            VertexUtils.updateNumber((Vertex) owner, model.number);
            VertexUtils.setPartOfNumeration((Vertex) owner, true);
        }

        if (model.radius > -1) {
            radius = model.radius;
        }

        if (model.shape > -1) {
            shape = model.shape;
        }

        if (model.vertexColor != null) {
            vertexColor = new Color(model.vertexColor.getRGB());
        }

        if (model.lineWidth > -1) {
            lineWidth = model.lineWidth;
        }

        if (model.lineType != LineType.EMPTY) {
            lineType = model.lineType;
        }

        if (model.lineColor != null) {
            lineColor = new Color(model.lineColor.getRGB());
        }

        if (model.fontColor != null) {
            fontColor = new Color(model.fontColor.getRGB());
        }

        if (model.labelInside > -1) {
            labelInside = model.labelInside;
        }

    }
}
