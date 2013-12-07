package pl.edu.agh.gratex.editor;


import pl.edu.agh.gratex.graph.Vertex;

import java.util.Comparator;

public class VertexOrderComparator implements Comparator<Vertex> {
    public int compare(Vertex x, Vertex y) {
        if (x.getNumber() < y.getNumber()) {
            return -1;
        } else if (x.getNumber() == y.getNumber()) {
            return 0;
        } else {
            return 1;
        }

    }
}
