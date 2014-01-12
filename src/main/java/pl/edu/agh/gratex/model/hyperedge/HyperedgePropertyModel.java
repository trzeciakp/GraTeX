package pl.edu.agh.gratex.model.hyperedge;

import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.*;

import java.awt.*;

/**
 *
 */
public class HyperedgePropertyModel extends PropertyModel {
    private LineType lineType = LineType.EMPTY;
    private int lineWidth = PropertyModel.EMPTY;
    private Color lineColor = null;

    private JointDisplay jointDisplay = JointDisplay.EMPTY;
    private ShapeType jointShape = ShapeType.EMPTY;
    private int jointSize = PropertyModel.EMPTY;
    private Color jointColor = null;

    private LineType jointLineType = LineType.EMPTY;
    private int jointLineWidth = PropertyModel.EMPTY;
    private Color jointLineColor = null;

    private String text = null;
    private JointLabelPosition jointLabelPosition = JointLabelPosition.EMPTY;
    private Color jointLabelColor = null;


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
            setIsJointDisplay(JointDisplay.EMPTY);
        }
        if (getJointLineType() != model.getJointLineType()) {
            setJointLineType(LineType.EMPTY);
        }
        if (getJointLineWidth() != model.getJointLineWidth()) {
            setJointLineWidth(PropertyModel.EMPTY);
        }
        if (getJointLineColor() != null && !getJointLineColor().equals(model.getJointLineColor())) {
            setJointLineColor(null);
        }
        if (getText() != null && !getText().equals(model.getText())) {
            setText(null);
        }
        if (getJointLabelPosition() != model.getJointLabelPosition()) {
            setJointLabelPosition(JointLabelPosition.EMPTY);
        }
        if (getJointLabelColor() != null && !getJointLabelColor().equals(model.getJointLabelColor())) {
            setJointLabelColor(null);
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
        if (model.getIsJointDisplay() != JointDisplay.EMPTY) {
            setIsJointDisplay(model.getIsJointDisplay());
        }
        if (model.getJointLineType() != LineType.EMPTY) {
            setJointLineType(model.getJointLineType());
        }
        if (model.getJointLineWidth() != PropertyModel.EMPTY) {
            setJointLineWidth(model.getJointLineWidth());
        }
        if (model.getJointLineColor() != null) {
            setJointLineColor(model.getJointLineColor());
        }
        if (model.getText() != null) {
            setText(model.getText());
        }
        if (model.getJointLabelPosition() != JointLabelPosition.EMPTY) {
            setJointLabelPosition(model.getJointLabelPosition());
        }
        if (model.getJointLabelColor() != null) {
            setJointLabelColor(model.getJointLabelColor());
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
        result.setJointLineType(getJointLineType());
        result.setJointLineWidth(getJointLineWidth());
        result.setJointLineColor(getJointLineColor());
        result.setText(getText());
        result.setJointLabelPosition(getJointLabelPosition());
        result.setJointLabelColor(getJointLabelColor());
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

    public JointDisplay getIsJointDisplay() {
        return jointDisplay;
    }

    public void setIsJointDisplay(JointDisplay jointDisplay) {
        this.jointDisplay = jointDisplay;
    }

    public JointDisplay getJointDisplay() {
        return jointDisplay;
    }

    public void setJointDisplay(JointDisplay jointDisplay) {
        this.jointDisplay = jointDisplay;
    }

    public LineType getJointLineType() {
        return jointLineType;
    }

    public void setJointLineType(LineType jointLineType) {
        this.jointLineType = jointLineType;
    }

    public int getJointLineWidth() {
        return jointLineWidth;
    }

    public void setJointLineWidth(int jointLineWidth) {
        this.jointLineWidth = jointLineWidth;
    }

    public Color getJointLineColor() {
        return jointLineColor;
    }

    public void setJointLineColor(Color jointLineColor) {
        this.jointLineColor = jointLineColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public JointLabelPosition getJointLabelPosition() {
        return jointLabelPosition;
    }

    public void setJointLabelPosition(JointLabelPosition jointLabelPosition) {
        this.jointLabelPosition = jointLabelPosition;
    }

    public Color getJointLabelColor() {
        return jointLabelColor;
    }

    public void setJointLabelColor(Color jointLabelColor) {
        this.jointLabelColor = jointLabelColor;
    }
}
