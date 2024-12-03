package backend.model;

import javafx.scene.paint.LinearGradient;

import javax.sound.sampled.Line;

public abstract class LinearFigure extends Figure{

    public Point getTopLeft() { return topLeft; }

    public Point getBottomRight() { return bottomRight; }

    protected final Point topLeft, bottomRight;

    public LinearFigure(Point topLeft, Point bottomRight, LinearGradient gradient, Shadow shadeType){

        super(gradient, shadeType);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public LinearGradient getGradient(){ return (LinearGradient) super.getGradient(); }

    public void setGradient(LinearGradient p){ super.setGradient(p);}
}
