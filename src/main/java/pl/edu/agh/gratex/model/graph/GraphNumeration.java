package pl.edu.agh.gratex.model.graph;

import pl.edu.agh.gratex.constants.Const;

public class GraphNumeration {
    public static final char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private boolean[] usedNumber;
    private int startingNumber;
    private boolean digitalNumeration;

    public GraphNumeration() {
        startingNumber = 1;
        digitalNumeration = true;
        usedNumber = new boolean[Const.MAX_VERTEX_NUMBER];
        for (int i = 0; i < Const.MAX_VERTEX_NUMBER; i++) {
            usedNumber[i] = false;
        }
    }

    public int getNextFreeNumber() {
        for (int i = startingNumber; i < Const.MAX_VERTEX_NUMBER; i++) {
            if (!usedNumber[i]) {
                return i;
            }
        }
        startingNumber = 1;
        return 1;
    }


    public static String digitalToAlphabetical(int number) {
        StringBuffer buffer = new StringBuffer();
        while (number > 0) {
            buffer.insert(0, letters[(number - 1) % 26]);
            number = (number - 1) / 26;
        }
        return buffer.toString();
    }

    //===================================================
    // Setters and getters
    public boolean isUsed(int index) {
        return usedNumber[index];
    }

    public void setUsed(int index, boolean flag) {
        if(index > -1) {
            usedNumber[index] = flag;
        }
    }

    public boolean isNumerationDigital() {
        return digitalNumeration;
    }

    public void setNumerationDigital(boolean flag) {
        this.digitalNumeration = flag;
    }

    public int getStartingNumber() {
        return startingNumber;
    }

    public void setStartingNumber(int startingNumber) {
        this.startingNumber = startingNumber;
    }
}
