package pl.edu.agh.gratex.model.linkBoundary;

import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.*;

import java.awt.*;


public class LinkBoundaryPropertyModel extends PropertyModel {
    private LineType lineType = LineType.NONE;
    private int lineWidth = PropertyModel.EMPTY;
    private Color lineColor = null;
    private IsDirected directed = IsDirected.EMPTY;
    private ArrowType arrowType = ArrowType.EMPTY;

    private LinkLabelPosition labelPosition = LinkLabelPosition.EMPTY;
    private String text = null;
    private Color labelColor = null;


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

        result.setLabelPosition(getLabelPosition());
        result.setText(getText());
        result.setLabelColor(getLabelColor());

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

        if (model.getLabelPosition() != LinkLabelPosition.EMPTY) {
            setLabelPosition(model.getLabelPosition());
        }

        if (model.getText() != null) {
            setText(model.getText());
        }

        if (model.getLabelColor() != null) {
            setLabelColor(model.getLabelColor());
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

        if (getLabelPosition() != model.getLabelPosition()) {
            setLabelPosition(LinkLabelPosition.EMPTY);
        }

        if (getText() != null && !getText().equals(model.getText())) {
            setText(null);
        }

        if (getLabelColor() != null && !getLabelColor().equals(model.getLabelColor())) {
            setLabelColor(null);
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

    public LinkLabelPosition getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(LinkLabelPosition labelPosition) {
        this.labelPosition = labelPosition;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(Color labelColor) {
        this.labelColor = labelColor;
    }
}
