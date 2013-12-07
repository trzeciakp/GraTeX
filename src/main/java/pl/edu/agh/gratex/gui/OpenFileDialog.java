package pl.edu.agh.gratex.gui;


import pl.edu.agh.gratex.editor.OperationList;
import pl.edu.agh.gratex.graph.Graph;

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
        setDialogTitle("Open...");
        setApproveButtonText("Open");
        setApproveButtonToolTipText("Open graph");
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

    public void showDialog(MainWindow mainWindow) {
        int option = showOpenDialog(mainWindow);
        if (option == JFileChooser.APPROVE_OPTION) {
            if (getSelectedFile() != null) {
                File file = getSelectedFile();

                Graph _graph;
                if ((_graph = FileManager.openFile(file)) != null) {
                    ControlManager.currentFile = file;
                    _graph.deleteUnusedLabels();
                    ControlManager.graph = _graph;
                    ControlManager.operations = new OperationList();
                    ControlManager.changeMade = false;
                    ControlManager.applyChange();
                    ControlManager.publishInfo("Graph loaded successfully");
                } else {
                    ControlManager.publishInfo("Loading graph failed!");
                    ControlManager.reportError("Loading graph failed.\nThe chosen file has either improper format or is damaged.");
                }
            }
        }
    }

}
