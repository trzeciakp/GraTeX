package pl.edu.agh.gratex.model.vertex;

import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.IsLabelInside;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;

import java.awt.*;

public class VertexPropertyModel extends PropertyModel {
    private int number = -1;
    private int radius = PropertyModel.EMPTY;
    private Color vertexColor = null;
    private LineType lineType = LineType.EMPTY;
    private int lineWidth = PropertyModel.EMPTY;
    private Color lineColor = null;
    private Color fontColor = null;
    private IsLabelInside labelInsideENUM = IsLabelInside.EMPTY;
    private ShapeType shape = ShapeType.EMPTY;


    @Override
    public PropertyModel getCopy() {
        VertexPropertyModel result = new VertexPropertyModel();
        result.number = number;
        result.radius = radius;
        result.shape = shape;
        if (vertexColor != null) {
            result.vertexColor = new Color(vertexColor.getRGB());
        }
        result.lineWidth = lineWidth;
        result.lineType = lineType;
        if (lineColor != null) {
            result.lineColor = new Color(lineColor.getRGB());
        }
        result.labelInsideENUM = labelInsideENUM;
        if (fontColor != null) {
            result.fontColor = new Color(fontColor.getRGB());
        }

        return result;
    }

    public void andOperator(PropertyModel pm) {
        VertexPropertyModel model = (VertexPropertyModel) pm;

        if (model.number != number) {
            number = PropertyModel.EMPTY;
        }

        if (model.radius != radius) {
            radius = PropertyModel.EMPTY;
        }

        if (model.shape != shape) {
            shape = ShapeType.EMPTY;
        }

        if (model.vertexColor != null) {
            if (!model.vertexColor.equals(vertexColor)) {
                vertexColor = null;
            }
        }

        if (model.lineWidth != lineWidth) {
            lineWidth = PropertyModel.EMPTY;
        }

        if (model.lineType != lineType) {
            lineType = LineType.EMPTY;
        }

        if (model.lineColor != null) {
            if (!model.lineColor.equals(lineColor)) {
                lineColor = null;
            }
        }

        if (model.fontColor != null) {
            if (!model.fontColor.equals(fontColor)) {
                fontColor = null;
            }
        }

        if (model.labelInsideENUM != labelInsideENUM) {
            labelInsideENUM = IsLabelInside.EMPTY;
        }
    }

    @Override
    public void updateWithModel(PropertyModel pm) {
        VertexPropertyModel model = (VertexPropertyModel) pm;

        if (model.number != PropertyModel.EMPTY) {
            VertexUtils.setPartOfNumeration((Vertex) owner, false);
            number = model.number;
            VertexUtils.setPartOfNumeration((Vertex) owner, true);
        }

        if (model.radius != PropertyModel.EMPTY) {
            radius = model.radius;
        }

        if (model.shape != ShapeType.EMPTY) {
            shape = model.shape;
        }

        if (model.vertexColor != null) {
            vertexColor = new Color(model.vertexColor.getRGB());
        }

        if (model.lineWidth != PropertyModel.EMPTY) {
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

        if (model.labelInsideENUM != IsLabelInside.EMPTY) {
            labelInsideENUM = model.labelInsideENUM;
        }

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color getVertexColor() {
        return vertexColor;
    }

    public void setVertexColor(Color vertexColor) {
        this.vertexColor = vertexColor;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public IsLabelInside getLabelInsideENUM() {
        return labelInsideENUM;
    }

    public void setLabelInside(IsLabelInside labelInside) {
        this.labelInsideENUM = labelInside;
    }

    public ShapeType getShape() {
        return shape;
    }

    public void setShape(ShapeType shape) {
        this.shape = shape;
    }
}
