package pl.edu.agh.gratex.parser.elements.labeledge;

import pl.edu.agh.gratex.model.GraphElement;
import pl.edu.agh.gratex.model.labelE.LabelE;
import pl.edu.agh.gratex.parser.elements.ParseElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LabelEdgeRotationParser extends ParseElement {
    private static final String REGEX = "\\[rotate=(\\d+)\\] ";
    public static final String ROTATE_FORMAT = "[rotate=%s] ";
    public static final Pattern PATTERN = Pattern.compile(REGEX);
    public static final int ROTATION_GROUP = 1;

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public int groups() {
        return super.groups()+1;
    }

    @Override
    public String regex() {
        return REGEX;
    }

    @Override
    public void setProperty(String match, GraphElement element) {
        Matcher matcher = PATTERN.matcher(match);
        if(!matcher.matches()) {
            //TODO
        }
        LabelE labelE = (LabelE) element;
        //TODO
        int angle = Integer.parseInt(matcher.group(ROTATION_GROUP));
        labelE.setAngle(angle);
    }

    @Override
    public String getProperty(GraphElement element) {
        LabelE labelE = (LabelE) element;
        String result = String.format(ROTATE_FORMAT, Integer.valueOf(labelE.getAngle()));
        return result;
    }
}
