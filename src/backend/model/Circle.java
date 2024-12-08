package backend.model;

import java.util.List;
import java.awt.*;
public class Circle extends Ellipse {

    public Circle(Point centerPoint, double radius, Shadow shadeType, boolean hasBevel) {
        super(centerPoint, radius * 2, radius * 2, shadeType, hasBevel);
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", this.getCenterPoint(), getWidth() / 2);
    }
    @Override
    public boolean pointBelongs(Point p){
        return  Math.sqrt(Math.pow(this.getCenterPoint().getX() - p.getX(), 2) +
                Math.pow(this.getCenterPoint().getY() - p.getY(), 2)) < this.getRadius();
    }

    public double getRadius() {
        return getWidth() / 2;
    }

    public Circle duplicate(){
        return new Circle(getCenterPoint().add(getDuplicateOffset()),getRadius(), getShadeType(), getHasBevel() );
    }


}

