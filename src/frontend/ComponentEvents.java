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

*/
}