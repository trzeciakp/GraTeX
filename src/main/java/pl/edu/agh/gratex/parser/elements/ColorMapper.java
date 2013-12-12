package pl.edu.agh.gratex.parser.elements;

import java.awt.*;

/**
 *
 */
public interface ColorMapper {

    public Color getColor(String colorText);
    public String getColorText(Color color);
}
