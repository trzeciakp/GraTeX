package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.ActionButtonType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.controller.*;
import pl.edu.agh.gratex.controller.mouse.MouseController;
import pl.edu.agh.gratex.model.GraphElement;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.List;

@SuppressWarnings("serial")
public class PanelButtonContainer extends JPanel implements SelectionListener {

    private GeneralController generalController;
    private EnumMap<ActionButtonType, ActionButton> buttons = new EnumMap<>(ActionButtonType.class);

    public PanelButtonContainer(GeneralController generalController, SelectionController selectionController) {
        super();
        this.generalController = generalController;
        selectionController.addListener(this);

        setLayout(null);
        setFocusTraversalKeysEnabled(false);

        int x = 10;
        for (ActionButtonType actionButtonType : ActionButtonType.values()) {
            ActionButton actionButton = new ActionButton(generalController, actionButtonType.getImageName(),
                    actionButtonType.getTooltip(), getActionListener(actionButtonType));
            actionButton.setBounds(x, 5, 40, 40);
            actionButton.setFocusable(false);
            add(actionButton);
            buttons.put(actionButtonType, actionButton);
            x += 45;
        }
        buttons.get(ActionButtonType.DUPLICATE_SUBGRAPH).setEnabled(false);
    }

    //TODO is it unused? should be
    /*public void updateFunctions() {
        buttons.get(ActionButtonType.COPY_SUBGRAPH).setEnabled(false);
        buttons.get(ActionButtonType.PASTE_SUBGRAPH).setEnabled(false);
        if (generalController.getModeController().getMode() == ModeType.VERTEX && generalController.getSelectionController().selectionSize() > 0) {
            buttons.get(ActionButtonType.COPY_SUBGRAPH).setEnabled(true);
        }
        if (mouseController.clipboardNotEmpty()) {
            buttons.get(ActionButtonType.PASTE_SUBGRAPH).setEnabled(true);
        }
    }*/

    private ActionListener getActionListener(ActionButtonType ActionButtonType) {
        switch (ActionButtonType) {
            case NEW_GRAPH:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.newGraphFile();
                    }
                };
            case OPEN_GRAPH:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.openGraphFile();
                    }
                };
            case SAVE_GRAPH:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.saveGraphFile(false);
                    }
                };
            case EDIT_TEMPLATE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.editGraphTemplate();
                    }
                };
            case DUPLICATE_SUBGRAPH:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.duplicateSubgraph();
                    }
                };
            case UNDO:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.undo();
                    }
                };
            case REDO:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.redo();
                    }
                };
            case TOGGLE_GRID:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.toggleGrid();
                    }
                };
            case NUMERATION_PREFERENCE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.setNumeration();
                    }
                };
            case SHOW_CODE:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.parseToTeX();
                    }
                };
            default:
                throw new RuntimeException("An action button is missing ActionListener");
        }
    }

    @Override
    public void selectionChanged(List<? extends GraphElement> collection) {
        boolean duplicationEnabled = collection.size() > 0 && generalController.getModeController().getMode() == ModeType.VERTEX;
        buttons.get(ActionButtonType.DUPLICATE_SUBGRAPH).setEnabled(duplicationEnabled);
    }
}
