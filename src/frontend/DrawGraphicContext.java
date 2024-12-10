package frontend;

import backend.model.Ellipse;
import backend.model.Figure;
import backend.model.Rectangle;
import backend.model.Shadow;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.ArcType;

import java.util.List;

public class DrawGraphicContext {
    GraphicsContext gc;
    ColorHandler ch = new ColorHandler();

    final Integer bevelWidth = 7;
    final Integer defaultWidth = 2;

    Color lineColor = Color.BLACK;
    Color selectedLineColor = Color.RED;

    public DrawGraphicContext(GraphicsContext gc){
        this.gc = gc;
        gc.setLineWidth(defaultWidth);
    }

    public void drawFigure(Figure fig, boolean isSelected, List<Color> c){
        List<Color> colors = fig.getColors();
        if (colors == null || colors.isEmpty()) {
            colors = List.of(Color.YELLOW, Color.ORANGE);
        }
        switch(fig){
            case Ellipse rf -> { drawRadialFigure( rf, isSelected, colors); }
            case Rectangle lf -> { drawLinearFigure(lf, isSelected, colors);}
            default -> { throw new RuntimeException("Unssuported figure");}
        }
    }

    private void drawRadialFigure(Ellipse figure, boolean isSelected, List<Color> colors){
        if (figure.getShadeType() != Shadow.NONE) {
            Color shadowColor = Color.GRAY; // Color por defecto para sombra
            if (figure.getShadeType() == Shadow.COLOR || figure.getShadeType() == Shadow.INVERTEDCOLOR  && !colors.isEmpty()) {
                shadowColor = colors.get(0).deriveColor(0, 1, 0.7, 1);
            }
            gc.setFill(shadowColor);

            gc.fillOval(figure.getCenterPoint().getX() - figure.getWidth() / 2 + figure.getShadeType().getOffset(),
                    figure.getCenterPoint().getY() - figure.getHeight() / 2 + figure.getShadeType().getOffset(), figure.getWidth(), figure.getHeight());
        }
        if(figure.getHasBevel()){
            drawRadialBevel(figure);
        }
        gc.setFill(ch.radialGradientFromColorList(colors));
        gc.setStroke(isSelected ? selectedLineColor : lineColor);
        gc.strokeOval(figure.getCenterPoint().getX() - (figure.getWidth() / 2),  figure.getCenterPoint().getY() - (figure.getHeight() / 2), figure.getWidth(), figure.getHeight());
        gc.fillOval(figure.getCenterPoint().getX() - (figure.getWidth() / 2), figure.getCenterPoint().getY() - (figure.getHeight() / 2), figure.getWidth(), figure.getHeight());

    }
    private void drawRadialBevel(Ellipse figure){
        gc.setStroke(Color.GRAY);
        double aux = gc.getLineWidth();
        gc.setLineWidth(bevelWidth);
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

    private void drawLinearFigure(Rectangle figure, boolean isSelected, List<Color> colors){
        if (figure.getShadeType() != Shadow.NONE) {
            Color shadowColor = Color.GRAY;
            if (figure.getShadeType() == Shadow.COLOR || figure.getShadeType() == Shadow.INVERTEDCOLOR  && !colors.isEmpty()) {
                shadowColor = colors.get(0).deriveColor(0, 1, 0.7, 1);
            }
            gc.setFill(shadowColor);
            gc.fillRect(figure.getTopLeft().getX() + figure.getShadeType().getOffset(),
                    figure.getTopLeft().getY() + figure.getShadeType().getOffset(),
                    Math.abs(figure.getTopLeft().getX() - figure.getBottomRight().getX()),
                    Math.abs(figure.getTopLeft().getY() - figure.getBottomRight().getY()));
        }
        if(figure.getHasBevel()) {
            drawLinearBevel(figure);
        }
        gc.setFill(ch.linearGradientFromColorList(colors));
        gc.fillRect(figure.getTopLeft().getX(), figure.getTopLeft().getY(),
                Math.abs(figure.getTopLeft().getX() - figure.getBottomRight().getX()), Math.abs(figure.getTopLeft().getY() - figure.getBottomRight().getY()));
        gc.setStroke(isSelected ? selectedLineColor : lineColor);
        gc.strokeRect(figure.getTopLeft().getX(), figure.getTopLeft().getY(),
                Math.abs(figure.getTopLeft().getX() - figure.getBottomRight().getX()), Math.abs(figure.getTopLeft().getY() - figure.getBottomRight().getY()));
    }
    private void drawLinearBevel(Rectangle figure){
        double aux = gc.getLineWidth();
        double x = figure.getTopLeft().getX();
        double y = figure.getTopLeft().getY();
        double width = Math.abs(x - figure.getBottomRight().getX());
        double height = Math.abs(y - figure.getBottomRight().getY());
        gc.setLineWidth(this.bevelWidth);
        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeLine(x, y, x + width, y);
        gc.strokeLine(x, y, x, y + height);
        gc.setStroke(Color.BLACK);
        gc.strokeLine(x + width, y, x + width, y + height);
        gc.strokeLine(x, y + height, x + width, y + height);
        gc.setLineWidth(aux);
    }
}
