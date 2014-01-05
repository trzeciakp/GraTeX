package pl.edu.agh.gratex.model.boundary;


import pl.edu.agh.gratex.constants.Const;

public class BoundaryUtils {
    public static boolean fitsIntoPage(Boundary boundary) {
        return !((boundary.getTopLeftX() - boundary.getLineWidth() / 2 < 0) ||
                (boundary.getTopLeftX() + boundary.getWidth() + boundary.getLineWidth() / 2 > Const.PAGE_WIDTH) ||
                (boundary.getTopLeftY() - boundary.getLineWidth() / 2 < 0) ||
                (boundary.getTopLeftY() + + boundary.getHeight() + boundary.getLineWidth() / 2 > Const.PAGE_HEIGHT));
    }
}
