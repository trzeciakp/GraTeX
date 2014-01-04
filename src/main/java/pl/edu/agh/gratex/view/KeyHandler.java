package pl.edu.agh.gratex.view;

import pl.edu.agh.gratex.controller.GeneralController;
import pl.edu.agh.gratex.controller.mouse.MouseController;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyHandler implements KeyEventDispatcher {
    // TODO Jednak tego nie mozemy wyrzucic, bo potrzebujemy przechwytywac nacisniecia shifta jak nie rusza sie mysz
    MouseController mouseController;

    public KeyHandler(GeneralController generalController) {
        this.mouseController = generalController.getMouseController();
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                mouseController.processShiftPressing(true);
            } else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                mouseController.processCtrlPressing(true);
            } else {
                return false;
            }
        } else if (e.getID() == KeyEvent.KEY_RELEASED) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                mouseController.processShiftPressing(false);
            } else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                mouseController.processCtrlPressing(false);
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
