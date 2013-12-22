package pl.edu.agh.gratex.prototype.view;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class LatexCodeDialog extends JDialog {
    public LatexCodeDialog(JFrame parent, String code) {
        super(parent, "Latex code", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(768, 768);
        setMinimumSize(new Dimension(200, 200));
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));
        JTextArea textArea_code = new JTextArea();
        textArea_code.setWrapStyleWord(true);
        textArea_code.setLineWrap(true);
        textArea_code.setText(code);
        textArea_code.setCaretPosition(0);
        add(textArea_code);
        this.setVisible(true);
    }
}
