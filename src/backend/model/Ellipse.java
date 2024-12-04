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
    @Override
    public boolean pointBelongs(Point p){
        return ((Math.pow(p.getX() - this.getCenterPoint().getX(), 2) / Math.pow(this.getsMayorAxis(), 2)) +
                (Math.pow(p.getY() - this.getCenterPoint().getY(), 2) / Math.pow(this.getsMinorAxis(), 2))) <= 0.30;

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
        return new Ellipse(newCenter, this.getsMayorAxis(), this.getsMinorAxis(),getColors(), getShadeType());
    }
    public Figure duplicate(){
        Point newCenter = new Point(getCenterPoint().getX()+DIM,getCenterPoint().getY()+DIM );
        return new Ellipse(newCenter, this.getsMayorAxis(),this.getsMinorAxis(), getColors(), getShadeType() );
    }

}
