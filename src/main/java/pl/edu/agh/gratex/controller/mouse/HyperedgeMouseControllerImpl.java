package pl.edu.agh.gratex.controller.mouse;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.operation.AlterationOperation;
import pl.edu.agh.gratex.controller.operation.CreationRemovalOperation;
import pl.edu.agh.gratex.controller.operation.GenericOperation;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.GraphElementFactory;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.hyperedge.HyperedgeUtils;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.view.Application;

/**
 *
 */
public class HyperedgeMouseControllerImpl extends GraphElementMouseController {
    private Hyperedge currentlyAddedHyperedge;
    private Hyperedge currentlyDraggedHyperedge;
    private AlterationOperation currentDragOperation;
    private Vertex edgeDragDummy;
    private boolean movingJoint;
    private String tempLatexCode;

    public HyperedgeMouseControllerImpl(GeneralController generalController, GraphElementFactory graphElementFactory, GraphElementType handledGraphElementType) {
        super(generalController, graphElementFactory, handledGraphElementType);
        edgeDragDummy = (Vertex) getGraphElementFactory().create(GraphElementType.VERTEX, null);
        edgeDragDummy.setRadius(2);
    }

    @Override
    public void shiftDownChanged() {
        if (movingJoint && currentlyDraggedHyperedge != null) {
            if (shiftDown) {
                currentlyDraggedHyperedge.autoCenterJoint();
            } else {
                int oldX = currentlyDraggedHyperedge.getJointCenterX();
                int oldY = currentlyDraggedHyperedge.getJointCenterY();
                currentlyDraggedHyperedge.setJointCenterX(mouseX);
                currentlyDraggedHyperedge.setJointCenterY(mouseY);
                if (!HyperedgeUtils.fitsIntoPage(currentlyDraggedHyperedge)) {
                    currentlyDraggedHyperedge.setJointCenterX(oldX);
                    currentlyDraggedHyperedge.setJointCenterY(oldY);
                }
            }
        }
    }

    @Override
    public void reset() {
        finishMoving();
        currentlyAddedHyperedge = null;
        currentlyDraggedHyperedge = null;
        currentDragOperation = null;
        tempLatexCode = null;
    }

    @Override
    public GraphElement getCurrentlyAddedElement() {
        if (currentlyAddedHyperedge != null) {
            Vertex vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY);
            if (vertex == null) {
                edgeDragDummy.setPosX(mouseX);
                edgeDragDummy.setPosY(mouseY);
            } else {
                edgeDragDummy.setPosX(vertex.getPosX());
                edgeDragDummy.setPosY(vertex.getPosY());
            }
            currentlyAddedHyperedge.autoCenterJoint();
            return currentlyAddedHyperedge;
        }
        return null;
    }

    @Override
    public void addNewElement(int mouseX, int mouseY) {
        if (currentlyAddedHyperedge == null) {
            Vertex vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY);
            if (vertex == null) {
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_HYPEREDGE_START));
            } else {
                currentlyAddedHyperedge = (Hyperedge) getGraphElementFactory().create(GraphElementType.HYPEREDGE, generalController.getGraph());
                currentlyAddedHyperedge.getConnectedVertices().add(vertex);
                currentlyAddedHyperedge.getConnectedVertices().add(edgeDragDummy);
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_CHOOSE_HYPEREDGE_END));
            }
        } else {
            Vertex vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY);
            if (vertex == null) {
                currentlyAddedHyperedge = null;
                generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_HYPEREDGE_ADDING_CANCELLED));
            } else {
                if (!currentlyAddedHyperedge.getConnectedVertices().contains(vertex)) {
                    currentlyAddedHyperedge.getConnectedVertices().remove(edgeDragDummy);
                    currentlyAddedHyperedge.getConnectedVertices().add(vertex);
                    new CreationRemovalOperation(generalController, currentlyAddedHyperedge, OperationType.ADD_HYPEREDGE, StringLiterals.INFO_HYPEREDGE_ADD, true);
                    generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_HYPEREDGE_HOW_TO_EXPAND));
                    currentlyAddedHyperedge = null;
                } else {
                    currentlyAddedHyperedge = null;
                    generalController.getOperationController().reportOperationEvent(new GenericOperation(StringLiterals.INFO_HYPEREDGE_ADDING_CANCELLED));
                }
            }
        }
    }

    @Override
    public void moveSelection(int mouseX, int mouseY) {
        if (currentlyDraggedHyperedge == null) {
            currentlyDraggedHyperedge = (Hyperedge) generalController.getGraph().getElementFromPosition(GraphElementType.HYPEREDGE, mouseX, mouseY);
            if (currentlyDraggedHyperedge != null) {
                startMoving();
            }
        } else {
            continueMoving();
        }
    }

    private void startMoving() {
        tempLatexCode = null;
        movingJoint = false;
        if (ctrlDown) {
            if (currentlyDraggedHyperedge.getJointArea().contains(mouseX, mouseY)) {
                currentDragOperation = new AlterationOperation(generalController, currentlyDraggedHyperedge, OperationType.SHRINK_HYPEREDGE, StringLiterals.INFO_HYPEREDGE_EXTEND);
                edgeDragDummy.setPosX(mouseX);
                edgeDragDummy.setPosY(mouseY);
                currentlyDraggedHyperedge.getConnectedVertices().add(edgeDragDummy);
            } else { // only joint works with Ctrl
                currentlyDraggedHyperedge = null;
            }
        } else { // !ctrlDown
            Vertex vertex;
            if (currentlyDraggedHyperedge.getJointArea().contains(mouseX, mouseY)) {
                movingJoint = true;
                currentDragOperation = new AlterationOperation(generalController, currentlyDraggedHyperedge, OperationType.MOVE_HYPEREDGE, StringLiterals.INFO_HYPEREDGE_JOINT_MOVE);
            } else if ((vertex = currentlyDraggedHyperedge.getVertexByEdge(mouseX, mouseY)) != null) {
                if (currentlyDraggedHyperedge.getConnectedVertices().size() == 2) {
                    tempLatexCode = generalController.getParseController().getParserByElementType(GraphElementType.HYPEREDGE).parseToLatex(currentlyDraggedHyperedge);
                }
                currentDragOperation = new AlterationOperation(generalController, currentlyDraggedHyperedge, OperationType.SHRINK_HYPEREDGE, StringLiterals.INFO_HYPEREDGE_SHRINK);
                currentlyDraggedHyperedge.getConnectedVertices().remove(vertex);
                currentlyDraggedHyperedge.getConnectedVertices().add(edgeDragDummy);
            } else {
                currentlyDraggedHyperedge = null;
            }
        }
    }

    private void continueMoving() {
        if (movingJoint) {
            // No need to duplicate code
            shiftDownChanged();
        } else {
            edgeDragDummy.setPosX(mouseX);
            edgeDragDummy.setPosY(mouseY);
            Vertex vertex;
            if ((vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY)) != null) {
                if (!currentlyDraggedHyperedge.getConnectedVertices().contains(vertex)) {
                    edgeDragDummy.setPosX(vertex.getPosX());
                    edgeDragDummy.setPosY(vertex.getPosY());
                }
            }
            currentlyDraggedHyperedge.autoCenterJoint();
        }
    }

    @Override
    public void finishMoving() {
        if (currentlyDraggedHyperedge != null) {
            if (!movingJoint) {
                currentlyDraggedHyperedge.getConnectedVertices().remove(edgeDragDummy);
                Vertex vertex;
                if ((vertex = (Vertex) generalController.getGraph().getElementFromPosition(GraphElementType.VERTEX, mouseX, mouseY)) != null) {
                    if (!currentlyDraggedHyperedge.getConnectedVertices().contains(vertex)) {
                        currentlyDraggedHyperedge.getConnectedVertices().add(vertex);
                    }
                }
                currentlyDraggedHyperedge.autoCenterJoint();
                if (currentlyDraggedHyperedge.getConnectedVertices().size() == 1) {
                    // Restore hyeredge state (because there is only one vertex left)
                    try {
                        generalController.getParseController().getParserByElementType(GraphElementType.HYPEREDGE).
                                updateElementWithCode(currentlyDraggedHyperedge, tempLatexCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Application.criticalError("Parser error", e);
                    }
                }
            }
            currentDragOperation.finish();
            currentlyDraggedHyperedge = null;
        }
    }
}
