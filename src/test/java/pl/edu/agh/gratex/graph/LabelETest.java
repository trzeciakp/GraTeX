package pl.edu.agh.gratex.graph;

import org.junit.Test;
import org.mockito.Mockito;
import pl.edu.agh.gratex.constants.GraphElementType;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class LabelETest {
    @Test
    public void testGetType() throws Exception {
        GraphElementType expectedType = GraphElementType.LABEL_EDGE;
        Graph mockedGraph = Mockito.mock(Graph.class);
        Edge mockedEdge = Mockito.mock(Edge.class);
        assertEquals("test type", expectedType, new LabelE(mockedEdge, mockedGraph).getType());
    }

    @Test
    public void testRemove() throws Exception {

    }
}
