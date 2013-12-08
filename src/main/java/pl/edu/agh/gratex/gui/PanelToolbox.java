package pl.edu.agh.gratex.gui;

import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.graph.GraphElementType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

public class PanelToolbox extends JPanel {
    private static final long serialVersionUID = 1477920757019877516L;
;
    private JComboBox<GraphElementType> comboBox_mode;
    private GeneralController generalController;
    private final EnumMap<Tools,ToolButton> toolButtons;

    public PanelToolbox(GeneralController generalController) {
        super();
        this.generalController = generalController;
        setLayout(null);

        comboBox_mode = new JComboBox<>(GraphElementType.values());
        comboBox_mode.setSelectedIndex(0);
        comboBox_mode.setBounds(0, 11, 90, 30);
        comboBox_mode.setToolTipText("Edition mode");
        comboBox_mode.setFocusable(false);
        toolButtons = new EnumMap<>(Tools.class);

        comboBox_mode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                PanelToolbox.this.generalController.changeMode((GraphElementType) comboBox_mode.getSelectedItem());

                toolButtons.get(Tools.ADD).requestFocus();
            }
        });
        add(comboBox_mode);
        int y = 65;
        for(Tools tool : Tools.values()) {
            ToolButton toolButton = new ToolButton(tool.getImageActiveName(), tool.getImagePassiveName(), generalController, tool.getToolType());
            toolButton.setToolTipText(tool.getTooltip());
            toolButton.setFocusable(false);
            toolButton.setBounds(20, y, 50, 50);
            toolButtons.put(tool, toolButton);
            add(toolButton);
            y += 60;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //TODO
        if (comboBox_mode.getSelectedItem() != generalController.getMode()) {
            comboBox_mode.setSelectedItem(generalController.getMode());
        }
        paintChildren(g);
    }

    private enum Tools {
        ADD("addtool.png", "addtoolpassive.png", "Add tool", ToolType.ADD),
        REMOVE("removetool.png", "removetoolpassive.png", "Remove tool", ToolType.REMOVE),
        SELECT("selecttool.png", "selecttoolpassive.png", "Select tool", ToolType.SELECT);
        private String imageActiveName;
        private String imagePassiveName;
        private String tooltip;
        private ToolType toolType;

        Tools(String imageActiveName, String imagePassiveName, String tooltip, ToolType toolType) {
            this.imageActiveName = imageActiveName;
            this.imagePassiveName = imagePassiveName;
            this.tooltip = tooltip;
            this.toolType = toolType;
        }

        private String getImageActiveName() {
            return imageActiveName;
        }

        private String getImagePassiveName() {
            return imagePassiveName;
        }

        private String getTooltip() {
            return tooltip;
        }

        private ToolType getToolType() {
            return toolType;
        }
    }
}
