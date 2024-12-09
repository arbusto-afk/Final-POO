package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class compInitializer {

    public static void initializePaintPane(PaintPane paintPane, CanvasState canvasState, StatusPane statusPane) {
        PaintPaneEvents paintPaneEvents = new PaintPaneEvents(paintPane);

        // Initialize ToggleButtons
        ToggleButton[] toolsArr = {
                paintPane.selectionButton, paintPane.rectangleButton, paintPane.circleButton,
                paintPane.squareButton, paintPane.ellipseButton, paintPane.deleteButton
        };
        for (ToggleButton tool : toolsArr) {
            tool.setMinWidth(90);
            tool.setToggleGroup(paintPane.tools);
            tool.setCursor(Cursor.HAND);
        }

        // Set up left buttons box
        VBox leftButtonsBox = new VBox(10);
        VBox rightButtonsBox = new VBox(10);
        HBox topButtonsBox = new HBox(10);
        Control[] leftControls = {
                paintPane.bevelCheckbox, paintPane.formatLabel, paintPane.shadowTypeBox,
                paintPane.fillColorPicker, paintPane.fillSecondaryColorPicker, paintPane.copyFormatButton
        };
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
        leftButtonsBox.getChildren().addAll(toolsArr);
        leftButtonsBox.getChildren().addAll(leftControls);

        // Set up choice box
        paintPane.shadowTypeBox.setValue(Shadow.NONE);
        paintPane.shadowTypeBox.getItems().addAll(Shadow.values());

        // Set up Hide and Show Layer Radio Buttons
        paintPaneEvents.updateRadioButtons();

        // Style and position boxes
        topButtonsBox.setPadding(new Insets(5));
        topButtonsBox.setStyle("-fx-background-color: #999");
        topButtonsBox.setPrefWidth(100);
        topButtonsBox.setAlignment(Pos.CENTER);

        leftButtonsBox.setPadding(new Insets(5));
        leftButtonsBox.setStyle("-fx-background-color: #999");
        leftButtonsBox.setPrefWidth(100);

        rightButtonsBox.setPadding(new Insets(5));
        rightButtonsBox.setStyle("-fx-background-color: #999");
        rightButtonsBox.setPrefWidth(100);

        // Set up events
        paintPane.shadowTypeBox.setOnAction(paintPaneEvents::onChoiceBoxSelection);
        paintPane.canvas.setOnMousePressed(paintPaneEvents::onMousePressed);
        paintPane.canvas.setOnMouseReleased(paintPaneEvents::onMouseRelease);
        paintPane.canvas.setOnMouseMoved(paintPaneEvents::onMouseMoved);
        paintPane.canvas.setOnMouseClicked(paintPaneEvents::onMouseClicked);
        paintPane.canvas.setOnMouseDragged(paintPaneEvents::onMouseDragged);
        paintPane.deleteButton.setOnAction(paintPaneEvents::onDeleteButtonClick);
        paintPane.copyFormatButton.setOnAction(paintPaneEvents::onCopyFormatButtonClick);
        paintPane.turnButton.setOnAction(paintPaneEvents::onTurnButtonClick);
        paintPane.flipHorizontalButton.setOnAction(paintPaneEvents::onFlipHorizontalButtonCLick);
        paintPane.flipVerticalButton.setOnAction(paintPaneEvents::onFlipVerticalButton);
        paintPane.duplicateButton.setOnAction(paintPaneEvents::onDuplicateButton);
        paintPane.divideButton.setOnAction(paintPaneEvents::onDivideButtonClick);
        paintPane.showLayerRadioButton.setOnAction(paintPaneEvents::showToggle);
        paintPane.hideLayerRadioButton.setOnAction(paintPaneEvents::hideToggle);
        paintPane.addLayerButton.setOnAction(paintPaneEvents::createLayer);
        paintPane.removeLayerButton.setOnAction(paintPaneEvents::removeLayer);


        // Set up layout
        paintPane.setLeft(leftButtonsBox);
        paintPane.setTop(topButtonsBox);
        paintPane.setCenter(paintPane.canvas);
        paintPane.setRight(rightButtonsBox);

        paintPane.showLayerRadioButton.setToggleGroup(paintPane.showHideToggle);
        paintPane.hideLayerRadioButton.setToggleGroup(paintPane.showHideToggle);

        // Creates the first 3 layers
        paintPaneEvents.createLayer();
        paintPaneEvents.createLayer();
        paintPaneEvents.createLayer();
        paintPane.layersChoiceBox.setValue("Capa 1");
    }
}
