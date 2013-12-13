package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.ModeType;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.ModeListener;
import pl.edu.agh.gratex.controller.OperationListener;
import pl.edu.agh.gratex.controller.ToolListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class InfoDisplay extends JLabel implements ModeListener, ToolListener, OperationListener {

    private ModeType mode = ModeType.VERTEX;
    private ToolType tool = ToolType.ADD;

    public InfoDisplay(GeneralController generalController) {
        generalController.getModeController().addModeListener(this);
        generalController.getToolController().addToolListener(this);
        generalController.getOperationController().addOperationListener(this);
    }

    private void maybeSetInfo(String info) {
        if (info != null) {
            setText(info);
        }
    }

    // ===================================
    // ModeListener implementation
    @Override
    public void modeChanged(ModeType previousMode, ModeType currentMode) {
        mode = currentMode;
        setText(StringLiterals.INFO_MODE_AND_TOOL(mode, tool));
    }

    @Override
    public int modeUpdatePriority() {
        return 0;
    }


    // ===================================
    // ToolListener implementation
    @Override
    public void toolChanged(ToolType previousTool, ToolType currentTool) {
        tool = currentTool;
        setText(StringLiterals.INFO_MODE_AND_TOOL(mode, tool));
    }

    @Override
    public int toolUpdatePriority() {
        return 0;
    }


    // ===================================
    // OperationListener implementation
    @Override
    public void operationStarted(String info) {
        maybeSetInfo(info);
    }

    @Override
    public void operationInProgress(String info) {
        maybeSetInfo(info);
    }

    @Override
    public void operationFinished(String info) {
        maybeSetInfo(info);
    }
}
