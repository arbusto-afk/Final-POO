package backend.model;

import javafx.scene.paint.Paint;

public abstract class Figure {
    final private Paint gradient;
    private Shadow shadeType;

    public boolean getBiselado() {
        return biselado;
    }

    public void setBiselado(boolean biselado) {
        this.biselado = biselado;
    }

    private boolean biselado;

    public Figure(Paint gradient, Shadow shadeType){
        this.gradient = gradient;
        this.shadeType = shadeType;
        this.biselado = false;
    }
    public Paint getGradient() { return gradient; }

    public Shadow getShadeType() { return shadeType; }

    public void setShadeType(Shadow shadeType) { this.shadeType = shadeType; }


}
