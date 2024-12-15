package frontend.DrawingTool;

import backend.CanvasState;
import backend.model.*;
import frontend.PaintPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

import java.util.HashMap;
import java.util.Map;

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

    private static final Color defaultStartColor = Color.YELLOW;
    private static final Color defaultEndColor = Color.ORANGE;
    public DrawingTool(Canvas canvas, CanvasState cs) {
        this.canvasState = cs;
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.pp = pp;
        this.drawingMode = DrawingMode.RECT;
        gc.setLineWidth(drawingMode.getDefaultWidth());
        this.startColor = defaultStartColor;
        this.endColor = defaultEndColor;
    }

    private DrawingMode drawingMode;
    public void setDrawingMode(DrawingMode drawingMode){
        this.drawingMode = drawingMode;
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


    boolean figToDrawHasBevel;
    Shadow figToDrawShadowType;
    public boolean isFigToDrawHasBevel() {
        return figToDrawHasBevel;
    }
    public void setFigToDrawHasBevel(boolean figToDrawHasBevel) {
        this.figToDrawHasBevel = figToDrawHasBevel;
    }
    public Shadow getFigToDrawShadowType() {
        return figToDrawShadowType;
    }
    public void setFigToDrawShadowType(Shadow figToDrawShadowType) {
        this.figToDrawShadowType = figToDrawShadowType;
    }

    public void drawFigure(Point startPoint, Point endPoint) {
        if (startPoint == null)
            return;
        if(this.drawingMode.equals(DrawingMode.NONE))
            return;

        Figure fig = drawingMode.createFigure(startPoint, endPoint, this.figToDrawShadowType, this.figToDrawHasBevel);
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
}
