package frontend.Events;

import backend.model.Ellipse;
import backend.model.Figure;
import backend.model.Rectangle;
import backend.model.Shadow;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;

import javax.sound.sampled.Line;

public enum DrawingMode {
    RADIAL{
        @Override
        public RadialGradient getGradient(Color startColor, Color endColor){
            return new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, startColor),
                    new Stop(1, endColor));
        }

        public void drawFigure(Figure fig, Paint p, boolean isSelected, GraphicsContext gc){
            Ellipse figure = (Ellipse) fig;
            if(figure.getShadeType() != Shadow.NONE){
                drawRadialShadow(figure, (RadialGradient) p, gc);
            }
            if(figure.getHasBevel()){
                drawRadialBevel(figure, gc);
            }
            gc.setFill(p);
            gc.setStroke(isSelected ? this.getSelectedLineColor() : this.getLineColor());
            gc.strokeOval(figure.getCenterPoint().getX() - (figure.getWidth() / 2),  figure.getCenterPoint().getY() - (figure.getHeight() / 2), figure.getWidth(), figure.getHeight());
            gc.fillOval(figure.getCenterPoint().getX() - (figure.getWidth() / 2), figure.getCenterPoint().getY() - (figure.getHeight() / 2), figure.getWidth(), figure.getHeight());

        }
        private void drawRadialShadow(Ellipse figure, RadialGradient p, GraphicsContext gc){
            if (figure.getShadeType() != Shadow.NONE) {
                Color shadowColor = Color.GRAY; // Color por defecto para sombra
                if (figure.getShadeType() == Shadow.COLOR ||
                        figure.getShadeType() == Shadow.INVERTEDCOLOR) {
                    shadowColor = p.getStops().getLast().getColor().darker();
                }
                gc.setFill(shadowColor);

                gc.fillOval(figure.getCenterPoint().getX() - figure.getWidth() / 2 + figure.getShadeType().getOffset(),
                        figure.getCenterPoint().getY() - figure.getHeight() / 2 + figure.getShadeType().getOffset(), figure.getWidth(), figure.getHeight());
            }
        }
        private void drawRadialBevel(Ellipse figure, GraphicsContext gc){
            gc.setStroke(Color.GRAY);
            double aux = gc.getLineWidth();
            gc.setLineWidth(this.getBevelWidth());
            gc.strokeArc(
                    figure.getCenterPoint().getX() - (figure.getWidth() / 2),
                    figure.getCenterPoint().getY() - (figure.getHeight() / 2),
                    figure.getWidth(),
                    figure.getHeight(),
                    45,
                    180,
                    ArcType.OPEN
            );
            gc.setStroke(Color.BLACK);
            gc.strokeArc(
                    figure.getCenterPoint().getX() - (figure.getWidth() / 2),
                    figure.getCenterPoint().getY() - (figure.getHeight() / 2),
                    figure.getWidth(),
                    figure.getHeight(),
                    225,
                    180,
                    ArcType.OPEN
            );
            gc.setLineWidth(aux);
        }
    },
    LINEAR {
        @Override
        public LinearGradient getGradient(Color startColor, Color endColor) {
            return new LinearGradient(0, 0, 1, 0, true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, startColor),
                    new Stop(1, endColor));
        }
        public void  drawFigure(Figure figure, Paint p, boolean isSelected, GraphicsContext gc){
            Rectangle rect = (Rectangle) figure;
            if (rect.getShadeType() != Shadow.NONE) {
                drawLinearShadow(rect, (LinearGradient) p, gc);
            }
            if(rect.getHasBevel()) {
                drawLinearBevel(rect, gc);
            }

            double height = figure.getHeight();
            double width = figure.getWidth();
            gc.setFill(p);
            gc.fillRect(rect.getTopLeft().getX(), rect.getTopLeft().getY(), width, height);
            gc.setStroke(isSelected ? getSelectedLineColor() : getLineColor());
            gc.strokeRect(rect.getTopLeft().getX(), rect.getTopLeft().getY(), width, height);
        }
        private void drawLinearShadow(Rectangle figure, LinearGradient lg, GraphicsContext gc){
                Color shadowColor = Color.GRAY;
                if (figure.getShadeType() == Shadow.COLOR || figure.getShadeType() == Shadow.INVERTEDCOLOR) {
                    shadowColor = lg.getStops().getLast().getColor().darker();
                }
                gc.setFill(shadowColor);
                gc.fillRect(figure.getTopLeft().getX() + figure.getShadeType().getOffset(),
                        figure.getTopLeft().getY() + figure.getShadeType().getOffset(),
                        Math.abs(figure.getTopLeft().getX() - figure.getBottomRight().getX()),
                        Math.abs(figure.getTopLeft().getY() - figure.getBottomRight().getY()));
        }
        private void drawLinearBevel(Rectangle figure, GraphicsContext gc){
            double aux = gc.getLineWidth();
            double x = figure.getTopLeft().getX();
            double y = figure.getTopLeft().getY();
            double width = figure.getWidth();
            double height = figure.getHeight();

            gc.setLineWidth(this.getBevelWidth());
            gc.setStroke(Color.LIGHTGRAY);
            gc.strokeLine(x, y, x + width, y);
            gc.strokeLine(x, y, x, y + height);
            gc.setStroke(Color.BLACK);
            gc.strokeLine(x + width, y, x + width, y + height);
            gc.strokeLine(x, y + height, x + width, y + height);
            gc.setLineWidth(aux);
        }

    },
    NONE{
        @Override
        public Paint getGradient(Color startColor, Color endColor) {
            return null;
        }

        @Override
        public void drawFigure(Figure figure, Paint p, boolean isSelected, GraphicsContext gc) {

        }
    };

    private final Integer bevelWidth = 7;
    private final Integer defaultWidth = 2;
    private final Color lineColor = Color.BLACK;
    private final Color selectedLineColor = Color.RED;

    public Integer getDefaultWidth() {
        return defaultWidth;
    }
    public Color getLineColor() {
        return lineColor;
    }
    public Color getSelectedLineColor() {
        return selectedLineColor;
    }
    public Integer getBevelWidth() { return bevelWidth; }

    public abstract Paint getGradient(Color startColor, Color endColor);
    public abstract void drawFigure(Figure figure, Paint p, boolean isSelected, GraphicsContext gc);

}