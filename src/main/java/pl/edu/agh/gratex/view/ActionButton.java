package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.controller.GeneralController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

@SuppressWarnings("serial")
public class ActionButton extends JButton {

    private Image image;

    public ActionButton(String imageName, String tooltip, ActionListener actionListener) {
        image = Application.loadImage(imageName);
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
