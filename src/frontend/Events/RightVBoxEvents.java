package frontend.Events;

import backend.CanvasState;
import backend.FigureNotFoundException;
import frontend.DrawingTool;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.layout.VBox;
import backend.model.*;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class RightVBoxEvents extends VBox {

    private  DrawingTool dt;
    private  CanvasState cs;

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


public RightVBoxEvents(DrawingTool drawingTool){
    super(10);

    setPadding(new Insets(5));
    setStyle("-fx-background-color: #999");
    setPrefWidth(100);


     actionLabel = new Label("Acciones");

    Control[] rightControls = {
            actionLabel, turnButton,flipHorizontalButton,
            flipVerticalButton, duplicateButton,divideButton
    };
    getChildren().add(actionLabel);
    getChildren().addAll(rightControls);


    turnButton.setOnAction(e-> {
            cs.forEachSelectedFigure(Figure::turnRight);
    dt.redrawCanvas();} );

    flipHorizontalButton.setOnAction(e->{ cs.forEachSelectedFigure(Figure::flipHorizontal);
        dt.redrawCanvas(); });

    flipVerticalButton.setOnAction(e->{cs.forEachSelectedFigure(Figure::flipVertical);
        dt.redrawCanvas();} );

    duplicateButton.setOnAction(e-> { cs.forEachSelectedFigure(figure -> figure.duplicate(15));
        dt.redrawCanvas();} );

   divideButton.setOnAction(event -> {
       cs.forEachSelectedFigure(fig -> {
           try {
               cs.divideFigure(fig, 1);
           } catch (FigureNotFoundException e) {
               throw new RuntimeException(e);
           }
       });
       dt.redrawCanvas();
    });
}


}
