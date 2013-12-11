package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.view.ControlManager;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SelectionControllerImpl implements SelectionController, ToolListener, ModeListener, Serializable {
    private GeneralController generalController;

    private LinkedList<GraphElement> selection;
    private ToolType tool;
    private ModeType mode;

    public SelectionControllerImpl(GeneralController generalController, ModeController modeController, ToolController toolController) {
        this.generalController = generalController;
        modeController.addModeListener(this);
        toolController.addToolListener(this);

        selection = new LinkedList<>();
    }


    //===========================================
    // Listeners implementation

    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        mode = currentMode;
    }

    @Override
    public int modeUpdatePriority() {
        return 0;
    }

    @Override
    public void toolChanged(ToolType previousToolType, ToolType currentToolType) {
        tool = currentToolType;
    }

    @Override
    public int toolUpdatePriority() {
        return 0;
    }

    //===========================================
    // SelectionController interface implementation

    @Override
    public List<GraphElement> getSelection() {
        return selection;
    }

    @Override
    public int selectionSize() {
        return selection.size();
    }

    @Override
    public boolean selectionContains(Object o) {
        return selection.contains(o);
    }

    @Override
    public void clearSelection() {
        selection.clear();
    }

    @Override
    public void selectAll() {
        if (tool == ToolType.SELECT) {
            selection.clear();
            selection.addAll(generalController.getGraph().getElements(mode.getRelatedElementType()));
        }
    }

    @Override
    public void addToSelection(Collection<? extends GraphElement> elements, boolean controlDown) {
        if (controlDown) {
            for (GraphElement temp : elements) {
                if (selection.contains(temp)) {
                    selection.remove(temp);
                } else {
                    selection.add(temp);
                }
            }
        } else {
            selection.clear();
            selection.addAll(elements);
        }
        ControlManager.updatePropertyChangeOperationStatus(true);
    }

    @Override
    public void addToSelection(GraphElement element, boolean controlDown) {
        if (element != null) {
            if (controlDown) {
                if (selection.contains(element)) {
                    selection.remove(element);
                } else {
                    selection.add(element);
                }
            } else {
                selection.clear();
                selection.add(element);
            }
        } else {
            if (!controlDown) {
                selection.clear();
            }
        }
        ControlManager.updatePropertyChangeOperationStatus(true);
    }
}
