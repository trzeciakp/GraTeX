package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.StringLiterals;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

@SuppressWarnings("serial")
public class SaveFileDialog extends JFileChooser {

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
            @Override
            public boolean accept(File file) {
                String fileName = file.getName().toLowerCase();
                return fileName.endsWith(Const.GRAPH_FILES_EXTENSION) || file.isDirectory();
            }

            @Override
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

    public File showDialog(MainWindow mainWindow) {
        int option = showOpenDialog(mainWindow);
        if (option == JFileChooser.APPROVE_OPTION) {
            if (getSelectedFile() != null) {
                File file = getSelectedFile();
                if (!file.getName().endsWith(Const.GRAPH_FILES_EXTENSION)) {
                    return new File(file.toString().concat(Const.GRAPH_FILES_EXTENSION));
                }
                return file;
            }
        }
        return null;
    }
}
