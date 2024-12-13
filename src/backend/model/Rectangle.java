    package backend.model;

    import java.util.ArrayList;
    import java.util.List;
    import java.awt.*;
    public class Rectangle extends Figure {

        private Point topLeft;
        private Point bottomRight;

        public Rectangle(Point topLeft, Point bottomRight, Shadow shadeType, boolean hasBevel) {
            super(
                    shadeType,
                    topLeft.centerPointTo(bottomRight),
                    bottomRight.x - topLeft.x,
                    bottomRight.y - topLeft.y,
                    hasBevel
            );
            this.topLeft = topLeft;
            this.bottomRight = bottomRight;
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

        public Point getTopLeft() { return topLeft; }
        public Point getBottomRight() { return bottomRight; }

        @Override
        public Figure move(Point newCenter){
            Point topLeftOffset = getTopLeft().getDifference(getCenterPoint());
            Point botRightOffset = getBottomRight().getDifference(getCenterPoint());
            this.topLeft = newCenter.add(topLeftOffset);
            this.bottomRight = newCenter.add(botRightOffset);
            return super.move(newCenter);
        }

        public void turnRight() {
            Point newCornersOffset = new Point(getHeight() / 2, getWidth() / 2);
            this.topLeft = getCenterPoint().substract(newCornersOffset);
            this.bottomRight = getCenterPoint().add(newCornersOffset);
            this.resize(getHeight(), getWidth());
        }


        public Rectangle duplicate(Integer offset){
            return new Rectangle(
                    getTopLeft().add(offset),
                    getBottomRight().add(offset),
                    getShadeType(),
                    getHasBevel());
        }

        @Override
        public void resize(double width, double height){
            super.resize(width, height);
            Point cornerOffset = new Point(getWidth() / 2, getHeight() / 2);
            this.topLeft = getCenterPoint().substract(cornerOffset);
            this.bottomRight = getCenterPoint().add(cornerOffset);
        }

    }
