package pl.edu.agh.gratex.graph;


import pl.edu.agh.gratex.constants.Constants;
import pl.edu.agh.gratex.constants.GraphElementType;
import pl.edu.agh.gratex.graph.utils.DrawingTools;
import pl.edu.agh.gratex.graph.utils.Geometry;
import pl.edu.agh.gratex.gui.ControlManager;
import pl.edu.agh.gratex.model.LabelVertexPropertyModel;
import pl.edu.agh.gratex.model.PropertyModel;

import java.awt.*;
import java.io.Serializable;


public class LabelV extends GraphElement implements Serializable {
    private static final long serialVersionUID = 5054682946344977073L;

    // Wartości edytowalne przez użytkowanika
    private String text;
    private Font font = new Font("Cambria", Font.PLAIN, 16);
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

    public boolean intersects(int x, int y) {
        return getOutline().contains(x, y);
    }

    public void updatePosition(Graphics2D g) {
        g.setFont(getFont());
        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(getText());
        int height = fm.getAscent();
        int descent = fm.getDescent();

        Point exitPoint = Geometry.calculateEdgeExitPoint(getOwner(), (450 - 45 * getPosition()) % 360);

        double dposX = 0.0;
        double dposY = 0.0;

        switch (getPosition()) {
            case 0: {
                dposX = exitPoint.x;
                dposY = exitPoint.y - getSpacing() - height / 2;
                break;
            }
            case 1: {
                dposX = exitPoint.x + getSpacing() / 1.4142 + width / 2;
                dposY = exitPoint.y - getSpacing() / 1.4142 - height / 2;
                break;
            }
            case 2: {
                dposX = exitPoint.x + getSpacing() + width / 2;
                dposY = exitPoint.y;
                break;
            }
            case 3: {
                dposX = exitPoint.x + getSpacing() / 1.4142 + width / 2;
                dposY = exitPoint.y + getSpacing() / 1.4142 + height / 2;
                break;
            }
            case 4: {
                dposX = exitPoint.x;
                dposY = exitPoint.y + getSpacing() + height / 2 + 0.5;
                break;
            }
            case 5: {
                dposX = exitPoint.x - getSpacing() / 1.4142 - width / 2;
                dposY = exitPoint.y + getSpacing() / 1.4142 + height / 2;
                break;
            }
            case 6: {
                dposX = exitPoint.x - getSpacing() - width / 2;
                dposY = exitPoint.y;
                break;
            }
            case 7: {
                dposX = exitPoint.x - getSpacing() / 1.4142 - width / 2;
                dposY = exitPoint.y - getSpacing() / 1.4142 - height / 2;
                break;
            }

        }
        setPosX((int) dposX);
        setPosY((int) dposY);
        setDrawX((int) (dposX - width / 2));
        setDrawY((int) (dposY - descent + height / 2));
        setOutline(new Rectangle(getPosX() - width / 2, getPosY() - height / 2, width, height));
    }

    public void draw(Graphics2D g2d, boolean dummy) {
        Graphics2D g = (Graphics2D) g2d.create();

        updatePosition(g);

        if (ControlManager.selection.contains(this)) {
            g.setColor(Constants.SELECTION_COLOR);
            g.fillRect(getOutline().x, getOutline().y, getOutline().width, getOutline().height);
        }

        g.setFont(getFont());
        g.setColor(getFontColor());
        if (dummy) {
            g.setColor(DrawingTools.getDummyColor(getFontColor()));
        }
        g.drawString(getText(), getDrawX(), getDrawY());

        g.dispose();
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
