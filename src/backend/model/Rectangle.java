    package backend.model;

    import java.util.ArrayList;
    import java.util.List;
    import java.awt.*;
    public class Rectangle extends Figure {

        public Rectangle(Point topLeft, Point bottomRight, Shadow shadeType, boolean hasBevel) {
            super(
                    shadeType,
                    topLeft.centerPointTo(bottomRight),
                    bottomRight.x - topLeft.x,
                    bottomRight.y - topLeft.y,
                    hasBevel
            );
        }

        @Override
        public String toString() {
            return String.format("Rectángulo [ %s , %s ]", this.getTopLeft(), this.getBottomRight());
        }

        @Override
        public boolean pointBelongs(Point p) {
            return p.getX() > this.getTopLeft().getX() &&
                    p.getX() < this.getBottomRight().getX() &&
                    p.getY() > this.getTopLeft().getY() &&
                    p.getY() < this.getBottomRight().getY();
        }
        public void turnRight() {
            this.resize(getHeight(), getWidth());
        }

        public Rectangle duplicate(Integer offset){
            return new Rectangle(
                    getTopLeft().add(offset),
                    getBottomRight().add(offset),
                    getShadeType(),
                    getHasBevel());
        }
    }
