package pl.edu.agh.gratex.view;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.editor.OperationList;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.graph.GraphUtils;

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

    public File showDialog(MainWindow mainWindow) {
        int option = showOpenDialog(mainWindow);
        if (option == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile();
        }
        return null;
    }

}
