package pl.edu.agh.gratex.view;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyHandler implements KeyEventDispatcher {
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                ControlManager.processShiftPressing(true);
            } else {
                return false;
            }
        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                ControlManager.processShiftPressing(false);
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
