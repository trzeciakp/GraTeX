package pl.edu.agh.gratex.graph;

import org.junit.Test;
import org.mockito.Mockito;
import pl.edu.agh.gratex.constants.GraphElementType;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class EdgeTest {
    @Test
    public void testGetType() throws Exception {

        GraphElementType expectedType = GraphElementType.EDGE;
        Graph mockedGraph = Mockito.mock(Graph.class);
        assertEquals("test type", expectedType, new Edge(mockedGraph).getType());
    }

    @Test
    public void testRemove() throws Exception {

    }
}
