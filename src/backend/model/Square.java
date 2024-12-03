package backend.model;

import java.util.List;
import java.awt.*;
public class Square extends Rectangle {

    public Square(Point topLeft, double size, List<Color> colors, Shadow shadeType) {
        super(topLeft, new Point(topLeft.x + size, topLeft.y + size), colors, shadeType);
    }
    @Override
    public String toString() {
        return String.format("Cuadrado [ %s , %s ]", super.getTopLeft(), super.getBottomRight());
    }
    public Figure flipH(){
        double width = this.getBottomRight().getX()-  this.getTopLeft().getX();
        Point newTopLeft = new Point(getTopLeft().getX() + width, getTopLeft().getY());

        return new Square(newTopLeft, width , getColors(), getShadeType());
    }
    public Figure flipV(){
        double height = this.getBottomRight().getY() - this.getTopLeft().getY();

        Point newTopLeft = new Point(getTopLeft().getX(), getTopLeft().getY()+height);
        return new Square(newTopLeft, height, getColors(), getShadeType());
    }

}
