package pl.edu.agh.gratex.gui;

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
        setDialogTitle("Save as...");
        setApproveButtonText("Save");
        setApproveButtonToolTipText("Save graph");
        setFileFilter(new FileFilter() {
            public boolean accept(File file) {
                String fileName = file.getName().toLowerCase();
                if (fileName.endsWith(".gph") || file.isDirectory()) {
                    return true;
                }
                return false;
            }

            public String getDescription() {
                return "Graph file (*.gph)";
            }
        });
    }

    @Override
    public void approveSelection() {
        File file = getSelectedFile();
        if (file.exists()) {
            int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Confirm", JOptionPane.YES_NO_OPTION);
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
                if (!file.getName().endsWith(".gph")) {
                    file = new File(file.toString().concat(".gph"));
                }

                if (FileManager.saveFile(file, ControlManager.graph)) {
                    ControlManager.publishInfo("Graph saved successfully");
                    ControlManager.currentFile = file;
                    ControlManager.changeMade = false;
                    return true;
                } else {
                    ControlManager.publishInfo("Saving graph failed!");
                    ControlManager.reportError("Saving graph failed.\nCheck whether the target file is write-protected.");
                }
            }
        }

        return false;
    }
}
