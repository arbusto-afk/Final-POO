package backend.model;

import javafx.scene.paint.Paint;

public class Square extends Figure {

    private final Point topLeft, bottomRight;

    public Square(Point topLeft, double size, Paint gradient, Shadow shadeType) {
        super(gradient, shadeType);
        this.topLeft = topLeft;
        this.bottomRight = new Point(topLeft.x + size, topLeft.y + size);
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public String toString() {
        return String.format("Cuadrado [ %s , %s ]", topLeft, bottomRight);
    }

}
