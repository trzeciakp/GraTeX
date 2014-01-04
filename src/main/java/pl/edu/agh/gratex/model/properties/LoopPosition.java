package pl.edu.agh.gratex.model.properties;

import pl.edu.agh.gratex.model.PropertyModel;

/**
 *
 */
public enum LoopPosition implements Emptible {
    EMPTY(" ", PropertyModel.EMPTY) {

        @Override
        public boolean isEmpty() {
            return false;
        }
    }, P25("25 %", 25), P50("50 %", 50), P75("75 %", 75);

    @Override
    public boolean isEmpty() {
        return false;
    }

    private String text;
    private int value;

    private LoopPosition(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String toString() {
        return text;
    }

    public int getValue() {
        return value;
    }

    public static LoopPosition getByValue(int position) {
        if(position < 0) {
            return EMPTY;
        }
        if(position < 38 ) {
            return P25;
        } else if (position < 68) {
            return P50;
        } else {
            return P75;
        }
    }
}
