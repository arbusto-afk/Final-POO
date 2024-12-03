package backend.model;

import java.awt.*;
public enum Shadow {
    NONE, SIMPLE, COLOR, INVERTED, INVERTEDCOLOR;

    final int shadowOffset = 10;

    public int getOffset() {
        if(this.equals(NONE))
            return 0;
        if(this.equals(SIMPLE) || this.equals(COLOR))
            return this.shadowOffset;
        return -this.shadowOffset;
    }

    public Color getShadowColor(Color c ) {
        if (this.equals(NONE))
            throw new RuntimeException("Attempting to access shade color of non-shaded shade");
        if (this.equals(SIMPLE) || this.equals(INVERTED))
            return Color.GRAY;
        return c.darker();
    }
}
