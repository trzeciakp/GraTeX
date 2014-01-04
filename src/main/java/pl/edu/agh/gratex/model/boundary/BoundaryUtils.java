package pl.edu.agh.gratex.model.boundary;


import pl.edu.agh.gratex.constants.Const;

public class BoundaryUtils {
    public static boolean fitsIntoPage(Boundary boundary) {
        return !((boundary.getLeftCornerX() - boundary.getLineWidth() / 2 < 0) ||
                (boundary.getLeftCornerX() + boundary.getWidth() + boundary.getLineWidth() / 2 > Const.PAGE_WIDTH) ||
                (boundary.getLeftCornerY() - boundary.getLineWidth() / 2 < 0) ||
                (boundary.getLeftCornerY() + + boundary.getHeight() + boundary.getLineWidth() / 2 > Const.PAGE_HEIGHT));
    }
}
