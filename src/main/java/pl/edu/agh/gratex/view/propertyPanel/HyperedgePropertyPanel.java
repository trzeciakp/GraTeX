package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.hyperedge.HyperedgePropertyModel;

/**
 *
 */
public class HyperedgePropertyPanel extends AbstractPropertyPanel {
    @Override
    public PropertyModel getModel() {
        return new HyperedgePropertyModel();
    }

    @Override
    public void setModel(PropertyModel pm) {

    }

    @Override
    protected void initialize() {

    }
}
