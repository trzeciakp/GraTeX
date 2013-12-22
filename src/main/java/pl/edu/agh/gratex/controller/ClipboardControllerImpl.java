package pl.edu.agh.gratex.controller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ClipboardControllerImpl implements ClipboardController {

    private List<ClipboardListener> listeners = new ArrayList<>();

    @Override
    public void addListener(ClipboardListener clipboardListener) {
        listeners.add(clipboardListener);
    }

    @Override
    public void removeListener(ClipboardListener clipboardListener) {
        listeners.remove(clipboardListener);
    }

    @Override
    public void setCopyingEnabled(boolean enabled) {
        for (ClipboardListener listener : listeners) {
            //listener.setCopyingEnabled(enabled);
        }
    }

    @Override
    public void setPastingEnabled(boolean enabled) {
        for (ClipboardListener listener : listeners) {
            //listener.setPastingEnabled(enabled);
        }
    }


}
