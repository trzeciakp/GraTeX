package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.StringLiterals;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {

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

        JTextArea aboutTextArea = new JTextArea() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    URL url = this.getClass().getClassLoader().getResource("images/icon.png");
                    g.drawImage(ImageIO.read(url), 250, 42, null);
                } catch (Exception e) {
                }
            }
        };
        aboutTextArea.setText(StringLiterals.MESSAGE_ABOUT_DIALOG);
        aboutTextArea.setFocusable(false);
        aboutTextArea.setBounds(0, 0, 294, 108);
        getContentPane().add(aboutTextArea);
        setVisible(true);
    }
}
