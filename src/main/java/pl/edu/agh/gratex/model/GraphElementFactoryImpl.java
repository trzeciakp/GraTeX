package pl.edu.agh.gratex.model;

import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.boundary.Boundary;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.hyperedge.Hyperedge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.linkBoundary.LinkBoundary;
import pl.edu.agh.gratex.model.properties.LabelPosition;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.model.vertex.VertexUtils;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class GraphElementFactoryImpl implements GraphElementFactory {

    private DrawerFactory drawerFactory;
    private PropertyModelFactory propertyModelFactory;
    private Graph exampleGraph;

    public GraphElementFactoryImpl(DrawerFactory drawerFactory, PropertyModelFactory propertyModelFactory) {
        this.drawerFactory = drawerFactory;
        this.propertyModelFactory = propertyModelFactory;
        initExampleGraph();
    }

    @Override
    public GraphElement create(GraphElementType type, Graph graph) {
        GraphElement result = null;
        PropertyModel propertyModel = propertyModelFactory.createTemplateModel(type);
        switch (type) {
            case VERTEX:
                result = new Vertex(graph, propertyModel);
                break;
            case EDGE:
                result = new Edge(graph, propertyModel);
                break;
            case LABEL_VERTEX:
                result = new LabelV(null, graph, propertyModel);
                break;
            case LABEL_EDGE:
                result = new LabelE(null, graph, propertyModel);
                break;
            case HYPEREDGE:
                result = new Hyperedge(graph, propertyModel);
                break;
            case BOUNDARY:
                result = new Boundary(graph, propertyModel);
                break;
            case LINK_BOUNDARY:
                result = new LinkBoundary(graph, propertyModel);
                break;
        }
        result.setDrawer(drawerFactory.createDefaultDrawer(type));
        return result;
    }

    @Override
    public DrawerFactory getDrawerFactory() {
        return drawerFactory;
    }

    @Override
    public PropertyModelFactory getPropertyModelFactory() {
        //TODO
        return propertyModelFactory;
    }

    private void initExampleGraph() {
        exampleGraph = new Graph();
        exampleGraph.setGridOn(false);

        Vertex vertex1 = (Vertex) this.create(GraphElementType.VERTEX, exampleGraph);
        vertex1.setNumber(1);
        VertexUtils.setPartOfNumeration(vertex1, true);
        vertex1.setPosX(120);
        vertex1.setPosY(255);
        exampleGraph.addElement(vertex1);

        Vertex vertex2 = (Vertex) this.create(GraphElementType.VERTEX, exampleGraph);
        vertex2.setNumber(2);
        VertexUtils.setPartOfNumeration(vertex2, true);
        vertex2.setPosX(400);
        vertex2.setPosY(255);
        exampleGraph.addElement(vertex2);

        Vertex vertex3 = (Vertex) this.create(GraphElementType.VERTEX, exampleGraph);
        vertex3.setNumber(3);
        VertexUtils.setPartOfNumeration(vertex3, true);
        vertex3.setPosX(260);
        vertex3.setPosY(90);
        exampleGraph.addElement(vertex3);

        Edge edge1 = (Edge) this.create(GraphElementType.EDGE, exampleGraph);
        edge1.setVertexA(vertex2);
        edge1.setVertexB(vertex3);
        edge1.setRelativeEdgeAngle(330);
        exampleGraph.addElement(edge1);

        Edge edge2 = (Edge) this.create(GraphElementType.EDGE, exampleGraph);
        edge2.setVertexA(vertex1);
        edge2.setVertexB(vertex1);
        edge2.setRelativeEdgeAngle(90);
        exampleGraph.addElement(edge2);

        Edge edge3 = (Edge) this.create(GraphElementType.EDGE, exampleGraph);
        edge3.setVertexA(vertex1);
        edge3.setVertexB(vertex2);
        edge3.setRelativeEdgeAngle(0);
        exampleGraph.addElement(edge3);

        LabelV labelV1 = (LabelV) this.create(GraphElementType.LABEL_VERTEX, exampleGraph);
        labelV1.setOwner(vertex2);
        labelV1.setLabelPosition(LabelPosition.SE);
        vertex1.setLabel(labelV1);
        exampleGraph.addElement(labelV1);

        LabelE labelE1 = (LabelE) this.create(GraphElementType.LABEL_EDGE, exampleGraph);
        labelE1.setOwner(edge1);
        labelE1.setPosition(35);
        edge1.setLabel(labelE1);
        exampleGraph.addElement(labelE1);

        LabelE labelE2 = (LabelE) this.create(GraphElementType.LABEL_EDGE, exampleGraph);
        labelE2.setOwner(edge2);
        edge2.setLabel(labelE2);
        exampleGraph.addElement(labelE2);

        LabelE labelE3 = (LabelE) this.create(GraphElementType.LABEL_EDGE, exampleGraph);
        labelE3.setOwner(edge3);
        labelE3.setPosition(35);
        edge3.setLabel(labelE3);
        exampleGraph.addElement(labelE3);

        Hyperedge hyperedge = (Hyperedge) this.create(GraphElementType.HYPEREDGE, exampleGraph);
        List<Vertex> connectedVertices = new LinkedList<>();
        connectedVertices.add(vertex1);
        connectedVertices.add(vertex2);
        connectedVertices.add(vertex3);
        hyperedge.setConnectedVertices(connectedVertices);
        hyperedge.autoCenterJoint();
        exampleGraph.addElement(hyperedge);

        Boundary boundary1 = (Boundary) this.create(GraphElementType.BOUNDARY, exampleGraph);
        boundary1.setTopLeftX(40);
        boundary1.setTopLeftY(140);
        boundary1.setWidth(440);
        boundary1.setHeight(173);
        exampleGraph.addElement(boundary1);

        Boundary boundary2 = (Boundary) this.create(GraphElementType.BOUNDARY, exampleGraph);
        boundary2.setTopLeftX(40);
        boundary2.setTopLeftY(30);
        boundary2.setWidth(140);
        boundary2.setHeight(60);
        exampleGraph.addElement(boundary2);

        LinkBoundary linkBoundary = (LinkBoundary) this.create(GraphElementType.LINK_BOUNDARY, exampleGraph);
        linkBoundary.setBoundaryA(boundary1);
        linkBoundary.setOutAngle(305);
        linkBoundary.setBoundaryB(boundary2);
        linkBoundary.setInAngle(180);
        exampleGraph.addElement(linkBoundary);

        for (GraphElement graphElement : exampleGraph.getAllElements()) {
            graphElement.setDummy(false);
        }

    }

    @Override
    public Graph createExampleGraph() {

        setTemplateModelsToAllGraphElements(exampleGraph);
        return exampleGraph;
    }


    private void setTemplateModelsToAllGraphElements(Graph graph) {
        PropertyModelFactory propertyModelFactory = this.getPropertyModelFactory();
        for (GraphElement graphElement : graph.getAllElements()) {
            PropertyModel model = propertyModelFactory.createTemplateModel(graphElement.getType());
            graphElement.setModel(model);
        }
    }
}
