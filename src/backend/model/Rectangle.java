package backend.model;

import java.util.List;
import java.awt.*;
public class Rectangle extends LinearFigure {

    public Rectangle(Point topLeft, Point bottomRight, List<Color> colors, Shadow shadeType) {
        super(topLeft, bottomRight, colors, shadeType);
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", this.getTopLeft(), this.getBottomRight());
    }
    public Figure turnRight() {

        double height = getBottomRight().getY() - getTopLeft().getY();
        double width = getBottomRight().getX() - getTopLeft().getX();

        Point centroide = getCentroide(height, width);

        getTopLeft().x = centroide.getX() - (height/2);
        getTopLeft().y = centroide.getY() - (width/2) ;

        getBottomRight().x = centroide.getX() +(height/2);
        getBottomRight().y = centroide.getY()+ (width/2) ;
        return new Rectangle( getTopLeft(), getTopLeft(), this.getColors(), this.getShadeType());
    }

    private Point getCentroide(double height, double width) {
        double pointX = getTopLeft().getX() + (width / 2);
        double pointY = getTopLeft().getY() + (height / 2);
        return new Point(pointX, pointY);
    }
}
