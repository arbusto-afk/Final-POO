package backend.model;


import java.util.List;

public abstract class Figure {
    //private List<Color> colors;

    private static final int DUPLICATEOFFSET = 15;

    private Point centerPoint;
    private double width, height;

    private Shadow shadeType;
    private boolean hasBevel;

    public Figure(Shadow shadeType, Point centerPoint, Double width, Double height,boolean hasBevel){
        this.shadeType = shadeType;
        this.centerPoint = centerPoint;
        this.hasBevel = hasBevel;
        this.width = width;
        this.height = height;
    }
    public Point getCenterPoint() { return centerPoint; }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void resize(double width, double height){
        this.width = width;
        this.height = height;
    }

    public boolean getHasBevel() { return hasBevel; }
    public void setHasBevel(boolean hasBevel) { this.hasBevel = hasBevel; }

    public Shadow getShadeType() { return shadeType; }
    public void setShadeType(Shadow shadeType) { this.shadeType = shadeType; }

    public double getDuplicateOffset() { return DUPLICATEOFFSET; }

    public void move(Point newCenter){
        this.centerPoint = newCenter;
    }

    /*
        Returns whether a point belongs to the figure or not
     */
    public abstract boolean pointBelongs(Point p);

    public abstract void turnRight();

    public void flipHorizontal(){
        move(getCenterPoint().addX(width));
    }
    public void flipVertical(){
         move(getCenterPoint().addY(height));
    }

  //  public abstract List<Figure> divide();
    public abstract Figure duplicate();
}

