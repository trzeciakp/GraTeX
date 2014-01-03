package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;

/**
 *
 */
public interface PropertyModelFactory {
    PropertyModel createEmptyModel(GraphElementType type);

    PropertyModel createDefaultModel(GraphElementType type);

    PropertyModel createTemplateModel(GraphElementType type);

    void setTemplateModel(GraphElementType type, PropertyModel model);
}
