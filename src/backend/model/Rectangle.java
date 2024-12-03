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

        double height = bottomRight.getY() - topLeft.getY();
        double width = bottomRight.getX() - topLeft.getX();

        Point centroide = getCentroide(height, width);

        topLeft.x = centroide.getX() - (height/2);
        topLeft.y = centroide.getY() - (width/2) ;

        bottomRight.x = centroide.getX() +(height/2);
        bottomRight.y = centroide.getY()+ (width/2) ;
        return new Rectangle( topLeft,  bottomRight, this.getGradient(), this.getShadeType());
    }

    private Point getCentroide(double height, double width) {
        double pointX = topLeft.getX() + (width / 2);
        double pointY = topLeft.getY() + (height / 2);
        return new Point(pointX, pointY);
    }
}
