package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolButtonType;
import pl.edu.agh.gratex.controller.ModeController;
import pl.edu.agh.gratex.controller.ToolController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

public class PanelToolbox extends JPanel {
    private static final long serialVersionUID = 1477920757019877516L;
;
    private JComboBox<ModeType> comboBox_mode;
    private ToolController toolController;
    private ModeController modeController;
    private final EnumMap<ToolButtonType,ToolButton> toolButtons;

    public PanelToolbox(ToolController toolController, ModeController modeController) {
        super();
        this.toolController = toolController;
        this.modeController = modeController;
        setLayout(null);

        comboBox_mode = new JComboBox<>(ModeType.values());
        comboBox_mode.setSelectedIndex(0);
        comboBox_mode.setBounds(0, 11, 90, 30);
        comboBox_mode.setToolTipText(StringLiterals.COMBOBOX_PANEL_TOOLBOX_MODE);
        comboBox_mode.setFocusable(false);
        toolButtons = new EnumMap<>(ToolButtonType.class);

        comboBox_mode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                PanelToolbox.this.modeController.setMode((ModeType) comboBox_mode.getSelectedItem());
                toolButtons.get(ToolButtonType.ADD).requestFocus();
            }
        });
        add(comboBox_mode);
        int y = 65;
        for(ToolButtonType tool : ToolButtonType.values()) {
            ToolButton toolButton = new ToolButton(tool.getImageActiveName(), tool.getImagePassiveName(), toolController, tool.getToolType());
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
        //TODO it should be changed to ModeListener
        if (comboBox_mode.getSelectedItem() != modeController.getMode()) {
            comboBox_mode.setSelectedItem(modeController.getMode());
        }
        paintChildren(g);
    }

    public ModeController getModeController() {
        return modeController;
    }
}
