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
        Point topLeftOffset = getCenterPoint().getDifference(getTopLeft());
        Point botRightOffset = getCenterPoint().getDifference(getBottomRight());
        super.move(newCenter);
        //System.out.println("Moving by diff " + diff);

        this.topLeft = newCenter.add(topLeftOffset);
        this.bottomRight = newCenter.add(botRightOffset);
    }
}
