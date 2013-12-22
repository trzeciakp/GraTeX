package pl.edu.agh.gratex.prototype.model;

import pl.edu.agh.gratex.prototype.controller.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class Element implements Drawable {

    private List<ElementListener> listeners = new ArrayList<ElementListener>();
    public void addListener(ElementListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ElementListener listener) {
        listeners.remove(listener);
    }

    public void informListeners() {
        for (ElementListener listener : listeners) {
            listener.fireElementChanged(this);
        }
    }

    public void destroy() {
        for (ElementListener listener : listeners) {
            listener.fireElementDeleted(this);
        }
    }
}
