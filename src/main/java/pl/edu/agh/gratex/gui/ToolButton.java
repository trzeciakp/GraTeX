package pl.edu.agh.gratex.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

public class ToolButton extends JButton implements MouseListener {
    private static final long serialVersionUID = -6621417247591518118L;

    private Image imageActive;
    private Image imagePassive;
    private int toolID;
    private boolean hovering;

    public ToolButton(String imageActiveName, String imagePassiveName, int _toolID) {
        toolID = _toolID;
        try {
            URL url = this.getClass().getClassLoader().getResource("images/" + imageActiveName);
            imageActive = ImageIO.read(url);
            url = this.getClass().getClassLoader().getResource("images/" + imagePassiveName);
            imagePassive = ImageIO.read(url);
        } catch (Exception e) {
        }
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (hovering || ControlManager.tool == toolID) {
            g.drawImage(imageActive, 10, 10, null);
        } else {
            g.drawImage(imagePassive, 10, 10, null);
        }

        if (ControlManager.tool == toolID) {
            g.setColor(new Color(50, 150, 250, 40));
            g.fillRect(3, 3, 44, 44);
        }
    }

    public void mouseClicked(MouseEvent e) {
        ControlManager.changeTool(toolID);
    }

    public void mouseEntered(MouseEvent e) {
        //hovering = true;
        repaint();
    }

    public void mouseExited(MouseEvent e) {
        //hovering = false;
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }
}
