package backend.model;

public class Ellipse extends Figure {

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis, Shadow shadeType, boolean hasBevel) {
        super( shadeType,centerPoint, sMayorAxis, sMinorAxis, hasBevel);
    }
    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", this.getCenterPoint(), getWidth(), getHeight());
    }
    @Override
    public boolean pointBelongs(Point p){
        return ((Math.pow(p.getX() - this.getCenterPoint().getX(), 2) / Math.pow(this.getWidth(), 2)) +
                (Math.pow(p.getY() - this.getCenterPoint().getY(), 2) / Math.pow(this.getHeight(), 2))) <= 0.30;
    }

    @Override
    public void turnRight() {
        resize(getHeight(), getWidth());
    }
 public Ellipse duplicate(){
            return new Ellipse(getCenterPoint().add(getDuplicateOffset()), getWidth(), getHeight(), getShadeType(), getHasBevel());
        }

}
