package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.StringLiterals;

import javax.swing.*;
import java.awt.*;

public class Application {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            javax.swing.UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                    new MainWindow();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, StringLiterals.MESSAGE_ERROR_GENERAL + e.toString(),
                            StringLiterals.TITLE_ERROR_DIALOG, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
