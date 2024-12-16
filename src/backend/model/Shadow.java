package backend.model;

import java.awt.*;

public enum Shadow {
    NONE("none"),
    SIMPLE("Simple"),
    COLOR("Color"),
    INVERTED("Inverted color"),
    INVERTEDCOLOR("Inverted");

    private final int shadowOffset = 10;
    private final String displayName;

    Shadow(String displayName){
        this.displayName = displayName;
    }
    public String toString() {
        return this.displayName;
    }
    public int getOffset() {
        if(this.equals(NONE))
            return 0;
        if(this.equals(SIMPLE) || this.equals(COLOR))
            return this.shadowOffset;
        return -this.shadowOffset;
    }


}
