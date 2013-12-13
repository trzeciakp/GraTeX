package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.MouseController;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyHandler implements KeyEventDispatcher {
    MouseController mouseController;

    public KeyHandler(GeneralController generalController) {
        this.mouseController = generalController.getMouseController();
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                mouseController.processShiftPressing(true);
            } else {
                return false;
            }
        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                mouseController.processShiftPressing(false);
            } else {
                return false;
            }
        } else if (e.getID() == KeyEvent.KEY_TYPED) {
            return false;
        } else {
            return false;
        }
        return true;
    }
}
