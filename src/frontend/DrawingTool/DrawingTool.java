package frontend.DrawingTool;

import backend.CanvasState;
import backend.Pair;
import backend.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

import java.util.HashMap;
import java.util.Map;

public class DrawingTool {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final CanvasState canvasState;
    //map figures to Pair containing options for drawing it
    private final Map<Figure, Pair<DrawingMode, Paint>> drawingModeMap = new HashMap<>();
    private Figure copiedFigure = null;

    public Figure getCopiedFigure() {
        return copiedFigure;
    }

    public void setCopiedFigure(Figure copiedFigure) {
        this.copiedFigure = copiedFigure;
    }

    public Paint getGradientForFigure(Figure fig, Color c1, Color c2){
        return drawingModeMap.get(fig).getLeft().getGradient(c1, c2);
    }
    public void setExistingFigureColor(Figure fig, Paint p){
        if(drawingModeMap.get(fig) == null)
            throw new RuntimeException("Attempting to change paint on non existent figure");
        Pair<DrawingMode, Paint> options = new Pair<>(drawingModeMap.get(fig).getLeft(), p);
        drawingModeMap.remove(fig);
        drawingModeMap.put(fig, options);
    }
    public void setFigureDrawingOptionsPair(Figure fig, Pair<DrawingMode, Paint> pair){
        drawingModeMap.remove(fig);
        drawingModeMap.put(fig, pair);
    }
    public Pair<DrawingMode, Paint> getFigurePair(Figure fig){
        return drawingModeMap.get(fig);
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
        this.useDrawingMode = true;
        this.selectionOn = false;
    }

    private boolean selectionOn = false;
    private boolean useDrawingMode = false;
    public void setUseDrawingMode(boolean useDrawingMode){
        this.useDrawingMode = useDrawingMode;
    }
    public boolean getUseDrawingMode() {
        return useDrawingMode;
    }
    public boolean isSelectionOn() {
        return selectionOn;
    }
    public void setSelectionOn(boolean selectionOn) {
        this.selectionOn = selectionOn;
        if(selectionOn)
            setUseDrawingMode(false);
    }


    public void createAndAddFigure(Point startPoint, Point endPoint) {
        if (startPoint == null || endPoint == null)
            return;
        Figure fig = drawingMode.createFigure(startPoint, endPoint, this.shadowType, this.bevel);
        Paint p = drawingMode.getGradient(
                this.startColor,
                this.endColor
        );
        addFigure(fig, new Pair<>(drawingMode, p), 1);
    }
    public void addFigure(Figure fig, Pair<DrawingMode, Paint> drawingInfo, Integer layerIndex){
        drawingModeMap.put(fig, drawingInfo);
        canvasState.addFigure(fig, layerIndex);
        redrawCanvas();
    }
    public void deleteFigure(Figure fig, Integer layer){
        canvasState.deleteFigure(fig, layer);
        drawingModeMap.remove(fig);
    }
    public void redrawCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for(Figure fig : canvasState.visibleFigures()){
            drawingModeMap.get(fig).getLeft().drawFigure(fig, drawingModeMap.get(fig).getRight(), canvasState.isSelected(fig), this.gc);
        }
    }

    public void pasteFormatOnto(Iterable<Figure> figs){
        if(copiedFigure == null)
            return;
        for(Figure fig : figs){
            pasteFormatOnto(fig);
        }
    }

    public void pasteFormatOnto(Figure fig){
        if(copiedFigure == null)
            return;
      //  Pair<DrawingMode, Paint> copiedFormat = getFigurePair(copiedFigure);
        fig.setShadeType(copiedFigure.getShadeType());
        fig.setHasBevel(copiedFigure.getHasBevel());
        DrawingMode currentMode = getFigurePair(fig).getLeft();
        Pair<DrawingMode, Paint> options = new Pair<>(currentMode, currentMode.getGradient(Color.PINK, Color.DEEPPINK));
        setFigureDrawingOptionsPair(fig, options );
        redrawCanvas();
    }

}
