package frontend;

import backend.CanvasState;
import backend.FigureNotFoundException;
import backend.model.*;
import frontend.Events.CanvasEvents;
import frontend.Events.LeftVBoxEvents;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ComponentInitializer {

    public void initializePaintPane(PaintPane paintPane, CanvasState canvasState, StatusPane statusPane, DrawingTool dt) {
        ComponentEvents componentEvents = new ComponentEvents(paintPane, dt);
/*
        // Initialize ToggleButtons
        ToggleButton[] toolsArr = {
                paintPane.selectionButton, paintPane.rectangleButton, paintPane.circleButton,
                paintPane.squareButton, paintPane.ellipseButton, paintPane.deleteButton
        };
        for (ToggleButton tool : toolsArr) {
            tool.setMinWidth(90);
            tool.setToggleGroup(paintPane.tools);
            tool.setCursor(Cursor.HAND);
        }*/

        // Set up left buttons box
    //    VBox leftButtonsBox = new VBox(10);
        VBox rightButtonsBox = new VBox(10);
        HBox topButtonsBox = new HBox(10);
    /*    Control[] leftControls = {
                paintPane.bevelCheckbox, paintPane.formatLabel, paintPane.shadowTypeBox,
                paintPane.fillColorPicker, paintPane.fillSecondaryColorPicker, paintPane.copyFormatButton
        };*/
        Control[] rightControls = {
                paintPane.actionLabel, paintPane.turnButton, paintPane.flipHorizontalButton,
                paintPane.flipVerticalButton, paintPane.duplicateButton, paintPane.divideButton
        };
        Control[] topControls = {
                paintPane.pushForwardButton, paintPane.pushToBottomButton, paintPane.layerLabel,
                paintPane.layersChoiceBox, paintPane.showLayerRadioButton, paintPane.hideLayerRadioButton,
                paintPane.addLayerButton, paintPane.removeLayerButton
        };

        topButtonsBox.getChildren().addAll(topControls);
        rightButtonsBox.getChildren().addAll(rightControls);
       //leftButtonsBox.getChildren().addAll(toolsArr);
        //leftButtonsBox.getChildren().addAll(leftControls);



        // Style and position boxes
        topButtonsBox.setPadding(new Insets(5));
        topButtonsBox.setStyle("-fx-background-color: #999");
        topButtonsBox.setPrefWidth(100);
        topButtonsBox.setAlignment(Pos.CENTER);


/*
        leftButtonsBox.setPadding(new Insets(5));
        leftButtonsBox.setStyle("-fx-background-color: #999");
        leftButtonsBox.setPrefWidth(100);
*/
        rightButtonsBox.setPadding(new Insets(5));
        rightButtonsBox.setStyle("-fx-background-color: #999");
        rightButtonsBox.setPrefWidth(100);

        // Set up radio buttons for show/hide layer toggle
        paintPane.showLayerRadioButton.setToggleGroup(paintPane.showHideToggle);
        paintPane.hideLayerRadioButton.setToggleGroup(paintPane.showHideToggle);
        // Set up events
     //   paintPane.shadowTypeBox.setOnAction(componentEvents::onShadowChoiceBoxSelection);

        CanvasEvents ce = new CanvasEvents(canvasState, dt);
        ce.setupCanvas(paintPane.canvas);



       // paintPane.deleteButton.setOnAction(componentEvents::onDeleteButtonClick);
        paintPane.copyFormatButton.setOnAction(componentEvents::onCopyFormatButtonClick);
        paintPane.turnButton.setOnAction(componentEvents::onTurnButtonClick);
        paintPane.flipHorizontalButton.setOnAction(componentEvents::onFlipHorizontalButtonCLick);
        paintPane.flipVerticalButton.setOnAction(componentEvents::onFlipVerticalButton);
        paintPane.duplicateButton.setOnAction(componentEvents::onDuplicateButton);
        paintPane.divideButton.setOnAction(event -> {
            try {
                componentEvents.onDivideButtonClick(event);
            } catch (FigureNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        paintPane.layersChoiceBox.setOnAction(componentEvents::onLayerSelection);
        paintPane.showLayerRadioButton.setOnAction(componentEvents::showToggle);
        paintPane.hideLayerRadioButton.setOnAction(componentEvents::hideToggle);
        paintPane.addLayerButton.setOnAction(componentEvents::createLayer);
        paintPane.removeLayerButton.setOnAction(componentEvents::removeLayer);
        paintPane.pushForwardButton.setOnAction(componentEvents::pushForward);
        paintPane.pushToBottomButton.setOnAction(componentEvents::pushToBottom);


        // Set up layout
     //   paintPane.setLeft(leftButtonsBox);
        paintPane.setTop(topButtonsBox);
        paintPane.setCenter(paintPane.canvas);
        paintPane.setRight(rightButtonsBox);


        // Creates the first 3 layers
        componentEvents.createLayer();
        componentEvents.createLayer();
        componentEvents.createLayer();
        paintPane.layersChoiceBox.setValue("Capa 1");

        // Set up Hide and Show Layer Radio Buttons
        componentEvents.updateRadioButtons();
    }
}
