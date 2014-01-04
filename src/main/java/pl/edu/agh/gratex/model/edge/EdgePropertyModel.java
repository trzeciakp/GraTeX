package pl.edu.agh.gratex.model.edge;

import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.ArrowType;
import pl.edu.agh.gratex.model.properties.IsDirected;
import pl.edu.agh.gratex.model.properties.IsLoop;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;


public class EdgePropertyModel extends PropertyModel {
    private LineType lineType = LineType.EMPTY;
    private int lineWidth = PropertyModel.EMPTY;
    private Color lineColor = null;
    private int relativeEdgeAngle = PropertyModel.EMPTY;
    private IsLoop isLoop = IsLoop.EMPTY;
    private ArrowType arrowType = ArrowType.EMPTY;
    private IsDirected directed = IsDirected.EMPTY;

    @Override
    public PropertyModel getCopy() {
        EdgePropertyModel result = new EdgePropertyModel();
        result.setLineWidth(lineWidth);
        result.setLineType(lineType);
        result.setArrowType(arrowType);
        if (lineColor != null) {
            result.setLineColor(new Color(lineColor.getRGB()));
        }
        result.setLoop(isLoop);
        result.setRelativeEdgeAngle(relativeEdgeAngle);
        result.setDirected(directed);

        return result;
    }

    @Override
    public void updateWithModel(PropertyModel pm) {
        EdgePropertyModel model = (EdgePropertyModel) pm;
        if (model.getLineType() != LineType.EMPTY) {
            lineType = model.getLineType();
        }

        if (model.getLineWidth() != PropertyModel.EMPTY) {
            lineWidth = model.getLineWidth();
        }

        if (model.getDirected() != IsDirected.EMPTY) {
            directed = model.getDirected();
        }

        if (model.getLineColor() != null) {
            lineColor = new Color(model.getLineColor().getRGB());
        }

        if (model.getRelativeEdgeAngle() != PropertyModel.EMPTY) {
            relativeEdgeAngle = model.getRelativeEdgeAngle();
        }

        if (model.getArrowType() != ArrowType.EMPTY) {
            arrowType = model.getArrowType();
        }
    }

    @Override
    public void andOperator(PropertyModel pm) {
        EdgePropertyModel model = (EdgePropertyModel) pm;

        if (model.lineType != lineType) {
            lineType = LineType.EMPTY;
        }

        if (model.lineWidth != lineWidth) {
            lineWidth = PropertyModel.EMPTY;
        }

        if (model.directed != directed) {
            directed = IsDirected.EMPTY;
        }

        if (lineColor != null) {
            if (!model.lineColor.equals(lineColor)) {
                lineColor = null;
            }
        }

        if (model.relativeEdgeAngle != relativeEdgeAngle) {
            relativeEdgeAngle = PropertyModel.EMPTY;
        }

        if (model.isLoop != isLoop) {
            isLoop = IsLoop.EMPTY;
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

    public int getRelativeEdgeAngle() {
        return relativeEdgeAngle;
    }

    public void setRelativeEdgeAngle(int relativeEdgeAngle) {
        this.relativeEdgeAngle = relativeEdgeAngle;
    }

    public IsLoop getLoop() {
        return isLoop;
    }

    public void setLoop(IsLoop isLoop) {
        this.isLoop = isLoop;
    }

    public ArrowType getArrowType() {
        return arrowType;
    }

    public void setArrowType(ArrowType arrowType) {
        this.arrowType = arrowType;
    }

    public IsDirected getDirected() {
        return directed;
    }

    public void setDirected(IsDirected directed) {
        this.directed = directed;
    }
}
