package backend.model;

public class Point {

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Point(double xy){
        this.x = xy;
        this.y = xy;
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

    public Point add(Point p) {
        return new Point(
                this.x + p.getX(),
                this.y + p.getY()
        );
    }
    public Point add(double val) {
        return new Point(this.x + val, this.y + val);
    }
    public Point add(double x, double y) {
        return new Point(this.x + x, this.y + y);
    }
    public Point addX(double x) {
        return new Point(this.x + x, this.y );
    }
    public Point addY(double y) {
        return new Point(this.x , this.y+y );
    }

    public Point substract(Point p){
        return new Point(x - p.x, y - p.y);
    }

    public Point substractX(double x){
        return new Point(this.x - x, y);
    }

    public Point substractY(double y){
        return new Point(x , this.y - y);
    }
    public Point substract(double x, double y){
        return new Point(this.x - x, this.y - y);
    }
        @Override
    public String toString() {
        return String.format("{%.2f , %.2f}", x, y);
    }

}
