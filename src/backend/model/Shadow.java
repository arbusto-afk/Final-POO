package backend.model;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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
    public Color getPaint(Paint p){
        if(this.equals(NONE))
            throw new RuntimeException("Attempting to access shade color of non-shaded shade");
        if(this.equals(SIMPLE) || this.equals(INVERTED))
            return Color.GREY;
        return Color.LIGHTPINK;
    }
}
