package backend.model;

import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;

public class Square extends Rectangle {

    public Square(Point topLeft, double size, LinearGradient gradient, Shadow shadeType) {
        super(topLeft, new Point(topLeft.x + size, topLeft.y + size), gradient, shadeType);
    }
    @Override
    public String toString() {
        return String.format("Cuadrado [ %s , %s ]", super.getTopLeft(), super.getBottomRight());
    }

}
