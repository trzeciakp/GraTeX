package pl.edu.agh.gratex.editor;

import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.Vertex;

public class DragOperation extends OldOperation {
    private int startPosX;
    private int startPosY;
    private int endPosX;
    private int endPosY;

    private int startAngle;
    private int endAngle;

    private int startBias;
    private int endBias;
    private boolean startLabelEHorizontal;
    private boolean endLabelEHorizontal;

    private int startRelativeEdgeAngle;
    private int endRelativeEdgeAngle;
    private boolean startEdgeDirection;
    private boolean endEdgeDirection;
    private Vertex disjointedVertex;
    private Vertex endVertex;
    private boolean isDisjointedVertexA;

    private GraphElement draggedGraphElement;

    public DragOperation(GraphElement _draggedGraphElement) {
        draggedGraphElement = _draggedGraphElement;
    }

    public boolean changeMade() {
        if (draggedGraphElement instanceof Vertex) {
            return (startPosX != endPosX || startPosY != endPosY);
        } else if (draggedGraphElement instanceof Edge) {
            return (startRelativeEdgeAngle != endRelativeEdgeAngle);
        } else if (draggedGraphElement instanceof LabelV) {
            return (startAngle != endAngle);
        } else {
            return (startBias != endBias || startLabelEHorizontal != endLabelEHorizontal);
        }
    }

    public void setStartPos(int x, int y) {
        startPosX = x;
        startPosY = y;
    }

    public void setEndPos(int x, int y) {
        endPosX = x;
        endPosY = y;
    }

    public void setStartAngle(int _angle) {
        startAngle = _angle;
    }

    public void setEndAngle(int _angle) {
        endAngle = _angle;
    }

    public void setLabelEStartState(LabelE labelE) {
        startBias = labelE.getPosition();
        startLabelEHorizontal = labelE.isHorizontalPlacement();
    }

    public void setLabelEEndState(LabelE labelE) {
        endBias = labelE.getPosition();
        endLabelEHorizontal = labelE.isHorizontalPlacement();
    }

    public void setEdgeStartState(Edge edge, boolean _isDisjointedVertexA) {
        startRelativeEdgeAngle = edge.getRelativeEdgeAngle();
        startEdgeDirection = edge.isDirected();
        isDisjointedVertexA = _isDisjointedVertexA;
        if (isDisjointedVertexA) {
            disjointedVertex = edge.getVertexA();
        } else {
            disjointedVertex = edge.getVertexB();
        }
    }

    public void setEdgeEndState(Edge edge) {
        endRelativeEdgeAngle = edge.getRelativeEdgeAngle();
        endEdgeDirection = edge.isDirected();
        if (isDisjointedVertexA) {
            endVertex = edge.getVertexA();
        } else {
            endVertex = edge.getVertexB();
        }
    }

    public void restoreEdgeStartState() {
        ((Edge) draggedGraphElement).setDirected(startEdgeDirection);
        ((Edge) draggedGraphElement).setRelativeEdgeAngle(startRelativeEdgeAngle);
        if (isDisjointedVertexA) {
            ((Edge) draggedGraphElement).setVertexA(disjointedVertex);
        } else {
            ((Edge) draggedGraphElement).setVertexB(disjointedVertex);
        }
    }

    public Vertex getDisjointedVertex() {
        return disjointedVertex;
    }

    public boolean draggingA() {
        return isDisjointedVertexA;
    }

    public void restoreDisjointedVertex() {
        ((Edge) draggedGraphElement).setDirected(startEdgeDirection);
        if (isDisjointedVertexA) {
            ((Edge) draggedGraphElement).setVertexA(disjointedVertex);
        } else {
            ((Edge) draggedGraphElement).setVertexB(disjointedVertex);
        }
    }

    public void setEndVertex(Vertex vertex) {
        endVertex = vertex;
    }

    public String doOperation() {
        String result = null;

        if (draggedGraphElement instanceof Vertex) {
            ((Vertex) draggedGraphElement).setPosX(endPosX);
            ((Vertex) draggedGraphElement).setPosY(endPosY);
            result = StringLiterals.INFO_VERTEX_MOVE;
        } else if (draggedGraphElement instanceof Edge) {
            ((Edge) draggedGraphElement).setDirected(endEdgeDirection);
            ((Edge) draggedGraphElement).setRelativeEdgeAngle(endRelativeEdgeAngle);
            if (isDisjointedVertexA) {
                ((Edge) draggedGraphElement).setVertexA(endVertex);
            } else {
                ((Edge) draggedGraphElement).setVertexB(endVertex);
            }
            result = StringLiterals.INFO_EDGE_MOVE;
        } else if (draggedGraphElement instanceof LabelV) {
            ((LabelV) draggedGraphElement).setPosition(endAngle);
            result = StringLiterals.INFO_LABEL_V_MOVE;
        } else if (draggedGraphElement instanceof LabelE) {
            ((LabelE) draggedGraphElement).setPosition(endBias);
            ((LabelE) draggedGraphElement).setHorizontalPlacement(endLabelEHorizontal);
            result = StringLiterals.INFO_LABEL_E_MOVE;
        }
        return result;
    }

    public String undoOperation() {
        String result = null;

        if (draggedGraphElement instanceof Vertex) {
            ((Vertex) draggedGraphElement).setPosX(startPosX);
            ((Vertex) draggedGraphElement).setPosY(startPosY);
            result = StringLiterals.INFO_UNDO(StringLiterals.INFO_VERTEX_MOVE);
        } else if (draggedGraphElement instanceof Edge) {
            restoreEdgeStartState();
            result = StringLiterals.INFO_UNDO(StringLiterals.INFO_EDGE_MOVE);
        } else if (draggedGraphElement instanceof LabelV) {
            ((LabelV) draggedGraphElement).setPosition(startAngle);
            result = StringLiterals.INFO_UNDO(StringLiterals.INFO_LABEL_V_MOVE);
        } else if (draggedGraphElement instanceof LabelE) {
            ((LabelE) draggedGraphElement).setPosition(startBias);
            ((LabelE) draggedGraphElement).setHorizontalPlacement(startLabelEHorizontal);
            result = StringLiterals.INFO_UNDO(StringLiterals.INFO_LABEL_E_MOVE);
        }
        return result;
    }
}
