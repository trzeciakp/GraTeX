package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.graph.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;

import java.util.Iterator;
import java.util.LinkedList;


public class PropertyChangeOperation extends Operation {
    private LinkedList<GraphElement> elements;
    public LinkedList<PropertyModel> initialModels;
    public PropertyModel initialModel;
    public PropertyModel endModel;
    public int selectionID;

    public PropertyChangeOperation(LinkedList<GraphElement> _elements, int _selectionID) {
        selectionID = _selectionID;
        elements = new LinkedList<GraphElement>(_elements);
        initialModels = new LinkedList<PropertyModel>();

        Iterator<GraphElement> it = elements.listIterator();
        while (it.hasNext()) {
            initialModels.add(it.next().getModel());
        }

        it = elements.listIterator();
        initialModel = it.next().getModel();
        while (it.hasNext()) {
            initialModel.andOperator(it.next().getModel());
        }
    }

    public void setEndModel(PropertyModel pm) {
        endModel = pm;
    }

    public String doOperation() {
        Iterator<GraphElement> it = elements.listIterator();
        while (it.hasNext()) {
            it.next().setModel(endModel);
        }

        return "Property changed";
    }

    public String undoOperation() {
        Iterator<GraphElement> itge = elements.listIterator();
        Iterator<PropertyModel> itpm = initialModels.listIterator();
        while (itge.hasNext()) {
            itge.next().setModel(itpm.next());
        }
        return "Property change undone";
    }
}
