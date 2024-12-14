package frontend.Events;

import backend.CanvasState;
import backend.FigureBuilder;
import backend.FigureNotFoundException;
import backend.model.Circle;
import backend.model.Figure;
import backend.model.Shadow;
import frontend.DrawingTool;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import backend.CanvasState;
import backend.FigureNotFoundException;
import backend.model.*;
import frontend.Events.CanvasEvents;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LeftVBoxEvents extends VBox {

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

    public LeftVBoxEvents(DrawingTool drawingTool){

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

        selectionButton.setOnAction(e -> {
            if(!selectionButton.isSelected()) {
                drawingTool.getCanvasState().deselectFigures();
            }
        drawingTool.setBuilder(false);});
        //todo improve
        rectangleButton.setOnAction(e -> {
            if(rectangleButton.isSelected()){
                drawingTool.setBuilder(FigureBuilder.RECT);
            } else {
                drawingTool.setBuilder(false);
            }
                });
        squareButton.setOnAction(e -> {
            if(squareButton.isSelected()) {
                drawingTool.setBuilder(FigureBuilder.SQUARE);
            } else {
                drawingTool.setBuilder(false);
            }});
        circleButton.setOnAction(e -> {
            if(circleButton.isSelected()) {
                drawingTool.setBuilder(FigureBuilder.CIRCLE);
            } else {
                drawingTool.setBuilder(false);
            }});
        ellipseButton.setOnAction(e -> {
            if(ellipseButton.isSelected()) {
                drawingTool.setBuilder(FigureBuilder.ELLIPSE);
            } else {
                drawingTool.setBuilder(false);
            }});
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
                    drawingTool.setBevelOn(bevelCheckbox.isSelected());
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
