package frontend.Events;

import backend.CanvasState;
import backend.model.Figure;
import backend.model.Point;
import frontend.DrawingTool.DrawingTool;
import frontend.StatusPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FigureCanvas extends Canvas {

    private final CanvasState canvasState;
    private final DrawingTool drawingTool;
    private final StatusPane statusPane;
    private Point startPoint;
    private final Map<Figure, Point> selectionDragStartOffset = new HashMap<>();

    public FigureCanvas(CanvasState canvasState, double width, double height, StatusPane statusPane){
        super(width, height);
        this.canvasState = canvasState;
        this.drawingTool = new DrawingTool(this, canvasState);
        this.statusPane = statusPane;

        this.setOnMousePressed(this::onMousePressed);
        this.setOnMouseReleased(this::onMouseRelease);
        this.setOnMouseMoved(this::onMouseMoved);
        this.setOnMouseClicked(this::onMouseClicked);
        this.setOnMouseDragged(this::onMouseDragged);
    }

    public DrawingTool getDrawingTool() {
        return drawingTool;
    }

    private void onMousePressed(MouseEvent event) {
        startPoint = new Point(event.getX(), event.getY());
        for(Figure fig : canvasState.selectedFigures()){
            selectionDragStartOffset.put(fig, fig.getCenterPoint().getDifference(startPoint));
        }
    }
    private void onMouseRelease(MouseEvent event) {
        if (startPoint == null) {
            return;
        }
        Point endPoint = new Point(event.getX(), event.getY());
        if (endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
            return;
        }

        drawingTool.getCanvasState().deselectFigures();
        selectionDragStartOffset.clear();
        if (drawingTool.isSelectionOn()) {
            for (Figure fig : canvasState.visibleFigures()) {
                Point topL = fig.getCenterPoint().substract(fig.getWidth() / 2, fig.getHeight() / 2);
                Point botR = fig.getCenterPoint().add(fig.getWidth() / 2, fig.getHeight() / 2);
                if (topL.isInRect(startPoint, endPoint) && botR.isInRect(startPoint, endPoint)) {
                    canvasState.selectFigure(fig);
                }
            }
        }
        if (drawingTool.getUseDrawingMode()) {
            drawingTool.createAndAddFigure(startPoint, endPoint);
        }
        drawingTool.redrawCanvas();
    }

    private void onMouseMoved(MouseEvent event) {
        Point eventPoint = new Point(event.getX(), event.getY());
        StringBuilder strB = new StringBuilder();
        for (Figure fig : canvasState.visibleFiguresAtPoint(eventPoint)) {
            strB.append(fig.toString());
        }
        if (strB.isEmpty()) {
            this.statusPane.updateStatus(eventPoint.toString());
        } else {
            this.statusPane.updateStatus(strB.toString());
        }

        drawingTool.redrawCanvas();
    }

    public void onMouseClicked(MouseEvent event) {
        Point eventPoint = new Point(event.getX(), event.getY());
        //single selection
            if (drawingTool.isSelectionOn() && (eventPoint.equals(startPoint))) {
            boolean found = false;
            drawingTool.getCanvasState().deselectFigures();
            StringBuilder label = new StringBuilder("Se seleccionó: ");
            for (Figure fig : drawingTool.getCanvasState().visibleFiguresAtPoint(eventPoint)) {
                label.append(fig.toString());
                statusPane.updateStatus(label.toString());
                drawingTool.getCanvasState().selectFigure(fig);
                found = true;
            }
            if (!found) {
                statusPane.updateStatus("Ninguna figura encontrada");
            }
        }
       /* if (paintPane.copiedFigure != null) {
            Figure figureToPasteFormatOnto = null;
            for (Figure figure : paintPane.canvasState.visibleFiguresAtPoint(eventPoint)) {
                figureToPasteFormatOnto = figure;
                //   figureToPasteFormatOnto.setColors(paintPane.copiedColors);
                break;
            }
            if (figureToPasteFormatOnto != null) {
                figureToPasteFormatOnto.setShadeType(paintPane.copiedFigure.getShadeType());
                figureToPasteFormatOnto.setHasBevel(paintPane.copiedFigure.getHasBevel());
                figureToPasteFormatOnto.setColors(paintPane.copiedColors);
                paintPane.redrawCanvas();
            }
            paintPane.copiedFigure = null;
        }*/
        drawingTool.redrawCanvas();

    }


    public void onMouseDragged(MouseEvent event) {
        Point eventPoint = new Point(event.getX(), event.getY());
        for (Figure fig : canvasState.selectedFigures()) {
            fig.move(eventPoint.add(selectionDragStartOffset.get(fig)));
        }
        drawingTool.redrawCanvas();
    }


}
