package pl.edu.agh.gratex.view;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.StringLiterals;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

@SuppressWarnings("serial")
public class OpenFileDialog extends JFileChooser {
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
            @Override
            public boolean accept(File file) {
                String fileName = file.getName().toLowerCase();
                return fileName.endsWith(Const.GRAPH_FILES_EXTENSION) || file.isDirectory();
            }

            @Override
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
