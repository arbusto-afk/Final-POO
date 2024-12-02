package backend.model;

import javafx.scene.paint.RadialGradient;

public class Ellipse extends RadialFigure {


    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis, RadialGradient gradient, Shadow shadeType) {
        super(centerPoint, sMayorAxis, sMinorAxis, gradient, shadeType);

    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", this.getCenterPoint(), getsMayorAxis(), getsMinorAxis());
    }

    public double getsMayorAxis() {
        return this.getWidth();
    }

    public double getsMinorAxis() {
        return this.getHeight();
    }

}
