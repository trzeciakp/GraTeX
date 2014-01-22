package pl.edu.agh.gratex.controller.operation;


import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.constants.OperationType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.graph.GraphUtils;

import java.util.LinkedList;
import java.util.List;

public class GridToggleOperation extends Operation {
    GeneralController generalController;
    private boolean gridToggledOn;
    private int gridResolutionX;
    private int gridResolutionY;
    private AlterationOperation alterationOperation;

    public GridToggleOperation(GeneralController generalController, boolean gridToggledOn, int gridResolutionX, int gridResolutionY) {
        super(StringLiterals.INFO_GENERIC_GRID(gridToggledOn, gridResolutionX, gridResolutionY), OperationType.GRID_TOGGLE);
        this.generalController = generalController;
        this.gridToggledOn = gridToggledOn;
        this.gridResolutionX = gridResolutionX;
        this.gridResolutionY = gridResolutionY;
        generalController.getOperationController().registerOperation(this);
    }

    @Override
    public void doOperation() {
        if (gridToggledOn) {
            enableGrid();
        } else {
            disableGrid();
        }
    }

    @Override
    public void undoOperation() {
        if (gridToggledOn) {
            disableGrid();
        } else {
            enableGrid();
        }
    }

    private void enableGrid() {
        generalController.getGraph().setGridOn(true);
        generalController.getGraph().setGridResolutionX(gridResolutionX);
        generalController.getGraph().setGridResolutionY(gridResolutionY);

        if (gridToggledOn) {
            List<GraphElement> verticesAndBoundaries = new LinkedList<>(generalController.getGraph().getElements(GraphElementType.BOUNDARY));
            verticesAndBoundaries.addAll(generalController.getGraph().getElements(GraphElementType.VERTEX));
            alterationOperation = new AlterationOperation(generalController, verticesAndBoundaries, OperationType.ADJUST_ELEMENTS_TO_GRID,
                    StringLiterals.INFO_ELEMENTS_ADJUSTED_TO_GRID);
            GraphUtils.adjustElementsToGrid(generalController.getGraph());
            alterationOperation.finish(false);
            alterationOperation.doOperation();
        }
    }

    private void disableGrid() {
        generalController.getGraph().setGridOn(false);
        if (gridToggledOn) {
            alterationOperation.undoOperation();
        }
    }
}
