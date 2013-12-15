package pl.edu.agh.gratex.controller;

/**
 *
 */
public interface ClipboardController {

    public void addListener(ClipboardListener clipboardListener);

    public void removeListener(ClipboardListener clipboardListener);

    public void setCopyingEnabled(boolean enabled);

    public void setPastingEnabled(boolean enabled);
}
