package backend;

import backend.model.*;

public enum FigureBuilder {
        RECT {
            @Override
            public Figure createFigure(Point startPoint, Point endPoint, Shadow shadowType, boolean hasBevel) {
                return new Rectangle(startPoint, endPoint, shadowType, hasBevel);
            }
        },
        SQUARE {
            @Override
            public Figure createFigure(Point startPoint, Point endPoint, Shadow shadowType, boolean hasBevel) {
                double xDiff = Math.abs(endPoint.getX() - startPoint.getX());
                return new Square(startPoint, xDiff, shadowType, hasBevel);
            }
        },
        CIRCLE {
            @Override
            public Figure createFigure(Point startPoint, Point endPoint, Shadow shadowType, boolean hasBevel) {
                double xDiff = Math.abs(endPoint.getX() - startPoint.getX());
                return new Circle(startPoint, xDiff, shadowType, hasBevel);
            }
        },
        ELLIPSE {
            @Override
            public Figure createFigure(Point startPoint, Point endPoint, Shadow shadowType, boolean hasBevel) {
                Point centerPoint = startPoint.centerPointTo(endPoint);
                double xDiff = Math.abs(endPoint.getX() - startPoint.getX());
                double yDiff = Math.abs(endPoint.getY() - startPoint.getY());
                return new Ellipse(centerPoint, xDiff, yDiff, shadowType, hasBevel);
            }
        };

        public abstract Figure createFigure(Point startPoint, Point endPoint, Shadow shadowType, boolean hasBevel);
}
