package pl.edu.agh.gratex.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class ActionButton extends JButton {
    private static final long serialVersionUID = 3129459381915495010L;

    private Image image;

    public ActionButton(String imageName, String tooltip, ActionListener actionListener) {
        try {
            URL url = this.getClass().getClassLoader().getResource("images/" + imageName);
            image = ImageIO.read(url);
        } catch (Exception e) {
            //TODO
        }
        setToolTipText(tooltip);
        addActionListener(actionListener);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 8, 8, null);
        if (!isEnabled()) {
            g.setColor(new Color(220, 220, 220, 200));
            g.fillRect(5, 5, 30, 30);
        }
    }
}
