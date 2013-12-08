package pl.edu.agh.gratex.gui;

import pl.edu.agh.gratex.constants.Constants;
import pl.edu.agh.gratex.constants.StringLiterals;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class SaveFileDialog extends JFileChooser {
    private static final long serialVersionUID = 2052316076238542736L;

    public SaveFileDialog(File currentFile) {
        super(currentFile.getParent());
        init();
    }

    public SaveFileDialog() {
        super();
        init();
    }

    private void init() {
        setDialogTitle(StringLiterals.TITLE_SAVE_FILE_DIALOG);
        setApproveButtonText(StringLiterals.BUTTON_SAVE_FILE_DIALOG_APPROVE);
        setApproveButtonToolTipText(StringLiterals.TOOLTIP_SAVE_FILE_DIALOG_APPROVE);
        setFileFilter(new FileFilter() {
            public boolean accept(File file) {
                String fileName = file.getName().toLowerCase();
                if (fileName.endsWith(Constants.GRAPH_FILES_EXTENSION) || file.isDirectory()) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return StringLiterals.TOOLTIP_SAVE_FILE_DIALOG_DESCRIPTION;
            }
        });
    }

    @Override
    public void approveSelection() {
        File file = getSelectedFile();
        if (file.exists()) {
            int result = JOptionPane.showConfirmDialog(this, StringLiterals.MESSAGE_CONFIRM_OVERWRITE_FILE, StringLiterals.TITLE_CONFIRM_DIALOG, JOptionPane.YES_NO_OPTION);
            switch (result) {
                case JOptionPane.YES_OPTION:
                    super.approveSelection();
                    return;
                case JOptionPane.NO_OPTION:
                    return;
                case JOptionPane.CANCEL_OPTION:
                    cancelSelection();
                    return;
            }
        }
        super.approveSelection();
    }

    public boolean showDialog(MainWindow mainWindow) {
        int option = showOpenDialog(mainWindow);
        if (option == JFileChooser.APPROVE_OPTION) {
            if (getSelectedFile() != null) {
                File file = getSelectedFile();
                if (!file.getName().endsWith(Constants.GRAPH_FILES_EXTENSION)) {
                    file = new File(file.toString().concat(Constants.GRAPH_FILES_EXTENSION));
                }

                if (FileManager.saveFile(file, ControlManager.graph)) {
                    ControlManager.publishInfo(StringLiterals.INFO_GRAPH_SAVE_OK);
                    ControlManager.currentFile = file;
                    ControlManager.changeMade = false;
                    return true;
                } else {
                    ControlManager.publishInfo(StringLiterals.INFO_GRAPH_SAVE_FAIL);
                    ControlManager.reportError(StringLiterals.MESSAGE_ERROR_SAVE_GRAPH);
                }
            }
        }

        return false;
    }
}
