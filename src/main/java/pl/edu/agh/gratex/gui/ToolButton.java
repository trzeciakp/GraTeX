package pl.edu.agh.gratex.gui;

import pl.edu.agh.gratex.controller.GeneralController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class ToolButton extends JButton {
    private static final long serialVersionUID = -6621417247591518118L;

    private Image imageActive;
    private Image imagePassive;
    private boolean hovering;
    private GeneralController generalController;
    private ToolType toolType;

    //TODO
    public ToolButton(String imageActiveName, String imagePassiveName, GeneralController generalController, ToolType toolType) {
        this.generalController = generalController;
        this.toolType = toolType;
        try {
            URL url = this.getClass().getClassLoader().getResource("images/" + imageActiveName);
            imageActive = ImageIO.read(url);
            url = this.getClass().getClassLoader().getResource("images/" + imagePassiveName);
            imagePassive = ImageIO.read(url);
        } catch (Exception e) {
            //TODO
        }
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ToolButton.this.generalController.changeTool(ToolButton.this.toolType);
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //TODO hovering when mouse entered. why?
        if (hovering || toolType == generalController.getTool()) {
            g.drawImage(imageActive, 10, 10, null);
        } else {
            g.drawImage(imagePassive, 10, 10, null);
        }

        if (toolType == generalController.getTool()) {
            g.setColor(new Color(50, 150, 250, 40));
            g.fillRect(3, 3, 44, 44);
        }
    }
}
