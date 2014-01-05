package pl.edu.agh.gratex.model.boundary;

import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.properties.LineType;

import java.awt.*;

/**
 *
 */
public class BoundaryPropertyModel extends PropertyModel {
    private LineType lineType = LineType.EMPTY;
    private int LineWidth = PropertyModel.EMPTY;
    private Color lineColor = null;
    private Color fillColor = null;

    @Override
    public void andOperator(PropertyModel pm) {
        BoundaryPropertyModel boundaryModel = (BoundaryPropertyModel) pm;
        if(getFillColor() == null || !getFillColor().equals(boundaryModel.getFillColor())) {
            setFillColor(null);
        }
        if(getLineColor() == null || !getLineColor().equals(boundaryModel.getLineColor())) {
            setLineColor(null);
        }
        if(getLineWidth() != boundaryModel.getLineWidth()) {
            setLineWidth(PropertyModel.EMPTY);
        }
        if(getLineType() != boundaryModel.getLineType()) {
            setLineType(LineType.EMPTY);
        }
    }

    @Override
    public void updateWithModel(PropertyModel pm) {
        BoundaryPropertyModel boundaryModel = (BoundaryPropertyModel) pm;
        if(boundaryModel.getFillColor() != null) {
            setFillColor(boundaryModel.getFillColor());
        }
        if(boundaryModel.getLineColor() != null) {
            setLineColor(boundaryModel.getLineColor());
        }
        if(boundaryModel.getLineWidth() != PropertyModel.EMPTY) {
            setLineWidth(boundaryModel.getLineWidth());
        }
        if(boundaryModel.getLineType() != LineType.EMPTY) {
            setLineType(boundaryModel.getLineType());
        }
    }

    @Override
    public PropertyModel getCopy() {
        BoundaryPropertyModel result = new BoundaryPropertyModel();
        result.setLineWidth(getLineWidth());
        result.setLineType(getLineType());
        result.setLineColor(getLineColor());
        result.setFillColor(getFillColor());
        return result;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public int getLineWidth() {
        return LineWidth;
    }

    public void setLineWidth(int lineWidth) {
        LineWidth = lineWidth;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
}
