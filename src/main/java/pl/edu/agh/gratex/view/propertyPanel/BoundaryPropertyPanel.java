package pl.edu.agh.gratex.view.propertyPanel;

import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.boundary.BoundaryPropertyModel;

/**
 *
 */
public class BoundaryPropertyPanel extends AbstractPropertyPanel {
    @Override
    public PropertyModel getModel() {
        return new BoundaryPropertyModel();
    }

    @Override
    public void setModel(PropertyModel pm) {

    }
}
