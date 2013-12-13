package pl.edu.agh.gratex.model.labelV;


import pl.edu.agh.gratex.constants.Const;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.PropertyModel;
import pl.edu.agh.gratex.model.graph.Graph;
import pl.edu.agh.gratex.model.properties.LabelPosition;
import pl.edu.agh.gratex.model.vertex.Vertex;

import java.awt.*;
import java.io.Serializable;


public class LabelV extends GraphElement implements Serializable {
    private static final long serialVersionUID = 5054682946344977073L;

    // Wartości edytowalne przez użytkowanika
    private String text;
    private Font font = Const.DEFAULT_FONT;
    private Color fontColor;
    private int position;                                        // 0-N; 1-NE; 2-E; ...; 7 - NW;
    private int spacing;                                        // odleglość napisu od obrysu wierzchołka

    // Wartości potrzebne do parsowania
    private int posX;                                            // \ Środek stringa
    private int posY;                                            // / Środek stringa

    // Pozostałe
    private Vertex owner;
    private int drawX;
    private int drawY;
    private Rectangle outline;
    private Graph graph;

    public LabelV(Vertex element, Graph graph) {
        this.graph = graph;
        setOwner(element);
        setText("Label");
    }

    public LabelV getCopy(Vertex owner) {
        LabelV result = new LabelV(owner, graph);
        result.setModel(getModel());

        return result;
    }

    public void setModel(PropertyModel pm) {
        LabelVertexPropertyModel model = (LabelVertexPropertyModel) pm;

        if (model.text != null) {
            setText(new String(model.text));
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
    }

    public PropertyModel getModel() {
        LabelVertexPropertyModel result = new LabelVertexPropertyModel();

        result.text = new String(getText());
        result.fontColor = new Color(getFontColor().getRGB());
        result.position = getPosition();
        result.spacing = getSpacing();

        return result;
    }

    @Override
    public GraphElementType getType() {
        return GraphElementType.LABEL_VERTEX;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    @Override
    public void draw(Graphics2D g, boolean dummy) {
        LabelVUtils.draw(this, g, dummy);
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

    public LabelPosition getLabelPosition() {
        return LabelPosition.values()[position+1];
    }

    public void setLabelPosition(LabelPosition labelPosition) {
        this.position = labelPosition.getValue();
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

    public Vertex getOwner() {
        return owner;
    }

    public void setOwner(Vertex owner) {
        this.owner = owner;
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

    public Rectangle getOutline() {
        return outline;
    }

    public void setOutline(Rectangle outline) {
        this.outline = outline;
    }
}
