package backend.model;



import java.awt.Color;
import java.util.List;

public abstract class Figure {
    private List<Color> colors;
    protected static final int DIM = 3;

    private Point centerPoint;
    private Shadow shadeType;
    private boolean hasBevel;

    public Figure(List<Color> colors, Shadow shadeType, Point p){
        this.colors = colors;
        this.shadeType = shadeType;
        this.centerPoint = p;
        this.hasBevel = false;
    }
    public Point getCenterPoint() { return centerPoint; }

    public List<Color> getColors() { return this.colors; }
    public void setColors(List<Color> l) { this.colors = l;}

    public boolean getHasBevel() { return hasBevel; }
    public void setHasBevel(boolean hasBevel) { this.hasBevel = hasBevel; }

    public Shadow getShadeType() { return shadeType; }
    public void setShadeType(Shadow shadeType) { this.shadeType = shadeType; }

    public void move(Point newCenter){
        this.centerPoint = newCenter;
      //  System.out.println("New center: " + this.centerPoint + "with newCenter: " + newCenter);
    }
    /*
        Returns whether a point belongs to the figure or not
     */
    public abstract boolean pointBelongs(Point p);

    public abstract Figure turnRight();
    public abstract Figure flipH();
    public abstract Figure flipV();
    public abstract Figure duplicate();



}

