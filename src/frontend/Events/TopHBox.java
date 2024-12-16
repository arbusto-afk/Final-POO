package frontend.Events;

import backend.CanvasState;
import frontend.DrawingTool.DrawingTool;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class TopHBox extends HBox {

    final private DrawingTool dt;
    final private  CanvasState cs;

    final String pushForwardButtonText = "Traer al frente";
    final String pushToBottomButtonText = "Enviar al fondo";
    final String layerLabelText = "Capas";
    final String showLayerRadioButtonText = "Mostrar";
    final String hideLayerRadioButtonText = "Ocultar";
    final String addLayerButtonText = "Agregar capa";
    final String removeLayerButtonText = "Eliminar capa";


    Button pushForwardButton = new Button (pushForwardButtonText);
    Button pushToBottomButton = new Button (pushToBottomButtonText);
    Label layerLabel = new Label(layerLabelText);
    ChoiceBox<String> layersChoiceBox = new ChoiceBox<>();
    RadioButton showLayerRadioButton = new RadioButton(showLayerRadioButtonText);
    RadioButton hideLayerRadioButton = new RadioButton(hideLayerRadioButtonText);
    ToggleGroup showHideToggle = new ToggleGroup();	// Para que solo se pueda seleccionar uno
    Button addLayerButton = new Button(addLayerButtonText);
    Button removeLayerButton = new Button(removeLayerButtonText);


    public TopHBox(int inset, DrawingTool dt) {
        super(inset);

        this.dt = dt;
        this.cs = dt.getCanvasState();

        // Style
        setPadding(new Insets(5));
        setStyle("-fx-background-color: #999");
        setPrefWidth(100);
        setAlignment(Pos.CENTER);

        // Controls
        Control[] topControls = {
                pushForwardButton, pushToBottomButton, layerLabel,
                layersChoiceBox, showLayerRadioButton, hideLayerRadioButton,
                addLayerButton, removeLayerButton
        };
        getChildren().addAll(topControls);

        // Set up Hide and Show Layer Radio Buttons Toggle Groups
        showLayerRadioButton.setToggleGroup(showHideToggle);
        hideLayerRadioButton.setToggleGroup(showHideToggle);


        layersChoiceBox.setOnAction(this::onLayerSelection);
        showLayerRadioButton.setOnAction(this::showToggle);
        hideLayerRadioButton.setOnAction(this::hideToggle);
        addLayerButton.setOnAction(this::createLayer);
        removeLayerButton.setOnAction(this::removeLayer);
        pushForwardButton.setOnAction(this::pushFigureForward);
        pushToBottomButton.setOnAction(this::pushFigureToBottom);


        // Creates the first 3 layers
        layersChoiceBox.getItems().addAll("Capa 1","Capa 2", "Capa 3");
        layersChoiceBox.setValue("Capa 1");

        // Set up Hide and Show Layer Radio Buttons
        updateRadioButtons();
    }

    private void createLayer(ActionEvent event) {
        createLayer();
    }

    private void createLayer() {
        String newLayerName = cs.initializeNewLayer();
        layersChoiceBox.getItems().add(newLayerName);
        System.out.println(newLayerName);
        layersChoiceBox.setValue(newLayerName);
        dt.redrawCanvas();
    }


    private void removeLayer(ActionEvent event) {
        if(layersChoiceBox.getValue().compareTo("Capa 4") < 0)
            return;

        String layerToDelete = layersChoiceBox.getValue();
        System.out.println(layerToDelete);
        layersChoiceBox.setValue("Capa 1");
        layersChoiceBox.getItems().remove(layerToDelete);
        cs.deleteLayer(layerToDelete);
        dt.redrawCanvas();
    }

    private void onLayerSelection(ActionEvent event) {
        updateRadioButtons();
        cs.setWorkingLayer(layersChoiceBox.getValue());
    }

    private void showToggle(ActionEvent event) {
        if(showLayerRadioButton.isSelected()) {
            cs.showLayer(layersChoiceBox.getValue());
        }
        dt.redrawCanvas();
    }

    private void hideToggle(ActionEvent event) {
        if(hideLayerRadioButton.isSelected()) {
            cs.hideLayer(layersChoiceBox.getValue()) ;
        }
        dt.redrawCanvas();
    }


    private void updateRadioButtons(){
        if(cs.isLayerHidden(layersChoiceBox.getValue()))
            hideLayerRadioButton.setSelected(true);
        else
            showLayerRadioButton.setSelected(true);
    }

    private void pushFigureForward(ActionEvent event){
        System.out.println("push to top");
        cs.pushSelectedFigureForward(layersChoiceBox.getValue());
        dt.redrawCanvas();
    }

    private void pushFigureToBottom(ActionEvent event){
        System.out.println("push to bottom");
        cs.pushSelectedFigureToBottom(layersChoiceBox.getValue());
        dt.redrawCanvas();
    }
}