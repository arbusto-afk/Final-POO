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

    public Point centerPointTo(Point p){
        return new Point(
                this.getX() + ((this.getX() - p.getX()) / 2),
                this.getY() + ((this.getY() - p.getY()) / 2)
        );
    }
    public Point getDifference(Point p){
        return new Point(
                this.getX() - p.getX(),
                this.getY() - p.getY()
        );
    }

    public Point add(Point p) {
        return new Point(
                this.x + p.getX(),
                this.y + p.getY()
        );
    }

        @Override
    public String toString() {
        return String.format("{%.2f , %.2f}", x, y);
    }

}
