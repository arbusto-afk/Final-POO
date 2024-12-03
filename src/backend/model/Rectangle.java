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

        double height = this.getBottomRight().getY() - this.getTopLeft().getY();
        double width = this.getBottomRight().getX()-  this.getTopLeft().getX();

        Point centroide = getCentroide(height, width);


        double topLeftX = centroide.getX() - (height/2);
        double topLeftY = centroide.getY() - (width/2) ;
        Point topleft = new Point(topLeftX, topLeftY);

        double bottomRightX = centroide.getX() +(height/2);
        double bottomRightY = centroide.getY()+ (width/2) ;
        Point bottomright = new Point(bottomRightX, bottomRightY);


        return new Rectangle( topleft,  bottomright, this.getColors(), this.getShadeType());
    }

    private Point getCentroide(double height, double width) {
        double pointX = getTopLeft().getX() + (width / 2);
        double pointY = getTopLeft().getY()+ (height / 2);
        return new Point(pointX, pointY);
    }

    public Figure flipH(){
        double width = this.getBottomRight().getX()-  this.getTopLeft().getX();
        Point newTopLeft = new Point(getTopLeft().getX() + width, getTopLeft().getY());
        Point newBottomRight =  new Point(getBottomRight().getX() + width , getBottomRight().getY());
        return new Rectangle(newTopLeft, newBottomRight , getColors(), getShadeType());
    }
    public Figure flipV(){
        double height = this.getBottomRight().getY() - this.getTopLeft().getY();
        Point newBottomRight =  new Point(getBottomRight().getX(), getBottomRight().getY()+height );
        Point newTopLeft = new Point(getTopLeft().getX(), getTopLeft().getY()+height);
        return new Rectangle(newTopLeft, newBottomRight , getColors(), getShadeType());
    }
    public Figure duplicate(){
        Point newBottomRight =  new Point(getBottomRight().getX()+DIM, getBottomRight().getY()+DIM );
        Point newTopLeft = new Point(getTopLeft().getX()+DIM, getTopLeft().getY()+DIM);
        return new Rectangle(newTopLeft, newBottomRight , getColors(), getShadeType());
    }

}
