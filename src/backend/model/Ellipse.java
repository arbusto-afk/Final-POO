package backend.model;

import java.awt.*;
import java.util.List;
public class Ellipse extends RadialFigure {


    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis, List<Color> colors, Shadow shadeType) {
        super(centerPoint, sMayorAxis, sMinorAxis, colors, shadeType);
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
