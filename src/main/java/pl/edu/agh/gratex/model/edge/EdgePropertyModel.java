package pl.edu.agh.gratex.model.edge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;
import java.io.Serializable;


public class EdgePropertyModel extends PropertyModel  {

    private LineType lineType;
    private int lineWidth;
    private int directed;
    private Color lineColor;
    private int relativeEdgeAngle;
    private int isLoop;
    private int arrowType;

    public EdgePropertyModel() {
        super(null);
        init();
    }

    public EdgePropertyModel(GraphElement owner) {
        super(owner);
        init();
    }

    private void init() {
        lineType = LineType.EMPTY;
        lineWidth = PropertyModel.EMPTY;
        directed = PropertyModel.EMPTY;
        lineColor = null;
        relativeEdgeAngle = PropertyModel.EMPTY;
        isLoop = PropertyModel.NO;
        arrowType = PropertyModel.EMPTY;
    }

    public EdgePropertyModel(EdgePropertyModel pm) {
        super(null);
        copy(pm);
    }

    private void copy(EdgePropertyModel pm) {
        lineType = pm.lineType;
        lineWidth = pm.lineWidth;
        directed = pm.directed;
        if (pm.lineColor != null)
            lineColor = new Color(pm.lineColor.getRGB());
        else
            lineColor = null;
        relativeEdgeAngle = pm.relativeEdgeAngle;
        isLoop = pm.isLoop;
        arrowType = pm.arrowType;
    }

    @Override
    public void andOperator(PropertyModel pm) {
        EdgePropertyModel model = (EdgePropertyModel) pm;

        if (model.lineType != lineType) {
            lineType = LineType.EMPTY;
        }

        if (model.lineWidth != lineWidth) {
            lineWidth = -1;
        }

        if (model.directed != directed) {
            directed = -1;
        }

        if (lineColor != null) {
            if (!model.lineColor.equals(lineColor)) {
                lineColor = null;
            }
        }

        if (model.relativeEdgeAngle != relativeEdgeAngle) {
            relativeEdgeAngle = -1;
        }

        if (model.isLoop != isLoop) {
            isLoop = -1;
        }

        if (model.arrowType != arrowType) {
            arrowType = -1;
        }
    }

    @Override
    public void mergeWithModel(PropertyModel pm) {
        EdgePropertyModel model = (EdgePropertyModel) pm;
        if (model.getLineType() != LineType.EMPTY) {
            lineType = model.getLineType();
        }

        if (model.getLineWidth() > -1) {
            lineWidth = model.getLineWidth();
        }

        if (model.getDirected() > -1) {
            directed = model.getDirected();
        }

        if (model.getLineColor() != null) {
            lineColor = new Color(model.getLineColor().getRGB());
        }

        if (model.getRelativeEdgeAngle() > -1) {
            relativeEdgeAngle = model.getRelativeEdgeAngle();
        }

        if (model.getArrowType() > -1) {
            arrowType = model.getArrowType();
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

    public int getDirected() {
        return directed;
    }

    public void setDirected(int directed) {
        this.directed = directed;
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

    public int getLoop() {
        return isLoop;
    }

    public void setLoop(int loop) {
        isLoop = loop;
    }

    public int getArrowType() {
        return arrowType;
    }

    public void setArrowType(int arrowType) {
        this.arrowType = arrowType;
    }
}
