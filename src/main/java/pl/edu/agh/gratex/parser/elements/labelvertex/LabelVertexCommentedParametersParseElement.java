package pl.edu.agh.gratex.parser.elements.labelvertex;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.labelV.LabelV;
import pl.edu.agh.gratex.model.properties.LabelPosition;
import pl.edu.agh.gratex.model.vertex.Vertex;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LabelVertexCommentedParametersParseElement extends ParseElement {
    private final static String SEPARATED_PARAMETER_REGEX = "([^,]+),";
    private static final String RETURN_FORMAT = "%%%d,%s,%d,";
    private final static String REGEX = "%" +
            SEPARATED_PARAMETER_REGEX + //OWNER NUMBER INT
            SEPARATED_PARAMETER_REGEX + //POSITION LabelPosition Enum
            SEPARATED_PARAMETER_REGEX; //SPACING NUMBER INT
    private static final int GROUPS = 3;
    private final static Pattern PATTERN = Pattern.compile(REGEX);
    public static final int OWNER_GROUP = 1;
    public static final int POSITION_GROUP = 2;
    public static final int SPACING_GROUP = 3;


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
        LabelV labelV = (LabelV) element;
        Matcher matcher = PATTERN.matcher(match);
        if(!matcher.matches()) {
            //TODO
        }
        Vertex owner = element.getGraph().getVertexById(Integer.parseInt(matcher.group(OWNER_GROUP)));
        LabelPosition labelPosition = LabelPosition.valueOf(LabelPosition.class, matcher.group(POSITION_GROUP));
        int spacing = Integer.parseInt(matcher.group(SPACING_GROUP));
        labelV.setOwner(owner);
        labelV.setSpacing(spacing);
        labelV.setLabelPosition(labelPosition);
    }

    @Override
    public String getProperty(GraphElement element) {
        LabelV labelV = (LabelV) element;
        int ownerNumber = labelV.getOwner().getNumber();
        LabelPosition labelPosition = labelV.getLabelPosition();
        int spacing = labelV.getSpacing();
        String result = String.format(RETURN_FORMAT, ownerNumber, labelPosition.name(), spacing);
        return result;
    }
}
