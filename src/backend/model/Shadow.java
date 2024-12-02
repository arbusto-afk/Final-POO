package backend.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.LinearGradient;

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
    private Color getShadowColor(Paint p ) {
        if (this.equals(NONE))
            throw new RuntimeException("Attempting to access shade color of non-shaded shade");
        if (this.equals(SIMPLE) || this.equals(INVERTED))
            return Color.GREY;
        return getLastGradientColor(p);
    }
    private Color getLastGradientColor(Paint p){
        if(p instanceof RadialGradient)
            return ((RadialGradient) p).getStops().getLast().getColor();
        if(p instanceof LinearGradient)
            return ((LinearGradient) p).getStops().getLast().getColor();
        throw new RuntimeException("Paint is neither a LinearGradient nor RadialGradient");
    }
    public Color getShadowColor(RadialGradient p){
        return getShadowColor((Paint) p);
    }
    public Color getShadowColor(LinearGradient p){
        return getShadowColor((Paint) p);
    }
}
