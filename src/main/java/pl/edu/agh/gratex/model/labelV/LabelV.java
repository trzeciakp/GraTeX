package pl.edu.agh.gratex.model.labelV;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.properties.LabelPosition;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.awt.*;
import java.util.*;
import java.util.List;


public class LabelV extends GraphElement  {
    private LabelVertexPropertyModel propertyModel = (LabelVertexPropertyModel) super.propertyModel;

    private int posX;
    private int posY;

    private Vertex owner;
    private int drawX;
    private int drawY;
    private Rectangle outline;

    public LabelV(Vertex element, Graph graph) {
        super(graph);
        setOwner(element);
        setText("Label");
    }

    @Override
    public void addToGraph() {
        graph.getLabelsV().add(this);
        getOwner().setLabel(this);
        updateLocation();
    }

    @Override
    public void removeFromGraph() {
        graph.getLabelsV().remove(this);
        getOwner().setLabel(null);
    }

    @Override
    public void updateLocation() {
        LabelVUtils.updateLocation(this);
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        return new LinkedList<>();
    }

    @Override
    public String toString() {
        return "LabelV{" +
                "text='" + getText() + '\'' +
                ", font=" + Const.DEFAULT_FONT +
                ", fontColor=" + getFontColor() +
                ", position=" + getPosition() +
                ", spacing=" + getSpacing() +
                ", posX=" + posX +
                ", posY=" + posY +
                ", owner=" + owner +
                ", drawX=" + drawX +
                ", drawY=" + drawY +
                ", outline=" + outline +
                ", graph=" + graph +
                '}';
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.LABEL_VERTEX;
    }

    public String getText() {
        return propertyModel.getText();
    }

    public void setText(String text) {
        propertyModel.setText(text);
    }

    public Color getFontColor() {
        return propertyModel.getFontColor();
    }

    public void setFontColor(Color fontColor) {
        propertyModel.setFontColor(fontColor);
    }

    public int getPosition() {
        return propertyModel.getPosition();
    }

    public LabelPosition getLabelPosition() {
        return LabelPosition.values()[propertyModel.getPosition() +1];
    }

    public void setLabelPosition(LabelPosition labelPosition) {
        propertyModel.setPosition(labelPosition.getValue());
    }

    public void setPosition(int position) {
        propertyModel.setPosition(position);
    }

    public int getSpacing() {
        return propertyModel.getSpacing();
    }

    public void setSpacing(int spacing) {
        propertyModel.setSpacing(spacing);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Vertex getOwner() {
        return owner;
    }

    public void setOwner(Vertex owner) {
        this.owner = owner;
    }

    public int getDrawX() {
        return drawX;
    }

    public void setDrawX(int drawX) {
        this.drawX = drawX;
    }

    public int getDrawY() {
        return drawY;
    }

    public void setDrawY(int drawY) {
        this.drawY = drawY;
    }

    public Rectangle getOutline() {
        return outline;
    }

    public void setOutline(Rectangle outline) {
        this.outline = outline;
    }
}
