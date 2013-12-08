package pl.edu.agh.gratex.gui;

import pl.edu.agh.gratex.controller.ToolController;

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
    private ToolController toolController;
    private ToolType toolType;

    //TODO maybe it should be tool listener and change icon when tool changes
    public ToolButton(String imageActiveName, String imagePassiveName, ToolController toolController, ToolType toolType) {
        this.toolController = toolController;
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
                ToolButton.this.toolController.setTool(ToolButton.this.toolType);
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //TODO hovering when mouse entered. why?
        if (hovering || toolType == toolController.getTool()) {
            g.drawImage(imageActive, 10, 10, null);
        } else {
            g.drawImage(imagePassive, 10, 10, null);
        }

        if (toolType == toolController.getTool()) {
            g.setColor(new Color(50, 150, 250, 40));
            g.fillRect(3, 3, 44, 44);
        }
    }
}
