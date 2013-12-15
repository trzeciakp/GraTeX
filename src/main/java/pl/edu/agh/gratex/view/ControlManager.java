package pl.edu.agh.gratex.view;


import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.editor.*;
import pl.edu.agh.gratex.model.*;
import pl.edu.agh.gratex.model.edge.EdgePropertyModel;
import pl.edu.agh.gratex.model.labelE.LabelEdgePropertyModel;
import pl.edu.agh.gratex.model.labelV.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.vertex.VertexPropertyModel;


public class ControlManager {
    public static MainWindow mainWindow;

    public static int selectionID = 0;
    public static OldOperationList operations = null;
    public static PropertyChangeOperation currentPropertyChangeOperation = null;


    public static void passWindowHandle(MainWindow _mainWindow) {
        mainWindow = _mainWindow;
    }

    public static void updatePropertyChangeOperationStatus(boolean newSelection) {
        //mainWindow.updateMenuBarAndActions();

        if (mainWindow.getGeneralController().getSelectionController().selectionSize() > 0) {
            if (newSelection) {
                selectionID++;
                mainWindow.getPanelPropertyEditor().setEnabled(true);
            }
            currentPropertyChangeOperation = new PropertyChangeOperation(mainWindow.getGeneralController().getSelectionController().getSelection(), selectionID);
            mainWindow.getPanelPropertyEditor().setModel(currentPropertyChangeOperation.initialModel);
        } else {
            currentPropertyChangeOperation = null;
            mainWindow.getPanelPropertyEditor().setEnabled(false);
            if (mainWindow.getGeneralController().getModeController().getMode() == ModeType.VERTEX) {
                mainWindow.getPanelPropertyEditor().setModel(new VertexPropertyModel());
            } else if (mainWindow.getGeneralController().getModeController().getMode() == ModeType.EDGE) {
                mainWindow.getPanelPropertyEditor().setModel(new EdgePropertyModel());
            } else if (mainWindow.getGeneralController().getModeController().getMode() == ModeType.LABEL_VERTEX) {
                mainWindow.getPanelPropertyEditor().setModel(new LabelVertexPropertyModel());
            } else if (mainWindow.getGeneralController().getModeController().getMode() == ModeType.LABEL_EDGE) {
                mainWindow.getPanelPropertyEditor().setModel(new LabelEdgePropertyModel());
            }
        }
    }
/*
    public static void updateSelectedItemsModel(PropertyModel pm) {
        if (currentPropertyChangeOperation != null) {
            if (!operations.mergePropertyChangeOperations(pm, selectionID)) {
                currentPropertyChangeOperation.setEndModel(pm);
                operations.addNewOperation(currentPropertyChangeOperation);
                mainWindow.getGeneralController().publishInfo(operations.redo());
            }
            mainWindow.getGeneralController().getOperationController().reportGenericOperation(null);
            updatePropertyChangeOperationStatus(false);
        }
    }*/
}
