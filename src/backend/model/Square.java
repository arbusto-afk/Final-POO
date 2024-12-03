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

}
