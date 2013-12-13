package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.constants.StringLiterals;
import pl.edu.agh.gratex.constants.ToolType;
import pl.edu.agh.gratex.controller.ToolController;
import pl.edu.agh.gratex.controller.ToolListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

@SuppressWarnings("serial")
public class ToolButton extends JButton implements ToolListener {

    private Image imageActive;
    private Image imagePassive;
    private ToolController toolController;
    private ToolType toolType;

    public ToolButton(String imageActiveName, String imagePassiveName, ToolController toolController, ToolType toolType) {
        this.toolController = toolController;
        toolController.addToolListener(this);
        this.toolType = toolType;
        try {
            URL url = this.getClass().getClassLoader().getResource("images/" + imageActiveName);
            imageActive = ImageIO.read(url);
            url = this.getClass().getClassLoader().getResource("images/" + imagePassiveName);
            imagePassive = ImageIO.read(url);
        } catch (Exception e) {
            toolController.getGeneralController().criticalError(StringLiterals.MESSAGE_ERROR_GET_RESOURCE, e);
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

        if (toolType == toolController.getTool()) {
            g.drawImage(imageActive, 10, 10, null);
        } else {
            g.drawImage(imagePassive, 10, 10, null);
        }

        if (toolType == toolController.getTool()) {
            g.setColor(new Color(50, 150, 250, 50));
            g.fillRect(3, 3, 44, 44);
        }
    }

    @Override
    public void toolChanged(ToolType previousTool, ToolType currentTool) {
        repaint();
    }

    @Override
    public int toolUpdatePriority() {
        return 0;
    }
}
