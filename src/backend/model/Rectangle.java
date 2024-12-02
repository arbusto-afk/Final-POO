package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;

public class Rectangle extends LinearFigure {

    public Rectangle(Point topLeft, Point bottomRight, LinearGradient gradient, Shadow shadeType) {
        super(topLeft, bottomRight, gradient, shadeType);
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", this.getTopLeft(), this.getBottomRight());
    }
}
