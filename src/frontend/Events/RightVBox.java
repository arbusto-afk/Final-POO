package frontend.Events;

import backend.CanvasState;
import backend.FigureNotFoundException;
import backend.Layer;
import backend.Pair;
import frontend.DrawingTool.DrawingMode;
import frontend.DrawingTool.DrawingTool;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.layout.VBox;
import backend.model.*;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;

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

    CanvasState cs = drawingTool.getCanvasState();

/*
todo these events i dont know a clear separation of individual figures instances and canvas state
todo for example divide in cs, but turn in fig instance
    */
    turnButton.setOnAction(e-> {
        for(Figure fig : cs.selectedFigures()){
            fig.turnRight();
        }
        drawingTool.redrawCanvas();
    });

    flipHorizontalButton.setOnAction(e->{
        for(Figure fig : cs.selectedFigures()){
            fig.flipHorizontal();
        }
        drawingTool.redrawCanvas();
         });

    flipVerticalButton.setOnAction(e->{
        for(Figure fig : cs.selectedFigures()){
           fig.flipVertical();
        }
        drawingTool.redrawCanvas();
    });

    duplicateButton.setOnAction(e-> {
        for(Figure fig : cs.selectedFigures()){
            Figure dupl = fig.duplicate(cs.getDUPLICATEOFFSET());
            drawingTool.addFigure(dupl, drawingTool.getFigurePair(fig), 1);
        }
        drawingTool.redrawCanvas();
    } );

   divideButton.setOnAction(event -> {
       //todo
       for(Figure fig : cs.selectedFigures()){

          Pair<Figure,Figure> divpair = cs.divideFigure(fig, cs.getFigureLayer(fig));
            Integer layer = cs.getFigureLayer(fig);
            Pair<DrawingMode, Paint> figDrawingOptions = drawingTool.getFigurePair(fig);
    //        cs.deleteFigure(fig, layer);
            //drawingTool.deleteFigure(fig, layer);
           drawingTool.setFigurePair(divpair.getLeft(), figDrawingOptions);
           drawingTool.setFigurePair(divpair.getRight(), figDrawingOptions);
            //drawingTool.addFigure(divpair.getLeft(), figDrawingOptions, layer);
            //drawingTool.addFigure(divpair.getRight(), figDrawingOptions, layer);
           }
       drawingTool.redrawCanvas();
    });
}


}
