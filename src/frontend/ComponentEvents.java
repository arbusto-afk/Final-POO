package frontend;

import backend.CanvasState;
import backend.FigureNotFoundException;
import backend.model.*;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ComponentEvents {

    private final PaintPane paintPane;

    private final DrawingTool dt;
    private final CanvasState cs;

    public ComponentEvents(PaintPane paintPane, DrawingTool dt) {

        this.paintPane = paintPane;
        this.dt = dt;
        this.cs = paintPane.canvasState;
    }


    //todo fix
    public void onDeleteButtonClick(ActionEvent event) {
        cs.forEachSelectedFigure(fig -> {
            try {
                cs.deleteFigure(fig, 1);
            } catch (FigureNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void onCopyFormatButtonClick(ActionEvent event) {
       // if (paintPane.selectedFigure == null)
         //   return;
       // paintPane.copiedFigure = paintPane.selectedFigure;
       // paintPane.copiedColors = paintPane.selectedFigure.getColors();
    }
/*
    public void onTurnButtonClick(ActionEvent event) {
       cs.forEachSelectedFigure(Figure::turnRight);
       dt.redrawCanvas();
    }

    public void onFlipHorizontalButtonCLick(ActionEvent event) {
        cs.forEachSelectedFigure(Figure::flipHorizontal);
        dt.redrawCanvas();
    }

    public void onFlipVerticalButton(ActionEvent event) {
        cs.forEachSelectedFigure(Figure::flipVertical);
        dt.redrawCanvas();
    }

    public void onDuplicateButton(ActionEvent event) {
        cs.forEachSelectedFigure(figure -> figure.duplicate(15));
        dt.redrawCanvas();
    }

    //Todo fix
    public void onDivideButtonClick(ActionEvent event) throws FigureNotFoundException {
        cs.forEachSelectedFigure(fig -> {
            try {
                cs.divideFigure(fig, 1);
            } catch (FigureNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        dt.redrawCanvas();
    }/*

    public void showToggle(ActionEvent event) {
      /*  if (paintPane.showLayerRadioButton.isSelected()) {
            paintPane.canvasState.showLayer();
        }
        paintPane.redrawCanvas();
    }*/

    public void hideToggle(ActionEvent event) {
    /*    if (paintPane.hideLayerRadioButton.isSelected()) {
            paintPane.canvasState.hideLayer();
        }
        paintPane.redrawCanvas();
    */}

    public void createLayer(ActionEvent event) {
        createLayer();
    }

    public void createLayer() {
      /*  int newLayerNumber = paintPane.canvasState.getNextLayerNumber();
        String newLayerName = "Capa " + newLayerNumber;
        paintPane.canvasState.increaseNextLayerNumber();
        paintPane.layersChoiceBox.getItems().add(newLayerName);
        System.out.println(newLayerName);
        paintPane.canvasState.addLayer(newLayerName);
        paintPane.canvasState.changeLayer(newLayerName);
        paintPane.layersChoiceBox.setValue(newLayerName);
        paintPane.redrawCanvas();
    */}

    public void removeLayer(ActionEvent event) {
      /*  if(paintPane.canvasState.getWorkingLayer().compareTo("Capa 4") < 0){
            return;
        }
        String layerToDelete = paintPane.layersChoiceBox.getValue();
        System.out.println(layerToDelete);
        paintPane.layersChoiceBox.setValue("Capa 1");
        paintPane.layersChoiceBox.getItems().remove(layerToDelete);
        paintPane.canvasState.deleteLayer(layerToDelete);
        paintPane.canvasState.changeLayer("Capa 1");
        paintPane.redrawCanvas();
    */}


    public void onLayerSelection(ActionEvent event) {
      /*  paintPane.canvasState.changeLayer(paintPane.layersChoiceBox.getValue());
        updateRadioButtons();
        paintPane.redrawCanvas();
    */}

    public void updateRadioButtons(){
      /*  paintPane.hideLayerRadioButton.setSelected(!paintPane.canvasState.isVisible());
        paintPane.showLayerRadioButton.setSelected(paintPane.canvasState.isVisible());
    */}


    public void pushForward(ActionEvent event){
      /*  System.out.println("push to top");
        paintPane.canvasState.pushForward();
        paintPane.redrawCanvas();
    */}

    public void pushToBottom(ActionEvent event){
       /* System.out.println("push to bottom");
        paintPane.canvasState.pushToBottom();
        paintPane.redrawCanvas();
    */}
}