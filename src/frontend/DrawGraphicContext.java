package frontend;

import backend.model.Figure;
import backend.model.LinearFigure;
import backend.model.RadialFigure;
import backend.model.Shadow;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

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

    public void drawFigure(Figure fig, boolean isSelected){
        switch(fig){
            case RadialFigure rf -> { drawRadialFigure( rf, isSelected);}
            case LinearFigure lf -> { drawLinearFigure(lf, isSelected);}
            default -> { throw new RuntimeException("Unssuported figure");}
        }
    }

    private void drawRadialFigure(RadialFigure figure, boolean isSelected){
        if(figure.getShadeType() != Shadow.NONE){
            gc.setFill(ch.awtColorToFxColor(figure.getShadeType().getShadowColor(figure.getColors().getLast())));
            gc.fillOval(figure.getCenterPoint().getX() - figure.getWidth() / 2 + figure.getShadeType().getOffset(),
                    figure.getCenterPoint().getY() - figure.getHeight() / 2 + figure.getShadeType().getOffset(), figure.getWidth(), figure.getHeight());
        }
        if(figure.getHasBevel()){
            drawRadialBevel(figure);
        }
        gc.setFill(ch.radialGradientFromColorList(figure.getColors()));
        gc.setStroke(isSelected ? selectedLineColor : lineColor);
        gc.strokeOval(figure.getCenterPoint().getX() - (figure.getWidth() / 2),  figure.getCenterPoint().getY() - (figure.getHeight() / 2), figure.getWidth(), figure.getHeight());
        gc.fillOval(figure.getCenterPoint().getX() - (figure.getWidth() / 2), figure.getCenterPoint().getY() - (figure.getHeight() / 2), figure.getWidth(), figure.getHeight());

    }
    private void drawRadialBevel(RadialFigure figure){
        gc.setStroke(Color.LIGHTGRAY);
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

    private void drawLinearFigure(LinearFigure figure, boolean isSelected){
        if(figure.getShadeType() != Shadow.NONE) {
            gc.setFill(ch.awtColorToFxColor(figure.getShadeType().getShadowColor(figure.getColors().getLast())));
            gc.fillRect(figure.getTopLeft().getX() + figure.getShadeType().getOffset(),
                    figure.getTopLeft().getY() + figure.getShadeType().getOffset(),
                    Math.abs(figure.getTopLeft().getX() - figure.getBottomRight().getX()),
                    Math.abs(figure.getTopLeft().getY() - figure.getBottomRight().getY()));
        }
        if(figure.getHasBevel()) {
            drawLinearBevel(figure);
        }
        gc.setFill(ch.linearGradientFromColorList(figure.getColors()));
        gc.fillRect(figure.getTopLeft().getX(), figure.getTopLeft().getY(),
                Math.abs(figure.getTopLeft().getX() - figure.getBottomRight().getX()), Math.abs(figure.getTopLeft().getY() - figure.getBottomRight().getY()));
        gc.setStroke(isSelected ? selectedLineColor : lineColor);
        gc.strokeRect(figure.getTopLeft().getX(), figure.getTopLeft().getY(),
                Math.abs(figure.getTopLeft().getX() - figure.getBottomRight().getX()), Math.abs(figure.getTopLeft().getY() - figure.getBottomRight().getY()));
    }
    private void drawLinearBevel(LinearFigure figure){
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
