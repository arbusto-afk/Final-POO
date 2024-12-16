package backend.model;


public class Square extends Rectangle {

    public Square(Point topLeft, double size, Shadow shadeType, boolean hasBevel) {
        super(topLeft, new Point(topLeft.x + size, topLeft.y + size), shadeType, hasBevel);
    }
    @Override
    public String toString() {
        return String.format("Cuadrado [ %s , %s ]", super.getTopLeft(), super.getBottomRight());
    }
    @Override
    public Square duplicate(Integer offset) {
       return new Square(getTopLeft().add(offset), getHeight(), getShadeType(), getHasBevel());
    }
}
