package pl.edu.agh.gratex.model.linkBoundary;

import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.IsDirected;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;


public class LinkBoundaryPropertyModel extends PropertyModel {
    private LineType lineType = LineType.NONE;
    private int lineWidth = PropertyModel.EMPTY;
    private Color lineColor = null;
    private IsDirected directed = IsDirected.EMPTY;
    private ArrowType arrowType = ArrowType.EMPTY;


    @Override
    public PropertyModel getCopy() {
        LinkBoundaryPropertyModel result = new LinkBoundaryPropertyModel();
        result.setLineWidth(lineWidth);
        result.setLineType(lineType);
        result.setArrowType(arrowType);
        if (lineColor != null) {
            result.setLineColor(new Color(lineColor.getRGB()));
        }
        result.setDirected(directed);

        return result;
    }

    @Override
    public void updateWithModel(PropertyModel pm) {
        LinkBoundaryPropertyModel model = (LinkBoundaryPropertyModel) pm;
        if (model.getLineType() != LineType.EMPTY) {
            lineType = model.getLineType();
        }

        if (model.getLineWidth() != PropertyModel.EMPTY) {
            lineWidth = model.getLineWidth();
        }

        if (model.getLineColor() != null) {
            lineColor = new Color(model.getLineColor().getRGB());
        }

        if (model.getDirected() != IsDirected.EMPTY) {
            directed = model.getDirected();
        }

        if (model.getArrowType() != ArrowType.EMPTY) {
            arrowType = model.getArrowType();
        }
    }

    @Override
    public void andOperator(PropertyModel pm) {
        LinkBoundaryPropertyModel model = (LinkBoundaryPropertyModel) pm;

        if (model.lineType != lineType) {
            lineType = LineType.EMPTY;
        }

        if (model.lineWidth != lineWidth) {
            lineWidth = PropertyModel.EMPTY;
        }

        if (model.lineColor != null) {
            if (!model.lineColor.equals(lineColor)) {
                lineColor = null;
            }
        }

        if (model.directed != directed) {
            directed = IsDirected.EMPTY;
        }

        if (model.arrowType != arrowType) {
            arrowType = ArrowType.EMPTY;
        }
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

    public IsDirected getDirected() {
        return directed;
    }

    public void setDirected(IsDirected directed) {
        this.directed = directed;
    }

    public ArrowType getArrowType() {
        return arrowType;
    }

    public void setArrowType(ArrowType arrowType) {
        this.arrowType = arrowType;
    }
}
