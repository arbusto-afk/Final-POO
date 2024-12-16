package frontend.Events;
import backend.model.Figure;
import backend.model.Shadow;
import frontend.DrawingTool.DrawingMode;
import frontend.DrawingTool.DrawingTool;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.geometry.Insets;

public class LeftVBox extends VBox {

    Color defaultFillColor = Color.YELLOW;
    Color defaultSecondaryFillColor = Color.ORANGE;

    final String rectangleText = "Rectángulo";
    final String squareText = "Cuadrado";
    final String circleText = "Círculo";
    final String ellipseText = "Elipse";
    ToggleButton selectionButton = new ToggleButton("Seleccionar");
    ToggleButton rectangleButton = new ToggleButton(rectangleText);
    ToggleButton circleButton = new ToggleButton(circleText);
    ToggleButton squareButton = new ToggleButton(squareText);
    ToggleButton ellipseButton = new ToggleButton(ellipseText);
    ToggleButton deleteButton = new ToggleButton("Borrar");
    Label formatLabel = new Label("Formato:");
    ChoiceBox<Shadow> shadowTypeBox = new ChoiceBox<>();
    CheckBox bevelCheckbox = new CheckBox("Biselado");
    ColorPicker fillColorPicker = new ColorPicker(defaultFillColor);
    ColorPicker fillSecondaryColorPicker = new ColorPicker(defaultSecondaryFillColor);
    Button copyFormatButton = new Button("Copiar formato");


    private Figure copiedFigure;

    public LeftVBox(DrawingTool drawingTool){

        super(10);
        this.setPadding(new Insets(5));
        this.setStyle("-fx-background-color: #999");
        this.setPrefWidth(100);

        ToggleButton[] toolsArr = {
                selectionButton, rectangleButton, circleButton,
                squareButton, ellipseButton, deleteButton
        };
        ToggleGroup tools = new ToggleGroup();
        for (ToggleButton tool : toolsArr) {
            tool.setToggleGroup(tools);
            tool.setCursor(Cursor.HAND);
        }
        VBox leftButtonsBox = this;
        Control[] controls = {
                bevelCheckbox, formatLabel, shadowTypeBox,
                fillColorPicker, fillSecondaryColorPicker, copyFormatButton
        };
        leftButtonsBox.getChildren().addAll(toolsArr);
        leftButtonsBox.getChildren().addAll(controls);


        // Set up choice box
        shadowTypeBox.setValue(Shadow.NONE);
        shadowTypeBox.getItems().addAll(Shadow.values());

        selectionButton.setOnAction(e-> drawingTool.setSelectionOn(selectionButton.isSelected()));
        rectangleButton.setOnAction(e -> drawingTool.setDrawingMode(DrawingMode.RECT));
        squareButton.setOnAction(e -> drawingTool.setDrawingMode(DrawingMode.SQUARE));
        circleButton.setOnAction(e -> drawingTool.setDrawingMode(DrawingMode.CIRCLE));
        ellipseButton.setOnAction(e -> drawingTool.setDrawingMode(DrawingMode.ELLIPSE));
        tools.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if(newToggle == null) {
                drawingTool.setUseDrawingMode(false);
            }
            drawingTool.setSelectionOn(selectionButton.isSelected());
        });


        deleteButton.setOnAction(e -> {
            for(Figure fig : drawingTool.getCanvasState().selectedFigures()) {
                    drawingTool.getCanvasState().deleteFigure(fig, drawingTool.getCanvasState().getFigureLayer(fig));
            }
            drawingTool.getCanvasState().deselectFigures();
            drawingTool.redrawCanvas();
            });

        bevelCheckbox.setOnAction(e -> {
                    for (Figure fig : drawingTool.getCanvasState().selectedFigures()) {
                        fig.setHasBevel(bevelCheckbox.isSelected());
                    }
                    drawingTool.setBevel(bevelCheckbox.isSelected());
                    drawingTool.redrawCanvas();
                });
        shadowTypeBox.setOnAction(e ->{
                for(Figure fig : drawingTool.getCanvasState().selectedFigures()) {
                    fig.setShadeType(shadowTypeBox.getValue());
                }
                drawingTool.setShadowType(shadowTypeBox.getValue());
                drawingTool.redrawCanvas();
        });
        //todo copy format (cast radial to lineargradient) and viceversa
        //todo copy not from first
        copyFormatButton.setOnAction(e -> {
            if(copiedFigure != null) {
                for (Figure fig : drawingTool.getCanvasState().selectedFigures()) {
                    fig.setHasBevel(copiedFigure.getHasBevel());
                    fig.setShadeType(copiedFigure.getShadeType());
                    //todo fix this
                 //   drawingTool.setFigureColor(fig, Color.PINK);
                }
                copiedFigure = null;
            }

            //todo here what to do hay que definir como interpretar este caso particular
        /*    if(!drawingTool.getCanvasState().selectedFigures().isEmpty()){
                copiedFigure = drawingTool.getCanvasState().selectedFigures().getFirst();
            }*/
        });

        fillColorPicker.setOnAction(e -> {
            drawingTool.setStartColor(fillColorPicker.getValue());
            for (Figure fig : drawingTool.getCanvasState().selectedFigures()) {
                drawingTool.setExistingFigureColor(fig, drawingTool.getGradientForFigure(fig, drawingTool.getStartColor(), drawingTool.getEndColor()));
            }
            drawingTool.redrawCanvas();
        });
        fillSecondaryColorPicker.setOnAction(e -> {
            drawingTool.setEndColor(fillSecondaryColorPicker.getValue());
            for (Figure fig : drawingTool.getCanvasState().selectedFigures()) {
                drawingTool.setExistingFigureColor(fig, drawingTool.getGradientForFigure(fig, drawingTool.getStartColor(), drawingTool.getEndColor()));
            }
            drawingTool.redrawCanvas();
        });


    }

}
