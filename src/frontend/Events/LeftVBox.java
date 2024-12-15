package frontend.Events;

import backend.FigureNotFoundException;
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
    //calculate shape gradient only from these color Picker
    final ColorPicker[] colorPickers = {fillColorPicker, fillSecondaryColorPicker};
    Button copyFormatButton = new Button("Copiar formato");


    private Figure copiedFigure;

    private final ToggleGroup tools = new ToggleGroup();

    public LeftVBox(DrawingTool drawingTool){

        super(10);
        this.setPadding(new Insets(5));
        this.setStyle("-fx-background-color: #999");
        this.setPrefWidth(100);

        ToggleButton[] toolsArr = {
                selectionButton, rectangleButton, circleButton,
                squareButton, ellipseButton, deleteButton
        };
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

        selectionButton.setOnAction(e-> drawingTool.setDrawingMode(DrawingMode.NONE));
        rectangleButton.setOnAction(e -> drawingTool.setDrawingMode(DrawingMode.RECT));
        squareButton.setOnAction(e -> drawingTool.setDrawingMode(DrawingMode.SQUARE));
        circleButton.setOnAction(e -> drawingTool.setDrawingMode(DrawingMode.CIRCLE));
        ellipseButton.setOnAction(e -> drawingTool.setDrawingMode(DrawingMode.ELLIPSE));
        tools.selectedToggleProperty().addListener((obs,oldToggle,newToggle) -> {
            if(newToggle == null) {
                drawingTool.setDrawingMode(DrawingMode.NONE);
            }
        });
     //   deleteButton.setOnAction(e -> canvasState.forEachSelectedFigure(canvasState.deleteFigure();));

        deleteButton.setOnAction(e -> {
            for(Figure fig : drawingTool.getCanvasState().selectedFigures()) {
                try {
                    drawingTool.getCanvasState().deleteFigure(fig, drawingTool.getCanvasState().getFigureLayer(fig));
                } catch (FigureNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
            drawingTool.redrawCanvas();
            });

        bevelCheckbox.setOnAction(e -> {
                    for (Figure fig : drawingTool.getCanvasState().selectedFigures()) {
                        fig.setHasBevel(bevelCheckbox.isSelected());
                    }
               //     drawingTool.setBevelOn(bevelCheckbox.isSelected());
                    drawingTool.redrawCanvas();
                });
        shadowTypeBox.setOnAction(e ->{
                for(Figure fig : drawingTool.getCanvasState().selectedFigures()) {
                    fig.setShadeType(shadowTypeBox.getValue());
                }
          //      drawingTool.setShadowType(shadowTypeBox.getValue());
                drawingTool.redrawCanvas();
        });
        //todo copy format (cast radial to lineargradient) and viceversa
        //todo copy not from first
        copyFormatButton.setOnAction(e -> {
            if(copiedFigure != null) {
                for (Figure fig : drawingTool.getCanvasState().selectedFigures()) {
                    fig.setHasBevel(bevelCheckbox.isSelected());
                    fig.setShadeType(shadowTypeBox.getValue());
                    drawingTool.setFigureColor(fig, drawingTool.getFigureColor(fig));
                }
                copiedFigure = null;
            }
            if(!drawingTool.getCanvasState().selectedFigures().isEmpty()){
                copiedFigure = drawingTool.getCanvasState().selectedFigures().getFirst();
            }
        });

        fillColorPicker.setOnAction(e -> {
            drawingTool.setStartColor(fillColorPicker.getValue());
        });
        fillSecondaryColorPicker.setOnAction(e -> {
            drawingTool.setEndColor(fillSecondaryColorPicker.getValue());
        });


    }

}
