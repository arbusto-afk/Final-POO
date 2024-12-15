package backend.model;


import backend.Pair;

public abstract class Figure {

    private Point centerPoint, topLeft, bottomRight;
    private double width, height;

    private Shadow shadeType;
    private boolean hasBevel;

    public Figure(Shadow shadeType, Point centerPoint, Double width, Double height,boolean hasBevel){
        this.shadeType = shadeType;
        this.centerPoint = centerPoint;
        this.hasBevel = hasBevel;
        this.width = width;
        this.height = height;
        setCorners();
    }
    public Point getCenterPoint() { return centerPoint; }
    public Point getTopLeft(){
        return topLeft;
    }
    public Point getBottomRight(){
        return bottomRight;
    }
    public double getHeight() {
        return height;
    }
    public double getWidth() {
        return width;
    }

    private void setCorners(){
        Point cornerOffset = new Point(height / 2, width / 2);
        this.bottomRight = centerPoint.add(cornerOffset);
        this.topLeft = centerPoint.substract(cornerOffset);
    }
    public void resize(double width, double height){
        this.width = width;
        this.height = height;
        setCorners();
    }
    public Figure move(Point newCenter){
        this.centerPoint = newCenter;
        setCorners();
        return this;
    }

    public boolean getHasBevel() { return hasBevel; }
    public void setHasBevel(boolean hasBevel) { this.hasBevel = hasBevel; }

    public Shadow getShadeType() { return shadeType; }
    public void setShadeType(Shadow shadeType) { this.shadeType = shadeType; }


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
    public abstract Figure duplicate(Integer offSet);

    /*public Pair<Figure, Figure> divide(){

    }*/
}

