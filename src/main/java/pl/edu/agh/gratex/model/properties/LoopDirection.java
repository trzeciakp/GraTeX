package pl.edu.agh.gratex.model.properties;

import pl.edu.agh.gratex.model.PropertyModel;

/**
*
*/
public enum LoopDirection implements Emptible {
    EMPTY(PropertyModel.EMPTY) {
        @Override
        public String toString() {
            return " ";
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    },RIGHT(0), ABOVE(90), LEFT(180), BELOW(270);
    private int angle;

    LoopDirection(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public static LoopDirection getByAngle(int angle) {
        if(angle == PropertyModel.EMPTY) {
            return EMPTY;
        }
        angle = (((angle + 45) % 360) / 90) * 90;

        for (LoopDirection direction : values()) {
            if (angle == direction.getAngle()) {
                return direction;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
