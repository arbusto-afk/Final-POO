package backend.model;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;

public abstract class RadialFigure extends Figure{

    private final Point centerPoint;
    private final double width, height;

    public double getHeight() { return height; }

    public double getWidth() { return width; }

    public RadialFigure(Point centerPoint, double width, double height, RadialGradient gradient, Shadow shadeType) {
        super(gradient, shadeType);
        this.centerPoint = centerPoint;
        this.width = width;
        this.height = height;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }


    @Override
    public RadialGradient getGradient(){ return (RadialGradient) super.getGradient(); }
    public void setGradient(RadialGradient p){ super.setGradient(p);}

}
