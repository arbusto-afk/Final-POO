package backend.model;

import javafx.scene.paint.Paint;

public abstract class Figure {
    final private Paint gradient;
    private Shadow shadeType;
    private boolean bevel;

    public boolean getBevel() { return bevel; }

    public void setBevel(boolean bevel) { this.bevel = bevel; }


    public Figure(Paint gradient, Shadow shadeType){
        this.gradient = gradient;
        this.shadeType = shadeType;
        this.bevel = false;
    }
    public Paint getGradient() { return gradient; }
    public Shadow getShadeType() { return shadeType; }

    public void setShadeType(Shadow shadeType) { this.shadeType = shadeType; }


}
