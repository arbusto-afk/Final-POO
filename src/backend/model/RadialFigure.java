package backend.model;

import java.awt.*;
import java.util.List;

public abstract class RadialFigure extends Figure{

    private final double width, height;

    public RadialFigure(Point centerPoint, double width, double height, List<Color> colors, Shadow shadeType) {
        super(colors, shadeType, centerPoint);
                 this.width = width;
        this.height = height;
    }
    public double getHeight() { return height; }
    public double getWidth() { return width; }

    protected Point getCenterH(Point center, double dimension ){
        double newCenterX = center.getX() + 2*(dimension);
        Point newCenter = new Point(newCenterX, center.getY());
        return newCenter;
    }
    protected Point getCenterV(Point center, double dimension ){
        double newCenterY = center.getY() + 2*(dimension);
        Point newCenter = new Point(center.getX(),  newCenterY);
        return newCenter;
    }

}
