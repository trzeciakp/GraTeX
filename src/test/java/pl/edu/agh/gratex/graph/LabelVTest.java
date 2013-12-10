package pl.edu.agh.gratex.model;

import org.junit.Test;
import org.mockito.Mockito;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.vertex.Vertex;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class LabelVTest {
    @Test
    public void testGetType() throws Exception {
        GraphElementType expectedType = GraphElementType.LABEL_VERTEX;
        Graph mockedGraph = Mockito.mock(Graph.class);
        Vertex mockedVertex = Mockito.mock(Vertex.class);
        assertEquals("test type", expectedType, new LabelV(mockedVertex, mockedGraph).getType());
    }

    @Test
    public void testRemove() throws Exception {

    }
}
