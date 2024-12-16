package frontend.Events;

import backend.CanvasState;
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
    final String flipHtext = "Voltear H";
    final String flipVtext = "Voltear V";
    final String duplicateText = "Duplicar";
    final String divideText = "Dividir";
    final String groupText = "Agrupar";
    final String ungroupText = "Desagrupar";

    Label actionLabel = new Label("Acciones");
    Button turnButton = new Button(rotateDtext);
    Button flipHorizontalButton = new Button(flipHtext);
    Button flipVerticalButton = new Button(flipVtext);
    Button duplicateButton = new Button(duplicateText);
    Button divideButton = new Button(divideText);
    Button groupButton = new Button(groupText);
    Button ungroupButton = new Button(ungroupText);


    public RightVBox(DrawingTool drawingTool) {
        super(10);

        setPadding(new Insets(5));
        setStyle("-fx-background-color: #999");
        setPrefWidth(100);

        Control[] rightControls = {
                actionLabel, turnButton, flipHorizontalButton,
                flipVerticalButton, duplicateButton, divideButton, groupButton, ungroupButton
        };
        this.getChildren().addAll(rightControls);

        CanvasState cs = drawingTool.getCanvasState();


        turnButton.setOnAction(e -> {
            for (Figure fig : cs.selectedFigures()) {
                fig.turnRight();
            }
            drawingTool.redrawCanvas();
        });
        flipHorizontalButton.setOnAction(e -> {
            for (Figure fig : cs.selectedFigures()) {
                fig.flipHorizontal();
            }
            drawingTool.redrawCanvas();
        });
        flipVerticalButton.setOnAction(e -> {
            for (Figure fig : cs.selectedFigures()) {
                fig.flipVertical();
            }
            drawingTool.redrawCanvas();
        });
        duplicateButton.setOnAction(e -> {
            for (Figure fig : cs.selectedFigures()) {
                Figure dupl = fig.duplicate(cs.getDUPLICATEOFFSET());
                drawingTool.addFigure(dupl, drawingTool.getFigurePair(fig));
            }
            drawingTool.redrawCanvas();
        });
        divideButton.setOnAction(event -> {

            for (Figure fig : cs.selectedFigures()) {
                Pair<DrawingMode, Paint> figDrawingOptions = drawingTool.getFigurePair(fig);
                Pair<Figure, Figure> divpair = cs.divideFigure(fig, cs.getFigureLayer(fig));
                //Integer layer = cs.getFigureLayer(fig);
                drawingTool.setFigureDrawingOptionsPair(divpair.getLeft(), figDrawingOptions);
                drawingTool.setFigureDrawingOptionsPair(divpair.getRight(), figDrawingOptions);
            }
            drawingTool.redrawCanvas();
        });
        groupButton.setOnAction(e -> {
            cs.groupFigures(cs.selectedFigures());
        });
        ungroupButton.setOnAction(e -> {
            cs.ungroupFigures(cs.selectedFigures());
        });
    }


}
