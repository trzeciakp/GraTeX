package pl.edu.agh.gratex.model.hyperedge;

import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.IsJointDisplay.IsJointDisplay;
import pl.edu.agh.gratex.model.properties.LineType;
import pl.edu.agh.gratex.model.properties.ShapeType;

import java.awt.*;

/**
 *
 */
public class HyperedgePropertyModel extends PropertyModel {
    private LineType lineType = LineType.EMPTY;
    private int lineWidth = PropertyModel.EMPTY;
    private Color lineColor = null;
    private Color jointColor = null;
    private ShapeType jointShape = ShapeType.EMPTY;
    private int jointSize = PropertyModel.EMPTY;
    private IsJointDisplay isJointDisplay = IsJointDisplay.EMPTY;

    @Override
    public void andOperator(PropertyModel pm) {
        HyperedgePropertyModel model = (HyperedgePropertyModel) pm;
        if (getLineType() != model.getLineType()) {
            setLineType(LineType.EMPTY);
        }
        if (getLineWidth() != model.getLineWidth()) {
            setLineWidth(PropertyModel.EMPTY);
        }
        if (getLineColor() != null && !getLineColor().equals(model.getLineColor())) {
            setLineColor(null);
        }
        if (getJointColor() != null && !getJointColor().equals(model.getJointColor())) {
            setJointColor(null);
        }
        if (getJointShape() != model.getJointShape()) {
            setJointShape(ShapeType.EMPTY);
        }
        if (getJointSize() != model.getJointSize()) {
            setJointSize(PropertyModel.EMPTY);
        }
        if (getIsJointDisplay() != model.getIsJointDisplay()) {
            setIsJointDisplay(IsJointDisplay.EMPTY);
        }
    }

    @Override
    public void updateWithModel(PropertyModel pm) {
        HyperedgePropertyModel model = (HyperedgePropertyModel) pm;
        if (model.getLineType() != LineType.EMPTY) {
            setLineType(model.getLineType());
        }
        if (model.getLineWidth() != PropertyModel.EMPTY) {
            setLineWidth(model.getLineWidth());
        }
        if (model.getLineColor() != null) {
            setLineColor(model.getLineColor());
        }
        if (model.getJointColor() != null) {
            setJointColor(model.getJointColor());
        }
        if (model.getJointShape() != ShapeType.EMPTY) {
            setJointShape(model.getJointShape());
        }
        if (model.getJointSize() != PropertyModel.EMPTY) {
            setJointSize(model.getJointSize());
        }
        if (model.getIsJointDisplay() != IsJointDisplay.EMPTY) {
            setIsJointDisplay(model.getIsJointDisplay());
        }
    }

    @Override
    public PropertyModel getCopy() {
        HyperedgePropertyModel result = new HyperedgePropertyModel();
        result.setJointSize(getJointSize());
        result.setJointShape(getJointShape());
        result.setLineColor(getLineColor());
        result.setJointColor(getJointColor());
        result.setLineType(getLineType());
        result.setLineWidth(getLineWidth());
        result.setIsJointDisplay(getIsJointDisplay());
        return result;
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

    public Color getJointColor() {
        return jointColor;
    }

    public void setJointColor(Color jointColor) {
        this.jointColor = jointColor;
    }

    public ShapeType getJointShape() {
        return jointShape;
    }

    public void setJointShape(ShapeType jointShape) {
        this.jointShape = jointShape;
    }

    public int getJointSize() {
        return jointSize;
    }

    public void setJointSize(int jointSize) {
        this.jointSize = jointSize;
    }

    public IsJointDisplay getIsJointDisplay() {
        return isJointDisplay;
    }

    public void setIsJointDisplay(IsJointDisplay isJointDisplay) {
        this.isJointDisplay = isJointDisplay;
    }
}
