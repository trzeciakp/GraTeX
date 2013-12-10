package pl.edu.agh.gratex.gui;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.editor.OperationList;
import pl.edu.agh.gratex.graph.Graph;
import pl.edu.agh.gratex.graph.utils.GraphUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class OpenFileDialog extends JFileChooser {
    private static final long serialVersionUID = -7745470295118891369L;

    public OpenFileDialog(File currentFile) {
        super(currentFile.getParent());
        init();
    }

    public OpenFileDialog() {
        super();
        init();
    }

    private void init() {
        setDialogTitle(StringLiterals.TITLE_OPEN_FILE_DIALOG);
        setApproveButtonText(StringLiterals.BUTTON_OPEN_FILE_DIALOG_APPROVE);
        setApproveButtonToolTipText(StringLiterals.TOOLTIP_OPEN_FILE_DIALOG_APPROVE);
        setFileFilter(new FileFilter() {
            public boolean accept(File file) {
                String fileName = file.getName().toLowerCase();
                if (fileName.endsWith(Const.GRAPH_FILES_EXTENSION) || file.isDirectory()) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return StringLiterals.TOOLTIP_OPEN_FILE_DIALOG_DESCRIPTION;
            }
        });
    }

    public void showDialog(MainWindow mainWindow) {
        int option = showOpenDialog(mainWindow);
        if (option == JFileChooser.APPROVE_OPTION) {
            if (getSelectedFile() != null) {
                File file = getSelectedFile();

                Graph _graph;
                if ((_graph = FileManager.openFile(file)) != null) {
                    ControlManager.currentFile = file;
                    GraphUtils.deleteUnusedLabels(_graph);
                    ControlManager.graph = _graph;
                    ControlManager.operations = new OperationList();
                    ControlManager.changeMade = false;
                    ControlManager.applyChange();
                    ControlManager.publishInfo(StringLiterals.INFO_GRAPH_OPEN_OK);
                } else {
                    ControlManager.publishInfo(StringLiterals.INFO_GRAPH_OPEN_FAIL);
                    ControlManager.reportError(StringLiterals.MESSAGE_ERROR_OPEN_GRAPH);
                }
            }
        }
    }

}
