package frontend;

import backend.model.*;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ComponentEvents {

    private final PaintPane paintPane;

    public ComponentEvents(PaintPane paintPane) {
        this.paintPane = paintPane;
    }

    public void onMousePressed(MouseEvent event) {
        paintPane.startPoint = new Point(event.getX(), event.getY());
        Point eventPoint = new Point(event.getX(), event.getY());
        if (paintPane.selectedFigure != null) {
            paintPane.selectionDragStartOffset = paintPane.selectedFigure.getCenterPoint().getDifference(eventPoint);
        }
    }

    public void onMouseRelease(MouseEvent event) {
        if (paintPane.startPoint == null) {
            return;
        }
        Point endPoint = new Point(event.getX(), event.getY());
        if (endPoint.getX() < paintPane.startPoint.getX() || endPoint.getY() < paintPane.startPoint.getY()) {
            return;
        }

        Figure newFigure;
        ToggleButton b = (ToggleButton) (paintPane.tools.getSelectedToggle());
        if (b != null) {
            List<Color> colors = colorListFromColorPickerArr(paintPane.colorPickers);
            Shadow shadowType = paintPane.shadowTypeBox.getValue();
            boolean hasBevel = paintPane.bevelCheckbox.isSelected();

            switch (b.getText()) {
                case PaintPane.squareText:
                    double size = Math.abs(endPoint.getX() - paintPane.startPoint.getX());
                    newFigure = new Square(paintPane.startPoint, size, shadowType, hasBevel);
                    break;
                case PaintPane.rectangleText:
                    newFigure = new Rectangle(paintPane.startPoint, endPoint, shadowType, hasBevel);
                    break;
                case PaintPane.circleText:
                    double circleRadius = Math.abs(endPoint.getX() - paintPane.startPoint.getX());
                    newFigure = new Circle(paintPane.startPoint, circleRadius, shadowType, hasBevel);
                    break;
                case PaintPane.ellipseText:
                    Point centerPoint = new Point(
                            Math.abs(endPoint.x + paintPane.startPoint.x) / 2,
                            Math.abs(endPoint.y + paintPane.startPoint.y) / 2
                    );
                    double sMajorAxis = Math.abs(endPoint.x - paintPane.startPoint.x);
                    double sMinorAxis = Math.abs(endPoint.y - paintPane.startPoint.y);
                    newFigure = new Ellipse(centerPoint, sMajorAxis, sMinorAxis, shadowType, hasBevel);
                    break;
                default:
                    return;
            }
            newFigure.setColors(colors);
            newFigure.setHasBevel(paintPane.bevelCheckbox.isSelected());
            paintPane.canvasState.addFigure(newFigure);

            paintPane.startPoint = null;
            paintPane.selectionDragStartOffset = null;
            paintPane.redrawCanvas();
        }
    }

    public void onMouseMoved(MouseEvent event) {
        Point eventPoint = new Point(event.getX(), event.getY());
        StringBuilder strB = new StringBuilder();
        for (Figure fig : paintPane.canvasState.figuresAtPoint(eventPoint)) {
            strB.append(fig.toString());
        }
        if (strB.isEmpty()) {
            paintPane.statusPane.updateStatus(eventPoint.toString());
        } else {
            paintPane.statusPane.updateStatus(strB.toString());
        }
    }

    public void onMouseClicked(MouseEvent event) {
        Point eventPoint = new Point(event.getX(), event.getY());
        if (paintPane.selectionButton.isSelected()) {
            boolean found = false;
            StringBuilder label = new StringBuilder("Se seleccionó: ");
            for (Figure fig : paintPane.canvasState.figuresAtPoint(eventPoint)) {
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
            for (Figure figure : paintPane.canvasState.figuresAtPoint(eventPoint)) {
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
        }
    }

    public void onMouseDragged(MouseEvent event) {
        Point eventPoint = new Point(event.getX(), event.getY());
        if (paintPane.selectionButton.isSelected() && paintPane.selectedFigure != null) {
            paintPane.selectedFigure.move(eventPoint.add(paintPane.selectionDragStartOffset));
            paintPane.redrawCanvas();
        }
    }

    public void onChoiceBoxSelection(ActionEvent event) {
        if (paintPane.selectedFigure != null) {
            paintPane.selectedFigure.setShadeType(paintPane.shadowTypeBox.getValue());
            paintPane.redrawCanvas();
        }
    }

    public void onDeleteButtonClick(ActionEvent event) {
        if (paintPane.selectedFigure != null) {
            paintPane.canvasState.deleteFigure(paintPane.selectedFigure);
            paintPane.selectedFigure = null;
            paintPane.redrawCanvas();
        }
    }

    public void onCopyFormatButtonClick(ActionEvent event) {
        if (paintPane.selectedFigure == null)
            return;
        paintPane.copiedFigure = paintPane.selectedFigure;
        paintPane.copiedColors = paintPane.selectedFigure.getColors();
    }

    public void onTurnButtonClick(ActionEvent event) {
        if (paintPane.selectedFigure != null) {
            paintPane.selectedFigure.turnRight();
            paintPane.redrawCanvas();
        }
    }

    public void onFlipHorizontalButtonCLick(ActionEvent event) {
        if (paintPane.selectedFigure != null) {
            paintPane.selectedFigure.flipHorizontal();
            paintPane.redrawCanvas();
        }
    }

    public void onFlipVerticalButton(ActionEvent event) {
        if (paintPane.selectedFigure != null) {
            paintPane.selectedFigure.flipVertical();
            paintPane.redrawCanvas();
        }
    }

    public void onDuplicateButton(ActionEvent event) {
        if (paintPane.selectedFigure != null) {
            Figure duplicate = paintPane.selectedFigure.duplicate();
            duplicate.setColors(paintPane.selectedFigure.getColors());
            paintPane.canvasState.addFigure(duplicate);
            paintPane.redrawCanvas();
        }
    }

    public void onDivideButtonClick(ActionEvent event) {
        if (paintPane.selectedFigure == null)
            return;
        paintPane.canvasState.divideFigure(paintPane.selectedFigure);
        paintPane.redrawCanvas();
    }

    private List<Color> colorListFromColorPickerArr(ColorPicker[] arr) {
        List<Color> res = new ArrayList<>();
        for (ColorPicker p : arr) {
            res.add(p.getValue());
        }
        return res;
    }

    public void showToggle(ActionEvent event) {
        if (paintPane.showLayerRadioButton.isSelected()) {
            paintPane.canvasState.showLayer();
        }
        paintPane.redrawCanvas();
    }

    public void hideToggle(ActionEvent event) {
        if (paintPane.hideLayerRadioButton.isSelected()) {
            paintPane.canvasState.hideLayer();
        }
        paintPane.redrawCanvas();
    }

    public void createLayer(ActionEvent event) {
        createLayer();
    }

    public void createLayer() {
        int newLayerNumber = paintPane.canvasState.getNextLayerNumber();
        String newLayerName = "Capa " + newLayerNumber;
        paintPane.canvasState.increaseNextLayerNumber();
        paintPane.layersChoiceBox.getItems().add(newLayerName);
        System.out.println(newLayerName);
        paintPane.canvasState.addLayer(newLayerName);
        paintPane.canvasState.changeLayer(newLayerName);
        paintPane.layersChoiceBox.setValue(newLayerName);
        paintPane.redrawCanvas();
    }

    public void removeLayer(ActionEvent event) {
        if(paintPane.canvasState.getWorkingLayer().compareTo("Capa 4") < 0){
            return;
        }
        String layerToDelete = paintPane.layersChoiceBox.getValue();
        System.out.println(layerToDelete);
        paintPane.layersChoiceBox.setValue("Capa 1");
        paintPane.layersChoiceBox.getItems().remove(layerToDelete);
        paintPane.canvasState.deleteLayer(layerToDelete);
        paintPane.canvasState.changeLayer("Capa 1");
        paintPane.redrawCanvas();
    }


    public void onLayerSelection(ActionEvent event) {
        paintPane.canvasState.changeLayer(paintPane.layersChoiceBox.getValue());
        updateRadioButtons();
        paintPane.redrawCanvas();
    }

    public void updateRadioButtons(){
        paintPane.hideLayerRadioButton.setSelected(!paintPane.canvasState.isVisible());
        paintPane.showLayerRadioButton.setSelected(paintPane.canvasState.isVisible());
    }


    public void pushForward(ActionEvent event){
        System.out.println("push to top");
        paintPane.canvasState.pushForward();
        paintPane.redrawCanvas();
    }

    public void pushToBottom(ActionEvent event){
        System.out.println("push to bottom");
        paintPane.canvasState.pushToBottom();
        paintPane.redrawCanvas();
    }
}