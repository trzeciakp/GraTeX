package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.ActionButtonType;
import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.controller.ClipboardController;
import pl.edu.agh.gratex.controller.ClipboardListener;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.MouseController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

public class PanelButtonContainer extends JPanel implements ClipboardListener {
    private static final long serialVersionUID = 4702408962784933450L;

    private GeneralController generalController;
    private MouseController mouseController;
    private EnumMap<ActionButtonType, ActionButton> buttons = new EnumMap<>(ActionButtonType.class);

    public PanelButtonContainer(GeneralController generalController, MouseController mouseController, ClipboardController clipboardController) {
        super();
        setLayout(null);
        setFocusTraversalKeysEnabled(false);
        this.generalController = generalController;
        this.mouseController = mouseController;
        clipboardController.addListener(this);

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
        buttons.get(ActionButtonType.COPY_SUBGRAPH).setEnabled(false);
        buttons.get(ActionButtonType.PASTE_SUBGRAPH).setEnabled(false);
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
            case COPY_SUBGRAPH:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.copyToClipboard();
                    }
                };
            case PASTE_SUBGRAPH:
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PanelButtonContainer.this.generalController.pasteFromClipboard();
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
    public void setCopyingEnabled(boolean copyingEnabled) {
        buttons.get(ActionButtonType.COPY_SUBGRAPH).setEnabled(copyingEnabled);
    }

    @Override
    public void setPastingEnabled(boolean pastingEnabled) {
        buttons.get(ActionButtonType.PASTE_SUBGRAPH).setEnabled(pastingEnabled);

    }
}
