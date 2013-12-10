package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.StringLiterals;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;

public class AboutDialog extends JDialog {
    private static final long serialVersionUID = 2858349132119113260L;

    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                dispose();
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return rootPane;
    }

    public AboutDialog(MainWindow parent) {
        super(parent, StringLiterals.TITLE_ABOUT_DIALOG, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 137);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        JTextArea txtrGratexVersion = new JTextArea() {
            private static final long serialVersionUID = 1L;

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    URL url = this.getClass().getClassLoader().getResource("images/icon.png");
                    g.drawImage(ImageIO.read(url), 250, 42, null);
                } catch (Exception e) {
                }
            }
        };
        txtrGratexVersion
                .setText(StringLiterals.MESSAGE_ABOUT_DIALOG);
        txtrGratexVersion.setFocusable(false);
        txtrGratexVersion.setBounds(0, 0, 294, 108);
        getContentPane().add(txtrGratexVersion);
        setVisible(true);
    }
}
