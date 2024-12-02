package backend.model;
import javafx.scene.paint.RadialGradient;

public abstract class CircularFigure extends Figure{
    public CircularFigure(RadialGradient gradient, Shadow shadeType){
        super(gradient, shadeType);
    }
}
