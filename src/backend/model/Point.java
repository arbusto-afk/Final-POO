package backend.model;

public class Point {

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point centerPointTo(Point p) {
        return new Point(
                (this.getX() + p.getX()) / 2,
                (this.getY() + p.getY()) / 2
        );
    }
    public Point getDifference(Point p){
        return new Point(
                this.getX() - p.getX(),
                this.getY() - p.getY()
        );
    }
    public Point negate() {
        return new Point(-x, -y);
    }
    public Point invert(){
        return new Point(y, x);
    }


    public Point add(double x, double y) {
        return new Point(this.x + x, this.y + y);
    }
    public Point add(double val) {
        return add(val,val);
    }
    public Point add(Point p) {
        return  this.add(p.x, p.y);
    }

    public Point addX(double x) {
        return this.add(x, 0);
    }
    public Point addY(double y) {
        return this.add(0 , y );
    }

    public Point substract(double x, double y) {
        return this.add(-x, -y);
    }
    public Point substract(Point p){
        return new Point(x - p.x, y - p.y);
    }

    public Point substractX(double x){
        return new Point(this.x - x, y);
    }
    public boolean isInRect(Point p1, Point p2) {
        double minX = Math.min(p1.x, p2.x);
        double maxX = Math.max(p1.x, p2.x);
        double minY = Math.min(p1.y, p2.y);
        double maxY = Math.max(p1.y, p2.y);

        if (this.x >= minX && this.x <= maxX && this.y >= minY && this.y <= maxY) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("{%.2f , %.2f}", x, y);
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof Point))
            return false;
        Point p = (Point)o;
        return this.x == p.getX() && this.y == p .getY();
    }
}