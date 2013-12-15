package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.model.GraphElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SelectionControllerImpl implements SelectionController, ToolListener, ModeListener, Serializable {
    private GeneralController generalController;

    private List<GraphElement> selection = new LinkedList<>();
    private List<SelectionListener> listeners = new ArrayList<>();
    private ModeType mode = ModeType.VERTEX;
    private ToolType tool = ToolType.ADD;

    public SelectionControllerImpl(GeneralController generalController, ModeController modeController, ToolController toolController) {
        this.generalController = generalController;
        modeController.addModeListener(this);
        toolController.addToolListener(this);
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
    public void toolChanged(ToolType previousTool, ToolType currentTool) {
        tool = currentTool;
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
    public boolean selectionContains(GraphElement element) {
        return selection.contains(element);
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
        //TODO not sure if it should not be somewhere else
        ClipboardController clipboardController = generalController.getClipboardController();
        clipboardController.setCopyingEnabled(mode == ModeType.VERTEX && selectionSize() > 0);
        informListeners();
        //ControlManager.updatePropertyChangeOperationStatus(true);
    }

    @Override
    public void clearSelection() {
        addToSelection(new ArrayList<GraphElement>(), false);
        //selection.clear();
    }

    @Override
    public void selectAll() {
        //TODO why is it checked?
        if (tool == ToolType.SELECT) {
            addToSelection(generalController.getGraph().getElements(mode.getRelatedElementType()), false);
            //selection.clear();
            //selection.addAll(generalController.getGraph().getElements(mode.getRelatedElementType()));
        }
    }

    @Override
    public void addToSelection(GraphElement element, boolean controlDown) {
        if (element != null) {
            List<GraphElement> list = new ArrayList<>();
            list.add(element);
            addToSelection(list, controlDown);
        } else {
            //TODO why?
            if (!controlDown) {
                clearSelection();
            }
        }
    }

    @Override
    public void addListener(SelectionListener listener) {
        listeners.add(listener);

    }

    @Override
    public void removeListener(SelectionListener listener) {
        listeners.remove(listener);
    }

    private void informListeners() {
        for (SelectionListener listener : listeners) {
            listener.selectionChanged(selection);
        }
    }
}
