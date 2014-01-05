package pl.edu.agh.gratex.parser.elements;

import pl.edu.agh.gratex.model.PropertyModel;

import java.awt.*;

/**
 *
 */
public class ColorMapperTmpImpl implements ColorMapper {
    @Override
    public Color getColor(String colorText) {
        return PropertyModel.REVERSE_COLORS.get(colorText);
    }

    @Override
    public String getColorText(Color color) {
        return PropertyModel.COLORS.get(color);
    }

    @Override
    public Color getTemplateColor() {
        return Color.black;
    }
}
