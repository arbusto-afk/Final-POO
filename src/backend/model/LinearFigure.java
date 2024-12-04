package backend.model;

import java.awt.*;
import java.util.List;

public abstract class LinearFigure extends Figure{

    private Point topLeft, bottomRight;

    public LinearFigure(Point topLeft, Point bottomRight, List<Color> colors, Shadow shadeType){
        super(colors, shadeType, topLeft.centerPointTo(bottomRight));
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }
    public Point getTopLeft() { return topLeft; }
    public Point getBottomRight() { return bottomRight; }

    @Override
    public void move(Point newCenter){
        Point topLeftOffset = getTopLeft().getDifference(getCenterPoint());
        Point botRightOffset = getBottomRight().getDifference(getCenterPoint());
        super.move(newCenter);
        this.topLeft = newCenter.add(topLeftOffset);
        this.bottomRight = newCenter.add(botRightOffset);
    }
}
