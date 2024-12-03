package backend.model;



import java.awt.Color;
import java.util.List;

public abstract class Figure {
    private List<Color> colors;

    private Shadow shadeType;
    private boolean bevel;

    public boolean getBevel() { return bevel; }

    public void setBevel(boolean bevel) { this.bevel = bevel; }

    public Figure(List<Color> colors, Shadow shadeType){
        this.colors = colors;
        this.shadeType = shadeType;
        this.bevel = false;
    }
    public List<Color> getColors() { return this.colors; }
    public void setColors(List<Color> l) { this.colors = l;}
    public Shadow getShadeType() { return shadeType; }

    public void setShadeType(Shadow shadeType) { this.shadeType = shadeType; }

    public  Figure turnRight(){ return null; };


}

