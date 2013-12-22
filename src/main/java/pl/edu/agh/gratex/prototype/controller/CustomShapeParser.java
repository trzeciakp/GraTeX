package pl.edu.agh.gratex.prototype.controller;

import pl.edu.agh.gratex.prototype.model.CustomShapeElement;
import pl.edu.agh.gratex.prototype.model.ElementFactory;

import java.awt.*;

/**
 *
 */
public class CustomShapeParser {

    private ElementFactory elementFactory;

    public CustomShapeParser(ElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public String parse() {
        StringBuilder result = new StringBuilder();
        result.append("\\documentclass{article} % say\n" +
                "\\usepackage{tikz,xcolor}\n" +
                "\\usetikzlibrary{arrows,snakes,backgrounds}\n" +
                "\\usetikzlibrary{topaths,calc}\n" +
                "\\begin{document}\n" +
                "\\begin{tikzpicture}");
        for (CustomShapeElement element : elementFactory.getCreatedElements()) {
            result.append("\\begin{pgfonlayer}{background}[fill opacity=0.8]\n" +
                    "\t\t\\filldraw[fill=blue!70] ");
            boolean first = true;
            for (Point point : element.getPoints()) {
                if(!first) {
                    result.append(" to ");
                }
                first = false;
                result.append("(").append(0.625*point.x).append("pt,").append(-0.625*point.y).append("pt)");
            }
            result.append(";\\end{pgfonlayer}");
        }
        result.append("    \n" +
                "\\end{tikzpicture}\n" +
                "\\end{document}");
        return result.toString();
    }
}
