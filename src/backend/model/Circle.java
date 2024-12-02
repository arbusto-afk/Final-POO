package backend.model;

import javafx.scene.paint.Paint;

public class Circle extends Figure {

    protected final Point centerPoint;
    protected final double radius;

    public Circle(Point centerPoint, double radius, Paint gradient, Shadow shadeType) {
        super(gradient, shadeType);
        this.centerPoint = centerPoint;
        this.radius = radius;
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", centerPoint, radius);
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public double getRadius() {
        return radius;
    }

}
