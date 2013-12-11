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
    public static OperationList operations = null;
    public static PropertyChangeOperation currentPropertyChangeOperation = null;


    public static void passWindowHandle(MainWindow _mainWindow) {
        mainWindow = _mainWindow;
    }

    public static void updatePropertyChangeOperationStatus(boolean newSelection) {
        mainWindow.menuBar.updateFunctions();
        mainWindow.panel_buttonContainer.updateFunctions();

        if (mainWindow.getSelectionController().getSize() > 0) {
            if (newSelection) {
                selectionID++;
                mainWindow.panel_propertyEditor.setEnabled(true);
            }
            currentPropertyChangeOperation = new PropertyChangeOperation(mainWindow.getSelectionController().getSelection(), selectionID);
            mainWindow.panel_propertyEditor.setModel(currentPropertyChangeOperation.initialModel);
        } else {
            currentPropertyChangeOperation = null;
            mainWindow.panel_propertyEditor.setEnabled(false);
            if (mainWindow.getGeneralController().getMode() == ModeType.VERTEX) {
                mainWindow.panel_propertyEditor.setModel(new VertexPropertyModel());
            } else if (mainWindow.getGeneralController().getMode() == ModeType.EDGE) {
                mainWindow.panel_propertyEditor.setModel(new EdgePropertyModel());
            } else if (mainWindow.getGeneralController().getMode() == ModeType.LABEL_VERTEX) {
                mainWindow.panel_propertyEditor.setModel(new LabelVertexPropertyModel());
            } else if (mainWindow.getGeneralController().getMode() == ModeType.LABEL_EDGE) {
                mainWindow.panel_propertyEditor.setModel(new LabelEdgePropertyModel());
            }
        }
    }

    public static void updateSelectedItemsModel(PropertyModel pm) {
        if (currentPropertyChangeOperation != null) {
            if (!operations.mergePropertyChangeOperations(pm, selectionID)) {
                currentPropertyChangeOperation.setEndModel(pm);
                operations.addNewOperation(currentPropertyChangeOperation);
                mainWindow.getGeneralController().publishInfo(operations.redo());
            }
            mainWindow.updateWorkspace();
            updatePropertyChangeOperationStatus(false);
        }
    }
}
