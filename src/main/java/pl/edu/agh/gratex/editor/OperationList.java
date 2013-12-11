package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.view.ControlManager;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;

import java.util.LinkedList;


public class OperationList {
    public static final int MAX_OPERATIONS = 256;
    private LinkedList<Operation> operations;
    private int iterator;

    public OperationList() {
        operations = new LinkedList<Operation>();
        iterator = 0;
    }

    public void addNewOperation(Operation operation) {
        while (iterator < operations.size()) {
            operations.removeLast();
        }
        while (operations.size() > MAX_OPERATIONS) {
            operations.removeFirst();
        }
        operations.add(operation);
    }

    public String undo() {
        if (iterator < 1) {
            return StringLiterals.INFO_NOTHING_TO_UNDO;
        } else {
            return operations.get(--iterator).undoOperation();
        }
    }

    public String redo() {
        if (iterator == operations.size()) {
            return StringLiterals.INFO_NOTHING_TO_REDO;
        } else {
            return operations.get(iterator++).doOperation();
        }
    }

    public boolean mergePropertyChangeOperations(PropertyModel pm, int _selectionID) {
        if (operations.size() > 0) {
            if (operations.getLast() instanceof PropertyChangeOperation) {
                PropertyChangeOperation operation = (PropertyChangeOperation) operations.getLast();
                if (operation.selectionID == _selectionID) {
                    if (ControlManager.mainWindow.getGeneralController().getMode() == ModeType.VERTEX) {
                        VertexPropertyModel oldInitialModel = (VertexPropertyModel) operation.initialModel;
                        VertexPropertyModel oldEndModel = (VertexPropertyModel) operation.endModel;
                        VertexPropertyModel newModel = (VertexPropertyModel) pm;

                        boolean merge = false;

                        if (oldEndModel.radius > -1 && oldInitialModel.radius > -1 && oldEndModel.radius != oldInitialModel.radius) {
                            if (oldEndModel.radius > -1 && newModel.radius > -1 && oldEndModel.radius != newModel.radius) {
                                merge = true;
                            }
                        }

                        if (oldEndModel.lineWidth > -1 && oldInitialModel.lineWidth > -1 && oldEndModel.lineWidth != oldInitialModel.lineWidth) {
                            if (oldEndModel.lineWidth > -1 && newModel.lineWidth > -1 && oldEndModel.lineWidth != newModel.lineWidth) {
                                merge = true;
                            }
                        }

                        if (oldEndModel.number > -1 && oldInitialModel.number > -1 && oldEndModel.number != oldInitialModel.number) {
                            if (oldEndModel.number > -1 && newModel.number > -1 && oldEndModel.number != newModel.number) {
                                merge = true;
                            }
                        }

                        if (merge) {
                            operation.endModel = newModel;
                            undo();
                            ControlManager.mainWindow.getGeneralController().publishInfo(redo());
                            return true;
                        }
                    } else if (ControlManager.mainWindow.getGeneralController().getMode() == ModeType.EDGE) {
                        EdgePropertyModel oldInitialModel = (EdgePropertyModel) operation.initialModel;
                        EdgePropertyModel oldEndModel = (EdgePropertyModel) operation.endModel;
                        EdgePropertyModel newModel = (EdgePropertyModel) pm;

                        boolean merge = false;

                        if (oldEndModel.relativeEdgeAngle > -1 && oldInitialModel.relativeEdgeAngle > -1
                                && oldEndModel.relativeEdgeAngle != oldInitialModel.relativeEdgeAngle) {
                            if (oldEndModel.relativeEdgeAngle > -1 && newModel.relativeEdgeAngle > -1
                                    && oldEndModel.relativeEdgeAngle != newModel.relativeEdgeAngle) {
                                merge = true;
                            }
                        }

                        if (oldEndModel.lineWidth > -1 && oldInitialModel.lineWidth > -1 && oldEndModel.lineWidth != oldInitialModel.lineWidth) {
                            if (oldEndModel.lineWidth > -1 && newModel.lineWidth > -1 && oldEndModel.lineWidth != newModel.lineWidth) {
                                merge = true;
                            }
                        }

                        if (merge) {
                            operation.endModel = newModel;
                            undo();
                            ControlManager.mainWindow.getGeneralController().publishInfo(redo());
                            return true;
                        }
                    } else if (ControlManager.mainWindow.getGeneralController().getMode() == ModeType.LABEL_VERTEX) {
                        LabelVertexPropertyModel oldInitialModel = (LabelVertexPropertyModel) operation.initialModel;
                        LabelVertexPropertyModel oldEndModel = (LabelVertexPropertyModel) operation.endModel;
                        LabelVertexPropertyModel newModel = (LabelVertexPropertyModel) pm;

                        boolean merge = false;

                        if (oldEndModel.spacing > -1 && oldInitialModel.spacing > -1 && oldEndModel.spacing != oldInitialModel.spacing) {
                            if (oldEndModel.spacing > -1 && newModel.spacing > -1 && oldEndModel.spacing != newModel.spacing) {
                                merge = true;
                            }
                        }

                        if (merge) {
                            operation.endModel = newModel;
                            undo();
                            ControlManager.mainWindow.getGeneralController().publishInfo(redo());
                            return true;
                        }
                    } else {
                        LabelEdgePropertyModel oldInitialModel = (LabelEdgePropertyModel) operation.initialModel;
                        LabelEdgePropertyModel oldEndModel = (LabelEdgePropertyModel) operation.endModel;
                        LabelEdgePropertyModel newModel = (LabelEdgePropertyModel) pm;

                        boolean merge = false;

                        if (oldEndModel.spacing > -1 && oldInitialModel.spacing > -1 && oldEndModel.spacing != oldInitialModel.spacing) {
                            if (oldEndModel.spacing > -1 && newModel.spacing > -1 && oldEndModel.spacing != newModel.spacing) {
                                merge = true;
                            }
                        }
                        if (oldEndModel.position > -1 && oldInitialModel.position > -1 && oldEndModel.position != oldInitialModel.position) {
                            if (oldEndModel.position > -1 && newModel.position > -1 && oldEndModel.position != newModel.position) {
                                merge = true;
                            }
                        }

                        if (merge) {
                            operation.endModel = newModel;
                            undo();
                            ControlManager.mainWindow.getGeneralController().publishInfo(redo());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
