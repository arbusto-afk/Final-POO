package frontend.Events;

import backend.CanvasState;
import backend.model.Figure;
import backend.model.Point;
import frontend.DrawingTool;
import frontend.StatusPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

public class CanvasEvents {

    private final CanvasState canvasState;
    private final DrawingTool drawingTool;
    private Point startPoint;
    private Point selectionDragStartOffset;

    public CanvasEvents(CanvasState canvasState, DrawingTool drawingTool){
        this.canvasState = canvasState;
        this.drawingTool = drawingTool;

    }

    public void setupCanvas(Canvas c){
        c.setOnMousePressed(this::onMousePressed);
        c.setOnMouseReleased(this::onMouseRelease);
        c.setOnMouseMoved(this::onMouseRelease);
        c.setOnMouseClicked(this::onMouseClicked);
        c.setOnDragDetected(this::onMouseDragged);
    }

    private void onMousePressed(MouseEvent event) {
        Point eventPoint = new Point(event.getX(), event.getY());
        startPoint = eventPoint;
        if (!canvasState.selectedFigures().isEmpty() && selectionDragStartOffset == null) {
            selectionDragStartOffset = canvasState.selectedFigures().getFirst().getCenterPoint().getDifference(eventPoint);
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
        drawingTool.drawFigure(startPoint, endPoint);
        for(Figure fig : canvasState.visibleFigures()){
            Point topL = fig.getCenterPoint().add(fig.getWidth() / 2, fig.getHeight() / 2);
            Point botR = topL.add(fig.getWidth(), fig.getHeight());
            if(topL.isInRect(startPoint, endPoint) && botR.isInRect(startPoint, endPoint)){
                canvasState.selectFigure(fig);
            }
        }
        startPoint = null;
        drawingTool.redrawCanvas();
    }
    //TODO
    private void onMouseMoved(MouseEvent event) {
     /*   Point eventPoint = new Point(event.getX(), event.getY());
        StringBuilder strB = new StringBuilder();
        for (Figure fig : canvasState.visibleFiguresAtPoint(eventPoint)) {
            strB.append(fig.toString());
        }
        StatusPane sPane = (StatusPane) event.getSource();
        if (strB.isEmpty()) {
            sPane.updateStatus(eventPoint.toString());
        } else {
            sPane.updateStatus(strB.toString());
        }*/
    }

    public void onMouseClicked(MouseEvent event) {
        Point eventPoint = new Point(event.getX(), event.getY());
      /*  if (paintPane.selectionButton.isSelected()) {
            boolean found = false;
            StringBuilder label = new StringBuilder("Se seleccionó: ");
            for (Figure fig : paintPane.canvasState.visibleFiguresAtPoint(eventPoint)) {
                label.append(fig.toString());
                paintPane.selectedFigure = fig;
                found = true;
            }
            if (found) {
                paintPane.statusPane.updateStatus(label.toString());
            } else {
                paintPane.statusPane.updateStatus("Ninguna figura encontrada");
                paintPane.selectedFigure = null;
            }
            paintPane.redrawCanvas();
        }
        if (paintPane.copiedFigure != null) {
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
    }


    //todo not move only first
    public void onMouseDragged(MouseEvent event) {
        Point eventPoint = new Point(event.getX(), event.getY());
       /* if(!canvasState.selectedFigures().isEmpty()){
            for(Figure fig : canvasState.selectedFigures()){
                Point newCenter = eventPoint;
                fig.move(newCenter);
            }
        }*/
        if (!canvasState.selectedFigures().isEmpty()) {
            canvasState.selectedFigures().getFirst().move(eventPoint.add(selectionDragStartOffset));
            drawingTool.redrawCanvas();
        }
    }


}
