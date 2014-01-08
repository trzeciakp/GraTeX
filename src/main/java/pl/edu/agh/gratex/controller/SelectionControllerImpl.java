package pl.edu.agh.gratex.controller;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.operation.OperationController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SelectionControllerImpl implements SelectionController, ToolListener, ModeListener {
    private OperationController operationController;

    private List<GraphElement> selection = new LinkedList<>();
    private List<SelectionListener> listeners = new ArrayList<>();
    private ModeType mode = ModeType.VERTEX;
    private ToolType tool = ToolType.ADD;

    public SelectionControllerImpl(ModeController modeController, ToolController toolController, OperationController operationController) {
        this.operationController = operationController;
        modeController.addModeListener(this);
        toolController.addToolListener(this);
    }


    //===========================================
    // Listeners implementation

    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        mode = currentMode;
        clearSelection();
    }

    @Override
    public int modeUpdatePriority() {
        return 0;
    }

    @Override
    public void toolChanged(ToolType previousTool, ToolType currentTool) {
        tool = currentTool;
        clearSelection();
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
    public void clearSelection() {
        addToSelection(new ArrayList<GraphElement>(), false);
    }

    @Override
    public void selectAll(Graph graph) {
        addToSelection(graph.getElements(mode.getRelatedElementType()), false);
    }

    @Override
    public void addToSelection(GraphElement element, boolean controlDown) {
        if (element != null) {
            List<GraphElement> list = new ArrayList<>();
            list.add(element);
            addToSelection(list, controlDown);
        } else {
            // TODO why?
            // TODO Bo jak element jest null to kliknelismy w puste miejsce. Jeśli jest control, to nie czyścimy zaznaczenia, ale jeśli nie ma, to tak
            if (!controlDown) {
                clearSelection();
            }
        }
        operationController.reportOperationEvent(null);
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
        operationController.reportOperationEvent(null);
        informListeners();
    }

    @Override
    public void repeatSelection() {
        informListeners();
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
