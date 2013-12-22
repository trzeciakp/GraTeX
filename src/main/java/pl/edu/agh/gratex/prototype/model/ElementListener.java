package pl.edu.agh.gratex.prototype.model;

/**
 *
 */
public interface ElementListener {
    void fireElementChanged(Element element);

    void fireElementDeleted(Element element);
}
