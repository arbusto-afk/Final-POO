package backend.model;

import java.util.List;
import java.awt.*;
public class Circle extends RadialFigure {


    protected final double radius;

    public Circle(Point centerPoint, double radius, List<Color> colors, Shadow shadeType) {
        super(centerPoint, radius * 2, radius * 2, colors, shadeType);
        this.radius = radius;
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", this.getCenterPoint(), radius);
    }


    public double getRadius() {
        return radius;
    }

    @Override
    public Figure flipH(){
        Point newCenter = getCenterH(getCenterPoint(), getRadius());
        return new Circle(newCenter, getRadius(), getColors(), getShadeType());
    }
    @Override
    public Figure flipV(){
        Point newCenter = getCenterV(getCenterPoint(), getRadius());
        return new Circle(newCenter, getRadius(), getColors(), getShadeType());
    }
    public Figure duplicate(){
        Point newCenter = new Point(getCenterPoint().getX()+DIM,getCenterPoint().getY()+DIM );
        return new Circle(newCenter,getRadius(), getColors(), getShadeType() );
    }

}
