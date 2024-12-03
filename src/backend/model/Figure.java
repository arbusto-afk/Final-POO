package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;

import java.awt.*;

public abstract class Figure {
    private Paint gradient;
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
    public void setGradient(Paint p) { this.gradient = p;}
    public Shadow getShadeType() { return shadeType; }

    public void setShadeType(Shadow shadeType) { this.shadeType = shadeType; }

    public  Figure turnRight(){ return null; };


}

