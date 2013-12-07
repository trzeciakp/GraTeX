package pl.edu.agh.gratex.gui;

import pl.edu.agh.gratex.controller.GeneralController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.Map;

public class PanelButtonContainer extends JPanel {
    private static final long serialVersionUID = 4702408962784933450L;

    private GeneralController generalController;
    private EnumMap<ActionType, ActionButton> buttons = new EnumMap<ActionType, ActionButton>(ActionType.class);

    private enum ActionType {
        NEW_GRAPH("new.png", "New graph"),
        OPEN_GRAPH("open.png", "Open graph file"),
        SAVE_GRAPH("save.png", "Save"),
        EDIT_TEMPLATE("defaults.png", "Edit graph's template"),
        COPY_SUBGRAPH("copy.png", "Copy selected subgraph"),
        PASTE_SUBGRAPH("paste.png", "Paste copied subgraph"),
        UNDO("undo.png", "Undo"),
        REDO("redo.png", "Redo"),
        TOGGLE_GRID("grid.png", "Toggle grid on/off"),
        NUMERATION_PREFERENCE("numeration.png", "Numeration preferences"),
        SHOW_CODE("tex.png", "Show graph's LaTeX code");

        private String imageName;
        private String tooltip;

        ActionType(String imageName, String tooltip) {
            this.imageName = imageName;
            this.tooltip = tooltip;
        }

        private String getImageName() {
            return imageName;
        }

        private String getTooltip() {
            return tooltip;
        }
    }

    public PanelButtonContainer(GeneralController generalController) {
        super();
        setLayout(null);
        setFocusTraversalKeysEnabled(false);
        this.generalController = generalController;

        int x = 10;
        for (ActionType actionType : ActionType.values()) {
            ActionButton actionButton = new ActionButton(actionType.getImageName(), actionType.getTooltip(), getPerformedAction(actionType));
            actionButton.setBounds(x, 5, 40, 40);
            actionButton.setFocusable(false);
            add(actionButton);
            buttons.put(actionType, actionButton);
            x += 45;
        }
        buttons.get(ActionType.COPY_SUBGRAPH).setEnabled(false);
        buttons.get(ActionType.PASTE_SUBGRAPH).setEnabled(false);
    }

    public void updateFunctions() {
        buttons.get(ActionType.COPY_SUBGRAPH).setEnabled(false);
        buttons.get(ActionType.PASTE_SUBGRAPH).setEnabled(false);
        if (ControlManager.mode == ControlManager.VERTEX_MODE && ControlManager.selection.size() > 0) {
            buttons.get(ActionType.COPY_SUBGRAPH).setEnabled(true);
        }
        if (ControlManager.currentCopyPasteOperation != null) {
            buttons.get(ActionType.PASTE_SUBGRAPH).setEnabled(true);
        }
    }

    private ActionListener getPerformedAction(ActionType actionType) {
        switch (actionType) {
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
                //TODO maybe null? in case when you forgot to add action
                return new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    }
                };
        }
    }
}
