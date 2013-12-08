package pl.edu.agh.gratex.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

//TODO it is never used
public class ModeSwitch extends JComponent implements MouseListener {
    private static final long serialVersionUID = -3210943331545567524L;

    private Image[] label;
    private boolean mouseOver;

    public ModeSwitch() {
        addMouseListener(this);

        try {
            label = new Image[4];
            URL url = this.getClass().getClassLoader().getResource("images/modelabelgreen.gif");
            label[1] = ImageIO.read(url);
            url = this.getClass().getClassLoader().getResource("images/modelabelblue.gif");
            label[2] = ImageIO.read(url);
            url = this.getClass().getClassLoader().getResource("images/modelabelred.gif");
            label[3] = ImageIO.read(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);

        if (!mouseOver) {
            g.drawImage(label[ControlManager.mode], 5, 1, null);
            g.drawString("Vertex", 28, 18);
        } else {
            g.drawRect(4, 0, 81, 76);
            if (ControlManager.mode == 1) {
                g.drawImage(label[1], 5, 1, null);
                g.drawImage(label[2], 5, 26, null);
                g.drawImage(label[3], 5, 51, null);
            } else if (ControlManager.mode == 2) {
                g.drawImage(label[2], 5, 1, null);
                g.drawImage(label[1], 5, 26, null);
                g.drawImage(label[3], 5, 51, null);
            } else {
                g.drawImage(label[3], 5, 1, null);
                g.drawImage(label[1], 5, 26, null);
                g.drawImage(label[2], 5, 51, null);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getY() < 26) {
            ControlManager.changeMode(ControlManager.mode);
        } else if (e.getY() < 51) {
            if (ControlManager.mode == 1) {
                ControlManager.changeMode(2);
            } else {
                ControlManager.changeMode(1);
            }
        } else {
            if (ControlManager.mode == 3) {
                ControlManager.changeMode(2);
            } else {
                ControlManager.changeMode(3);
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
        mouseOver = true;
        repaint();
    }

    public void mouseExited(MouseEvent e) {
        mouseOver = false;
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }
}
