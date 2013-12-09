package pl.edu.agh.gratex.graph;

import org.junit.Test;
import org.mockito.Mockito;
import pl.edu.agh.gratex.constants.GraphElementType;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class VertexTest {
    @Test
    public void testGetType() throws Exception {
        GraphElementType expectedType = GraphElementType.VERTEX;
        Graph mockedGraph = Mockito.mock(Graph.class);
        assertEquals("test type", expectedType, new Vertex(mockedGraph).getType());
    }

    @Test
    public void testRemove() throws Exception {
        //TODO
    }
}
