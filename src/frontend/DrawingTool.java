package frontend;

import backend.CanvasState;
import backend.model.*;
import backend.FigureBuilder;
import frontend.Events.DrawingMode;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DrawingTool {
    private final Canvas canvas;
    private final GraphicsContext gc;

    private final CanvasState canvasState;
    //todo Map<Figure, Tuple<Paint, DrawingMode>
    private final Map<Figure, Paint> cStateColorMap = new HashMap<>();
    private final Map<Figure, DrawingMode> drawModeMap = new HashMap<>();

    public void setFigureColor(Figure fig, Paint p){
        cStateColorMap.remove(fig);
        cStateColorMap.put(fig, p);
    }
    public Paint getFigureColor(Figure fig){
        return cStateColorMap.get(fig);
    }

    PaintPane pp;
    public CanvasState getCanvasState() { return this.canvasState; }

    public DrawingTool(Canvas canvas, CanvasState cs, PaintPane pp) {
        this.canvasState = cs;
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.pp = pp;
        this.drawingMode = DrawingMode.LINEAR;
        gc.setLineWidth(drawingMode.getDefaultWidth());
    }

    private FigureBuilder figureBuilder;
    private DrawingMode drawingMode;

    public void setBuilder(FigureBuilder builder){
        switch (builder){
            case RECT:
            case SQUARE:
                this.drawingMode = DrawingMode.LINEAR;
                break;
            case CIRCLE:
            case ELLIPSE:
                this.drawingMode = DrawingMode.RADIAL;
                break;
        }
        this.figureBuilder = builder;
    }
    //temporarily disables drawing
    //todo improve
    public void setBuilder(boolean bool){
        if(bool){
            this.setBuilder(this.figureBuilder);
        } else {
            this.drawingMode = DrawingMode.NONE;
        }
    }

    private Shadow shadowType = Shadow.NONE;
    private boolean bevelOn = false;
    //todo not hardcode
    private Color startColor = Color.YELLOW;
    private Color endColor = Color.ORANGE;

    public Color getEndColor() {
        return endColor;
    }

    public void setEndColor(Color endColor) {
        this.endColor = endColor;
    }

    public Color getStartColor() {
        return startColor;
    }

    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }

    public boolean isBevelOn() {
        return bevelOn;
    }

    public void setBevelOn(boolean bevelOn) {
        this.bevelOn = bevelOn;
    }

    public Shadow getShadowType() {
        return shadowType;
    }

    public void setShadowType(Shadow shadowType) {
        this.shadowType = shadowType;
    }

    public void drawFigure(Point startPoint, Point endPoint) {
        if (startPoint == null)
            return;
        if(this.drawingMode.equals(DrawingMode.NONE))
            return;
        Shadow shadowType = this.shadowType;
        boolean hasBevel = this.bevelOn;
        Figure fig = figureBuilder.createFigure(startPoint, endPoint, shadowType, hasBevel);
        Paint p = drawingMode.getGradient(
                this.startColor,
                this.endColor
        );

        cStateColorMap.put(fig, p);
        drawModeMap.put(fig, drawingMode);
        canvasState.addFigure(fig, 1);
        pp.startPoint = null;
        redrawCanvas();
    }

    public void redrawCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for(Figure fig : canvasState.visibleFigures()){
            drawModeMap.get(fig).drawFigure(fig, this.cStateColorMap.get(fig), canvasState.isSelected(fig), this.gc);
        }
    }


    public void forEachSelectedFigureThenRedraw(Consumer<Figure> func){
        canvasState.forEachSelectedFigure(func);
        redrawCanvas();
    }
}
