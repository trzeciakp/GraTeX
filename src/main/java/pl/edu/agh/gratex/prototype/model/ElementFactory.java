package pl.edu.agh.gratex.prototype.model;

import pl.edu.agh.gratex.prototype.view.EditorPanel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ElementFactory {


    private List<CustomShapeElement> createdElements = new ArrayList<CustomShapeElement>();

    public void setEditorPanel(EditorPanel editorPanel) {
        this.editorPanel = editorPanel;
    }

    private EditorPanel editorPanel;

    public ElementFactory(EditorPanel editorPanel) {
        this.editorPanel = editorPanel;
    }

    public ElementFactory() {

    }

    public CustomShapeElement createElement() {
        CustomShapeElement customShapeElement = new CustomShapeElement(editorPanel);
        createdElements.add(customShapeElement);
        return customShapeElement;
    }

    public List<CustomShapeElement> getCreatedElements() {
        return createdElements;
    }

    public void clear() {
        for (CustomShapeElement createdElement : createdElements) {
            createdElement.destroy();
        }
        createdElements.clear();
    }
}
