package frontend.DrawingTool;

import backend.CanvasState;
import backend.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class DrawingTool {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final CanvasState canvasState;
    private final Map<Figure, Pair<DrawingMode, Paint>> drawingModeMap = new HashMap<>();
    private boolean selectionOn = false;

    public boolean isSelectionOn() {
        return selectionOn;
    }
    public void setSelectionOn(boolean selectionOn) {
        this.selectionOn = selectionOn;
    }
    public Paint getGradientForFigure(Figure fig, Color c1, Color c2){
        return drawingModeMap.get(fig).getKey().getGradient(c1, c2);
    }
    public void setFigureColor(Figure fig, Paint p){
        Pair<DrawingMode, Paint> options = new Pair<>(drawingModeMap.get(fig).getKey(), p);
        drawingModeMap.remove(fig);
        drawingModeMap.put(fig, options);
    }


    public CanvasState getCanvasState() { return this.canvasState; }

    private static final Color defaultStartColor = Color.YELLOW;
    private static final Color defaultEndColor = Color.ORANGE;
    public DrawingTool(Canvas canvas, CanvasState cs) {
        this.canvasState = cs;
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.drawingMode = DrawingMode.RECT;
        gc.setLineWidth(drawingMode.getDefaultWidth());
        this.startColor = defaultStartColor;
        this.endColor = defaultEndColor;
        this.bevel = false;
        this.shadowType = Shadow.NONE;
    }

    private Color startColor;
    private Color endColor;
    public Color getEndColor() {
        return endColor;
    }
    public Color getStartColor() {
        return startColor;
    }
    public void setEndColor(Color endColor) {
        this.endColor = endColor;
    }
    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }

    private DrawingMode drawingMode;
    private boolean bevel;
    private Shadow shadowType;
    public void setBevel(boolean bevel) {
        this.bevel = bevel;
    }
    public void setShadowType(Shadow shadowType) {
        this.shadowType = shadowType;
    }
    public void setDrawingMode(DrawingMode drawingMode){
        this.drawingMode = drawingMode;
    }

    public void drawFigure(Point startPoint, Point endPoint) {
        if (startPoint == null)
            return;
        if (this.drawingMode.equals(DrawingMode.NONE))
            return;

        Figure fig = drawingMode.createFigure(startPoint, endPoint, this.shadowType, this.bevel);
        Paint p = drawingMode.getGradient(
                this.startColor,
                this.endColor
        );
        addFigure(fig, new Pair<>(drawingMode, p), 1);
    }
    public void addFigure(Figure fig, Pair<DrawingMode, Paint> drawingInfo, Integer layerIndex){
        if(drawingInfo.getKey().equals(DrawingMode.NONE))
            throw new RuntimeException("attempting to add figure with invalid drawingMode");
        drawingModeMap.put(fig, drawingInfo);
        canvasState.addFigure(fig, layerIndex);
        redrawCanvas();
    }

    public void redrawCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for(Figure fig : canvasState.visibleFigures()){
            drawingModeMap.get(fig).getKey().drawFigure(fig, drawingModeMap.get(fig).getValue(), canvasState.isSelected(fig), this.gc);
        }
    }
}
