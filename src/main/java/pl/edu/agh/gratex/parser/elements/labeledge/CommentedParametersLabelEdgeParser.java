package pl.edu.agh.gratex.parser.elements.labeledge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.edge.Edge;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.model.properties.LabelHorizontalPlacement;
import pl.edu.agh.gratex.model.properties.LabelTopPlacement;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class CommentedParametersLabelEdgeParser extends ParseElement {
    private final static String SEPARATED_PARAMETER_REGEX = "([^,]+),";
    private static final String RETURN_FORMAT = "%%%d,%s,%d,%s,%d,";
    private final static String REGEX = "%" +
            SEPARATED_PARAMETER_REGEX + //OWNER NUMBER INT
            SEPARATED_PARAMETER_REGEX + //BELOW or ABOVE  topPlacement
            SEPARATED_PARAMETER_REGEX + //POSITION NUMBER (0-100)
            SEPARATED_PARAMETER_REGEX + //TANGENT or LEVEL     horizontalPlacement
            SEPARATED_PARAMETER_REGEX; //SPACING NUMBER INT
    private static final int GROUPS = 5;
    private final static Pattern PATTERN = Pattern.compile(REGEX);
    public static final int OWNER_GROUP = 1;
    public static final int TOP_PLACEMENT_GROUP = 2;
    public static final int POSITION_GROUP = 3;
    public static final int HORIZONTAL_PLACEMENT_GROUP = 4;
    public static final int SPACING_GROUP = 5;


    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public String regex() {
        return REGEX;
    }

    public int groups() {
        return super.groups() + GROUPS;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        LabelE labelE = (LabelE) element;
        Matcher matcher = PATTERN.matcher(match);
        if(!matcher.matches()) {
            //TODO
        }
        Edge owner = element.getGraph().getEdgeById(Integer.parseInt(matcher.group(OWNER_GROUP)));
        LabelTopPlacement labelTopPlacement = LabelTopPlacement.valueOf(matcher.group(TOP_PLACEMENT_GROUP));
        int position = Integer.parseInt(matcher.group(POSITION_GROUP));
        LabelHorizontalPlacement labelHorizontalPlacement = LabelHorizontalPlacement.valueOf(matcher.group(HORIZONTAL_PLACEMENT_GROUP));
        int spacing = Integer.parseInt(matcher.group(SPACING_GROUP));
        labelE.setOwner(owner);
        labelE.setTopPlacement(labelTopPlacement);
        labelE.setSpacing(spacing);
        labelE.setHorizontalPlacement(labelHorizontalPlacement);
        labelE.setPosition(position);
    }

    @Override
    public String getProperty(GraphElement element) {
        LabelE labelE = (LabelE) element;
        int ownerNumber = labelE.getOwner().getNumber();
        LabelTopPlacement labelTopPlacement = labelE.getTopPlacement();
        int position = labelE.getPosition();
        LabelHorizontalPlacement labelHorizontalPlacement = labelE.getHorizontalPlacement();
        int spacing = labelE.getSpacing();
        String result = String.format(RETURN_FORMAT, ownerNumber, labelTopPlacement.name(),position,labelHorizontalPlacement.name(), spacing);
        return result;
    }
}
