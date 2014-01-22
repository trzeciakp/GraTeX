package pl.edu.agh.gratex.model.boundary;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.graph.Graph;

public class BoundaryUtils {
    public static boolean fitsIntoPage(Boundary boundary) {
        return !((boundary.getTopLeftX() - boundary.getLineWidth() / 2 < 0) ||
                (boundary.getTopLeftX() + boundary.getWidth() + boundary.getLineWidth() / 2 > Const.PAGE_WIDTH) ||
                (boundary.getTopLeftY() - boundary.getLineWidth() / 2 < 0) ||
                (boundary.getTopLeftY() + +boundary.getHeight() + boundary.getLineWidth() / 2 > Const.PAGE_HEIGHT));
    }

    public static void adjustToGrid(Boundary boundary) {
        Graph graph = boundary.getGraph();
        boundary.setTopLeftX(((boundary.getTopLeftX() + (graph.getGridResolutionX() / 2)) / graph.getGridResolutionX()) * graph.getGridResolutionX());
        boundary.setTopLeftY(((boundary.getTopLeftY() + (graph.getGridResolutionY() / 2)) / graph.getGridResolutionY()) * graph.getGridResolutionY());
        boundary.setWidth(((boundary.getWidth() + ((graph.getGridResolutionX() - 1) / 2)) / graph.getGridResolutionX()) * graph.getGridResolutionX());
        boundary.setHeight(((boundary.getHeight() + ((graph.getGridResolutionY() - 1) / 2)) / graph.getGridResolutionY()) * graph.getGridResolutionY());
    }
}
