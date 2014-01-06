package pl.edu.agh.gratex.model.hyperedge;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.model.vertex.Vertex;

public class HyperedgeUtils {
    public static boolean fitsIntoPage(Hyperedge hyperedge) {
        return !((hyperedge.getJointCenterX() - hyperedge.getJointSize() - 1 / 2 < 0) ||
                (hyperedge.getJointCenterX() + hyperedge.getJointSize() + 1 / 2 > Const.PAGE_WIDTH) ||
                (hyperedge.getJointCenterY() - hyperedge.getJointSize() - 1 / 2 < 0) ||
                (hyperedge.getJointCenterY() + hyperedge.getJointSize() + 1 / 2 > Const.PAGE_HEIGHT));
    }
}
