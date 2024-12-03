package backend.model;

import java.awt.*;
import java.util.List;

public abstract class LinearFigure extends Figure{

    private final Point topLeft, bottomRight;

    public LinearFigure(Point topLeft, Point bottomRight, List<Color> colors, Shadow shadeType){
        super(colors, shadeType);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }
    public Point getTopLeft() { return topLeft; }
    public Point getBottomRight() { return bottomRight; }
}
