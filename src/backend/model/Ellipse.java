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

    @Override
    public Figure turnRight() {
        double currentMajorAxis = this.getsMayorAxis();
        double currentMinorAxis = this.getsMinorAxis();

        return new Ellipse(this.getCenterPoint(), this.getsMinorAxis(), this.getsMayorAxis(), this.getColors(), this.getShadeType());
    }
    @Override
    public Figure flipH(){
        Point newCenter = getCenterH(getCenterPoint(), this.getsMayorAxis()/2 );
        return new Ellipse(newCenter, this.getsMayorAxis() ,this.getsMinorAxis(), this.getColors(), getShadeType());
    }
    public Figure flipV(){
        Point newCenter = getCenterV(getCenterPoint(), this.getsMayorAxis()/2 );
        return new Circle(newCenter, this.getsMayorAxis(), getColors(), getShadeType());
    }

}
