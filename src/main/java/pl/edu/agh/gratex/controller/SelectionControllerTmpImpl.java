package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.view.ControlManager;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SelectionControllerTmpImpl implements SelectionController, ToolListener, ModeListener {
    private LinkedList<GraphElement> selection;
    private ToolType tool;
    private ModeType mode;

    public SelectionControllerTmpImpl(ModeController modeController, ToolController toolController) {
        selection = new LinkedList<>();
        modeController.addModeListener(this);
        toolController.addToolListener(this);
    }


    //===========================================
    // Listeners implementation

    @Override
    public void toolChanged(ToolType previousToolType, ToolType currentToolType) {
        tool = currentToolType;
    }

    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        mode = currentMode;
    }

    @Override
    public int modeUpdatePriority() {
        return 10;
    }

    @Override
    public int toolUpdatePriority() {
        return 10;
    }

//===========================================
    // SelectionController interface implementation

    @Override
    public List<GraphElement> getSelection() {
        return selection;
    }

    @Override
    public int getSize() {
        return selection.size();
    }

    @Override
    public boolean contains(Object o) {
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
            selection.addAll(ControlManager.mainWindow.getGeneralController().getGraph().getElements(mode.getRelatedElementType()));
        }
    }

    @Override
    public void addToSelection(Collection<? extends GraphElement> elements, boolean controlDown) {
        if (controlDown) {
            for(GraphElement temp : elements) {
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
    }
}
