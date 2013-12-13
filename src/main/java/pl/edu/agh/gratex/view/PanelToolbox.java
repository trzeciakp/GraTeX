package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolButtonType;
import pl.edu.agh.gratex.controller.ModeController;
import pl.edu.agh.gratex.controller.ModeListener;
import pl.edu.agh.gratex.controller.ToolController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class PanelToolbox extends JPanel implements ModeListener {
    private ModeController modeController;

    private JComboBox<ModeType> comboBox_mode;

    // TODO wyglada na to, ze nie bedziemy potrzebowali wyciagac tych guzikow, wiec nie ma potrzeby pakowac ich do mapy
    //private final EnumMap<ToolButtonType, ToolButton> toolButtons;

    public PanelToolbox(ToolController toolController, ModeController modeController) {
        super();

        this.modeController = modeController;
        modeController.addModeListener(this);

        setLayout(null);
        comboBox_mode = new JComboBox<>(ModeType.values());
        comboBox_mode.setSelectedIndex(0);
        comboBox_mode.setBounds(0, 11, 90, 30);
        comboBox_mode.setToolTipText(StringLiterals.COMBOBOX_PANEL_TOOLBOX_MODE);
        comboBox_mode.setFocusable(false);
        //toolButtons = new EnumMap<>(ToolButtonType.class);

        comboBox_mode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // This if ensures there is no event loop (setSelectedItem -> actionPerformed -> setMode -> setSelectedItem)
                if (PanelToolbox.this.modeController.getMode() != comboBox_mode.getSelectedItem()) {
                    PanelToolbox.this.modeController.setMode((ModeType) comboBox_mode.getSelectedItem());
                }
            }
        });
        add(comboBox_mode);
        int y = 65;
        for (ToolButtonType toolButtonType : ToolButtonType.values()) {
            ToolButton toolButton = new ToolButton(toolButtonType.getImageActiveName(), toolButtonType.getImagePassiveName(), toolController, toolButtonType.getToolType());
            toolButton.setToolTipText(toolButtonType.getTooltip());
            toolButton.setFocusable(false);
            toolButton.setBounds(20, y, 50, 50);
            //toolButtons.put(toolButtonType, toolButton);
            add(toolButton);
            y += 60;
        }
    }

    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        // This if ensures there is no event loop (setSelectedItem -> actionPerformed -> setMode -> setSelectedItem)
        if (comboBox_mode.getSelectedItem() != currentMode) {
            comboBox_mode.setSelectedItem(currentMode);
        }
    }

    @Override
    public int modeUpdatePriority() {
        return 0;
    }
}
