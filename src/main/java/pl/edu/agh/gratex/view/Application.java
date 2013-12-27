package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.StringLiterals;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

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
                    new MainWindow().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    criticalError("Error while creating main window", e);
                }
            }
        });
    }

    public static void reportError(String message, Exception e) {
        String fullMessage = message + (e == null ? "" : "\n\n" + e.toString());
        JOptionPane.showMessageDialog(null, fullMessage, StringLiterals.TITLE_ERROR_DIALOG, JOptionPane.ERROR_MESSAGE);
    }

    public static void criticalError(String message, Exception e) {
        reportError(StringLiterals.MESSAGE_ERROR_CRITICAL + message, e);
        System.exit(1);
    }

    public static Image loadImage(String imageName) {
        try {
            return ImageIO.read(Application.class.getResource("/images/" + imageName));
        }
        catch(Exception e) {
            criticalError(StringLiterals.MESSAGE_ERROR_GET_RESOURCE, e);
            return null;
        }
    }
}
