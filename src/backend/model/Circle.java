package backend.model;

import javafx.scene.paint.RadialGradient;

public class Circle extends RadialFigure {

    protected final double radius;

    public Circle(Point centerPoint, double radius, RadialGradient gradient, Shadow shadeType) {
        super(centerPoint, radius * 2, radius * 2, gradient, shadeType);
        this.radius = radius;
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", this.getCenterPoint(), radius);
    }


    public double getRadius() {
        return radius;
    }

}
