package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;

import java.util.List;

public class DrawingTool {
    GraphicsContext gc;
    ColorHandler ch = new ColorHandler();

    final Integer bevelWidth = 7;
    final Integer defaultWidth = 2;

    Color lineColor = Color.BLACK;
    Color selectedLineColor = Color.RED;

    final CanvasState<Paint> canvasState;

    PaintPane pp;
    public DrawingTool(GraphicsContext gc, CanvasState<Paint> cs, PaintPane pp){
        this.gc = gc;
        this.pp = pp;
        gc.setLineWidth(defaultWidth);
        canvasState = cs;
    }
    public void drawFigure(Point startPoint, Point endPoint){
        if(startPoint == null)
            return;
        Figure newFigure;
        ToggleButton b = (ToggleButton) (pp.tools.getSelectedToggle());

        if (b != null) {
           // List<Color> colors = ch.colorListFromColorPickerArr(pp.colorPickers);
            Shadow shadowType = pp.shadowTypeBox.getValue();
            boolean hasBevel = pp.bevelCheckbox.isSelected();

            switch (b.getText()) {
                case PaintPane.squareText:
                    double size = Math.abs(endPoint.getX() - startPoint.getX());
                    newFigure = new Square(startPoint, size, shadowType, hasBevel);
                    break;
                case PaintPane.rectangleText:
                    newFigure = new Rectangle(startPoint, endPoint, shadowType, hasBevel);
                    break;
                case PaintPane.circleText:
                    double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
                    newFigure = new Circle(startPoint, circleRadius, shadowType, hasBevel);
                    break;
                case PaintPane.ellipseText:
                    Point centerPoint = new Point(
                            Math.abs(endPoint.x + startPoint.x) / 2,
                            Math.abs(endPoint.y + startPoint.y) / 2
                    );
                    double sMajorAxis = Math.abs(endPoint.x - startPoint.x);
                    double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
                    newFigure = new Ellipse(centerPoint, sMajorAxis, sMinorAxis, shadowType, hasBevel);
                    break;
                default:
                    return;
            }
          //  newFigure.setColors(colors);
            Paint p;
            switch(newFigure) {
                case Rectangle r:{
                    p = new LinearGradient(0, 0, 1, 0, true,
                            CycleMethod.NO_CYCLE,
                            new Stop(0, pp.fillColorPicker.getValue()),
                            new Stop(1, pp.fillSecondaryColorPicker.getValue()));
            break;
                }
                case Ellipse e: {
                    p = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
                            CycleMethod.NO_CYCLE,
                            new Stop(0, pp.fillColorPicker.getValue()),
                            new Stop(1, pp.fillSecondaryColorPicker.getValue()));
                break;
                }
                default:
                    throw new RuntimeException("unsup fig");
            }
            canvasState.setFigureColor(newFigure, p);
            newFigure.setHasBevel(pp.bevelCheckbox.isSelected());
            pp.canvasState.addFigure(newFigure, 1);

            pp.startPoint = null;
            pp.selectionDragStartOffset = null;
            redrawCanvas();
        }
    }


    public void redrawCanvas() {
        Canvas canvas = gc.getCanvas();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvasState.forEachVisibleFigure(fig ->
                drawFigure(fig, canvasState.isSelected(fig), canvasState.getFigureColor(fig))
        );
    }
    public void drawFigure(Figure fig, boolean isSelected, Paint p){

        switch(fig){
            case Ellipse rf -> { drawRadialFigure( rf, isSelected, (RadialGradient) p); }
            case Rectangle lf -> { drawLinearFigure(lf, isSelected, (LinearGradient) p);}
            default -> { throw new RuntimeException("Unhandled figure");}
        }
    }

    private void drawRadialFigure(Ellipse figure, boolean isSelected, RadialGradient p){
        if (figure.getShadeType() != Shadow.NONE) {
            Color shadowColor = Color.GRAY; // Color por defecto para sombra
            if (figure.getShadeType() == Shadow.COLOR ||
                    figure.getShadeType() == Shadow.INVERTEDCOLOR) {
                shadowColor = p.getStops().getFirst().getColor();
            }
            gc.setFill(shadowColor);

            gc.fillOval(figure.getCenterPoint().getX() - figure.getWidth() / 2 + figure.getShadeType().getOffset(),
                    figure.getCenterPoint().getY() - figure.getHeight() / 2 + figure.getShadeType().getOffset(), figure.getWidth(), figure.getHeight());
        }
        if(figure.getHasBevel()){
            drawRadialBevel(figure);
        }
        gc.setFill(p);
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

    private void drawLinearFigure(Rectangle figure, boolean isSelected, LinearGradient p){
        if (figure.getShadeType() != Shadow.NONE) {
            Color shadowColor = Color.GRAY;
            if (figure.getShadeType() == Shadow.COLOR ||
                    figure.getShadeType() == Shadow.INVERTEDCOLOR) {
                shadowColor = p.getStops().getLast().getColor();
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
        gc.setFill(p);
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
