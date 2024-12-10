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


}
