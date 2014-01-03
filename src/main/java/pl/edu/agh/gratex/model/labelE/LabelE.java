package pl.edu.agh.gratex.model.labelE;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.LabelHorizontalPlacement;
import pl.edu.agh.gratex.model.properties.LabelTopPlacement;
import pl.edu.agh.gratex.model.PropertyModel;

import java.awt.*;
import java.util.*;
import java.util.List;


public class LabelE extends GraphElement {
    private LabelEdgePropertyModel propertyModel = (LabelEdgePropertyModel) super.propertyModel;

    private int posX;
    private int posY;
    private int angle;

    private Edge owner;
    private Polygon outline;
    private int drawX;
    private int drawY;

    public LabelE(Edge element, Graph graph) {
        super(graph);
        setOwner(element);
        setText("Label");
    }

    @Override
    public PropertyModel getModel() {
        LabelEdgePropertyModel pm = (LabelEdgePropertyModel) propertyModel.getCopy();
        pm.setLoop(owner.isLoop() ? PropertyModel.YES : PropertyModel.NO);
        return pm;
    }

    @Override
    public void addToGraph() {
        graph.getLabelsE().add(this);
        getOwner().setLabel(this);
        updateLocation();
    }

    @Override
    public void removeFromGraph() {
        graph.getLabelsE().remove(this);
        getOwner().setLabel(null);
    }

    @Override
    public void updateLocation() {
        LabelEUtils.updateLocation(this);
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        return new LinkedList<>();
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.LABEL_EDGE;
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

    public void setPosition(int position) {
        propertyModel.setPosition(position);
    }

    public int getSpacing() {
        return propertyModel.getSpacing();
    }

    public void setSpacing(int spacing) {
        propertyModel.setSpacing(spacing);
    }

    public boolean isTopPlacement() {
        return propertyModel.getTopPlacement() == 1;
    }

    public void setTopPlacement(boolean topPlacement) {
        propertyModel.setTopPlacement(topPlacement ? 1 : 0);
    }

    public void setTopPlacement(LabelTopPlacement topPlacement) {
        propertyModel.setTopPlacement((topPlacement == LabelTopPlacement.ABOVE) ? 1 : 0);
    }

    public LabelTopPlacement getTopPlacement() {
        return (propertyModel.getTopPlacement() == 1 ? LabelTopPlacement.ABOVE: LabelTopPlacement.BELOW);
    }

    public boolean isHorizontalPlacement() {
        return propertyModel.getHorizontalPlacement() == 1;
    }

    public void setHorizontalPlacement(boolean horizontalPlacement) {
        propertyModel.setHorizontalPlacement(horizontalPlacement ? 1 : 0);
    }

    public void setHorizontalPlacement(LabelHorizontalPlacement horizontalPlacement) {
        propertyModel.setHorizontalPlacement((horizontalPlacement == LabelHorizontalPlacement.LEVEL) ? 1 : 0);
    }

    public LabelHorizontalPlacement getHorizontalPlacement() {
        return (propertyModel.getHorizontalPlacement() == 1 ? LabelHorizontalPlacement.LEVEL: LabelHorizontalPlacement.TANGENT);
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

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public Edge getOwner() {
        return owner;
    }

    public void setOwner(Edge owner) {
        this.owner = owner;
    }

    public Polygon getOutline() {
        return outline;
    }

    public void setOutline(Polygon outline) {
        this.outline = outline;
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
}
