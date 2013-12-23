package pl.edu.agh.gratex.model.labelE;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.properties.LabelHorizontalPlacement;
import pl.edu.agh.gratex.model.properties.LabelTopPlacement;
import pl.edu.agh.gratex.model.PropertyModel;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;


public class LabelE extends GraphElement implements Serializable {
    private static final long serialVersionUID = 7816741486248743237L;

    // Wartości edytowalne przez użytkowanika
    private String text;
    private Font font = Const.DEFAULT_FONT;
    private Color fontColor;
    private int position;                                        // Procent przesunięcia na krawędzie
    private int spacing;                                        // odleglość napisu od krawędzi
    private boolean topPlacement;                                    // Czy etykieta jest nad krawędzią
    private boolean horizontalPlacement;                            // Czy etykieta jest zawsze poziomo (nie = równolegle do krawędzi)

    // Wartości potrzebne do parsowania
    private int posX;                                            // Środek stringa x
    private int posY;                                            // Środek stringa y
    private int angle;                                            // Nachylenie (0 stopni gdy pozioma)

    // Pozostałe
    private Edge owner;
    private Polygon outline;
    private int drawX;
    private int drawY;

    public LabelE(Edge element, Graph graph) {
        super(graph);
        setOwner(element);
        setText("Label");
    }

    @Override
    public void addToGraph() {
        graph.getLabelsE().add(this);
        getOwner().setLabel(this);
        LabelEUtils.updatePosition(this);
        setLatexCode(graph.getGeneralController().getParseController().getLabelEdgeParser().parseToLatex(this));
    }

    @Override
    public void removeFromGraph() {
        graph.getLabelsE().remove(this);
        getOwner().setLabel(null);
    }

    @Override
    public List<? extends GraphElement> getConnectedElements() {
        return new LinkedList<>();
    }

    public void setModel(PropertyModel pm) {
        LabelEdgePropertyModel model = (LabelEdgePropertyModel) pm;

        if (model.text != null) {
            setText(model.text);
        }

        if (model.fontColor != null) {
            setFontColor(new Color(model.fontColor.getRGB()));
        }

        if (model.position > -1) {
            setPosition(model.position);
        }

        if (model.spacing > -1) {
            setSpacing(model.spacing);
        }

        if (model.topPlacement > -1) {
            setTopPlacement((model.topPlacement == 1));
        }

        if (model.horizontalPlacement > -1) {
            setHorizontalPlacement((model.horizontalPlacement == 1));
        }
    }

    public PropertyModel getModel() {
        LabelEdgePropertyModel result = new LabelEdgePropertyModel();

        result.text = new String(getText());
        result.fontColor = new Color(getFontColor().getRGB());
        result.position = getPosition();
        result.spacing = getSpacing();
        result.topPlacement = 0;
        result.isLoop = PropertyModel.NO;
        if (getOwner().getVertexA() == getOwner().getVertexB()) {
            result.isLoop = PropertyModel.YES;
        }
        if (isTopPlacement()) {
            result.topPlacement = 1;
        }
        result.horizontalPlacement = 0;
        if (isHorizontalPlacement()) {
            result.horizontalPlacement = 1;
        }

        return result;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.LABEL_EDGE;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    public void draw(Graphics2D g2d, boolean dummy) {
        LabelEUtils.draw(this, g2d, dummy);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public boolean isTopPlacement() {
        return topPlacement;
    }

    public void setTopPlacement(boolean topPlacement) {
        this.topPlacement = topPlacement;
    }

    public void setTopPlacement(LabelTopPlacement topPlacement) {
        this.topPlacement = (topPlacement == LabelTopPlacement.ABOVE);
    }

    public LabelTopPlacement getTopPlacement() {
        return (topPlacement? LabelTopPlacement.ABOVE: LabelTopPlacement.BELOW);
    }

    public boolean isHorizontalPlacement() {
        return horizontalPlacement;
    }

    public void setHorizontalPlacement(boolean horizontalPlacement) {
        this.horizontalPlacement = horizontalPlacement;
    }

    public void setHorizontalPlacement(LabelHorizontalPlacement horizontalPlacement) {
        this.horizontalPlacement = (horizontalPlacement == LabelHorizontalPlacement.LEVEL);
    }

    public LabelHorizontalPlacement getHorizontalPlacement() {
        return (horizontalPlacement? LabelHorizontalPlacement.LEVEL: LabelHorizontalPlacement.TANGENT);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public Edge getOwner() {
        return owner;
    }

    public void setOwner(Edge owner) {
        this.owner = owner;
    }

    public Polygon getOutline() {
        return outline;
    }

    public void setOutline(Polygon outline) {
        this.outline = outline;
    }

    public int getDrawX() {
        return drawX;
    }

    public void setDrawX(int drawX) {
        this.drawX = drawX;
    }

    public int getDrawY() {
        return drawY;
    }

    public void setDrawY(int drawY) {
        this.drawY = drawY;
    }
}
