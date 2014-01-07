package pl.edu.agh.gratex.model.hyperedge;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.properties.*;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class Hyperedge extends GraphElement {
    private static int jointNumberCounter = Const.MAX_VERTEX_NUMBER + 1;

    private HyperedgePropertyModel propertyModel = (HyperedgePropertyModel) super.propertyModel;

    private int number = jointNumberCounter++;
    private List<Vertex> connectedVertices = new LinkedList<>();
    private int jointCenterX;
    private int jointCenterY;

    private int labelPosX;
    private int labelPosY;
    private int labelDrawX;
    private int labelDrawY;

    public Hyperedge(Graph graph, PropertyModel propertyModel) {
        super(graph, propertyModel);
    }

    //it should contain unique number to identify joint, any vertex cannot have the same number
    //it can be string, not necessary number
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        if (number > jointNumberCounter) {
            jointNumberCounter = number + 1;
        }
    }

    @Override
    public void finalizeAddingToGraph() {

    }

    @Override
    public void finalizeRemovingFromGraph() {

    }

    @Override
    public void updateLocation() {
        HyperedgeUtils.updateLocation(this);
    }

    public void autoCenterJoint() {
        int centroidX = 0;
        int centroidY = 0;
        for (Vertex vertex : getConnectedVertices()) {
            centroidX += vertex.getPosX();
            centroidY += vertex.getPosY();
        }
        centroidX /= Math.max(1, getConnectedVertices().size());
        centroidY /= Math.max(1, getConnectedVertices().size());
        setJointCenterX(centroidX);
        setJointCenterY(centroidY);
    }

    @Override
    public Area getArea() {
        Path2D path = new Path2D.Double();
        for (Vertex vertex : connectedVertices) {
            path.moveTo(jointCenterX, jointCenterY);
            path.lineTo(vertex.getPosX(), vertex.getPosY());
        }
        Area edgesArea = new Area(new BasicStroke(2 * Const.EDGE_SELECTION_MARGIN + getLineWidth()).createStrokedShape(path));
        edgesArea.add(getJointArea());
        return edgesArea;
    }

    public Area getJointArea() {
        return new Area(VertexUtils.getVertexShape(getJointShape(), Const.HYPEREDGE_JOINT_SELECTION_SIZE, jointCenterX, jointCenterY));
    }

    public Vertex getVertexByEdge(int x, int y) {
        for (Vertex vertex : getConnectedVertices()) {
            Path2D path = new Path2D.Double();
            path.moveTo(jointCenterX, jointCenterY);
            path.lineTo(vertex.getPosX(), vertex.getPosY());
            Area edgeArea = new Area(new BasicStroke(2 * Const.EDGE_SELECTION_MARGIN + getLineWidth()).createStrokedShape(path));
            if (edgeArea.contains(x, y)) {
                return vertex;
            }
        }
        return null;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.HYPEREDGE;
    }

    @Override
    public int getTypeDrawingPriority() {
        return 200000000;
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        return new ArrayList<>();
    }

    public List<Vertex> getConnectedVertices() {
        return connectedVertices;
    }

    public void setConnectedVertices(List<Vertex> connectedVertices) {
        this.connectedVertices = connectedVertices;
    }

    public int getJointCenterX() {
        return jointCenterX;
    }

    public void setJointCenterX(int jointCenterX) {
        this.jointCenterX = jointCenterX;
    }

    public int getJointCenterY() {
        return jointCenterY;
    }

    public void setJointCenterY(int jointCenterY) {
        this.jointCenterY = jointCenterY;
    }

    public Color getLineColor() {
        return propertyModel.getLineColor();
    }

    public ShapeType getJointShape() {
        return propertyModel.getJointShape();
    }

    public LineType getLineType() {
        return propertyModel.getLineType();
    }

    public int getLineWidth() {
        return propertyModel.getLineWidth();
    }

    public void setLineType(LineType lineType) {
        propertyModel.setLineType(lineType);
    }

    public Color getJointColor() {
        return propertyModel.getJointColor();
    }

    public int getJointSize() {
        return propertyModel.getJointSize();
    }

    public void setJointShape(ShapeType jointShape) {
        propertyModel.setJointShape(jointShape);
    }

    public void setJointColor(Color jointColor) {
        propertyModel.setJointColor(jointColor);
    }

    public void setLineWidth(int lineWidth) {
        propertyModel.setLineWidth(lineWidth);
    }

    public void setJointSize(int jointSize) {
        propertyModel.setJointSize(jointSize);
    }

    public void setLineColor(Color lineColor) {
        propertyModel.setLineColor(lineColor);
    }

    public void setIsJointDisplay(JointDisplay jointDisplay) {
        propertyModel.setIsJointDisplay(jointDisplay);
    }

    public JointDisplay getIsJointDisplay() {
        return propertyModel.getIsJointDisplay();
    }

    public LineType getJointLineType() {
        return propertyModel.getJointLineType();
    }

    public JointDisplay getJointDisplay() {
        return propertyModel.getJointDisplay();
    }

    public void setJointLabelColor(Color jointLabelColor) {
        propertyModel.setJointLabelColor(jointLabelColor);
    }

    public int getJointLineWidth() {
        return propertyModel.getJointLineWidth();
    }

    public void setJointLabelPosition(JointLabelPosition jointLabelPosition) {
        propertyModel.setJointLabelPosition(jointLabelPosition);
    }

    public String getText() {
        return propertyModel.getText();
    }

    public void setJointLineType(LineType jointLineType) {
        propertyModel.setJointLineType(jointLineType);
    }

    public Color getJointLineColor() {
        return propertyModel.getJointLineColor();
    }

    public JointLabelPosition getJointLabelPosition() {
        return propertyModel.getJointLabelPosition();
    }

    public void setText(String text) {
        propertyModel.setText(text);
    }

    public void setJointLineColor(Color jointLineColor) {
        propertyModel.setJointLineColor(jointLineColor);
    }

    public void setJointLineWidth(int jointLineWidth) {
        propertyModel.setJointLineWidth(jointLineWidth);
    }

    public Color getJointLabelColor() {
        return propertyModel.getJointLabelColor();
    }

    public void setJointHasLabel(IsLabelInside jointHasLabel) {
        propertyModel.setJointHasLabel(jointHasLabel);
    }

    public void setJointDisplay(JointDisplay jointDisplay) {
        propertyModel.setJointDisplay(jointDisplay);
    }

    public IsLabelInside getJointHasLabel() {
        return propertyModel.getJointHasLabel();
    }

    public int getLabelPosX() {
        return labelPosX;
    }

    public void setLabelPosX(int labelPosX) {
        this.labelPosX = labelPosX;
    }

    public int getLabelPosY() {
        return labelPosY;
    }

    public void setLabelPosY(int labelPosY) {
        this.labelPosY = labelPosY;
    }

    public int getLabelDrawX() {
        return labelDrawX;
    }

    public void setLabelDrawX(int labelDrawX) {
        this.labelDrawX = labelDrawX;
    }

    public int getLabelDrawY() {
        return labelDrawY;
    }

    public void setLabelDrawY(int labelDrawY) {
        this.labelDrawY = labelDrawY;
    }
}