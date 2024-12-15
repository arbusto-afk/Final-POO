package frontend.Events;

import backend.CanvasState;
import backend.FigureNotFoundException;
import frontend.DrawingTool.DrawingTool;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.layout.VBox;
import backend.model.*;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class RightVBox extends VBox {


    final String rotateDtext = "Girar D";
    final String flipHtext =  "Voltear H";
    final String flipVtext = "Voltear V";
    final String duplicateText  = "Duplicar";
    final String divideText = "Dividir";

    Label actionLabel = new Label("Acciones");
    Button turnButton = new Button(rotateDtext);
    Button flipHorizontalButton = new Button(flipHtext);
    Button flipVerticalButton = new Button(flipVtext);
    Button duplicateButton = new Button(duplicateText);
    Button divideButton = new Button(divideText);


public RightVBox(DrawingTool drawingTool){
    super(10);

    setPadding(new Insets(5));
    setStyle("-fx-background-color: #999");
    setPrefWidth(100);

    Control[] rightControls = {
            actionLabel, turnButton,flipHorizontalButton,
            flipVerticalButton, duplicateButton,divideButton
    };
    this.getChildren().addAll(rightControls);

/*
todo these events i dont know a clear separation of individual figures instances and canvas state
todo for example divide in cs, but turn in fig instance
    */
    turnButton.setOnAction(e-> {
        CanvasState cs = drawingTool.getCanvasState();
        for(Figure fig : cs.selectedFigures()){
            fig.turnRight();
        }
        drawingTool.redrawCanvas();
    });

    flipHorizontalButton.setOnAction(e->{
        CanvasState cs = drawingTool.getCanvasState();
        for(Figure fig : cs.selectedFigures()){
            fig.flipHorizontal();
        }
        drawingTool.redrawCanvas();
         });

    flipVerticalButton.setOnAction(e->{
        CanvasState cs = drawingTool.getCanvasState();
        for(Figure fig : cs.selectedFigures()){
           fig.flipVertical();
        }
        drawingTool.redrawCanvas();
    });

    duplicateButton.setOnAction(e-> {
        //todo
        //todo Magic NUMBER
        CanvasState cs = drawingTool.getCanvasState();
        for(Figure fig : cs.selectedFigures()){
            cs.addFigure(fig.duplicate(15), cs.getFigureLayer(fig));
        }
        drawingTool.redrawCanvas();
    } );

   divideButton.setOnAction(event -> {
       //todo
       CanvasState cs = drawingTool.getCanvasState();
       for(Figure fig : cs.selectedFigures()){
          cs.divideFigure(fig, cs.getFigureLayer(fig));
       }
       drawingTool.redrawCanvas();

    });
}


}
