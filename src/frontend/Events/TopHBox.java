package frontend.Events;

import backend.CanvasState;
import backend.Layer;
import frontend.DrawingTool.DrawingTool;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class TopHBox extends HBox {

    private  CanvasState cs;
    private DrawingTool dt;

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

        // Set actions
        layersChoiceBox.setOnAction(e->{}); // this::onLayerSelection
        showLayerRadioButton.setOnAction(e->{}); // this::showToggle
        hideLayerRadioButton.setOnAction(e->{}); // this::hideToggle
        addLayerButton.setOnAction(e->{}); // this::createLayer
        removeLayerButton.setOnAction(e->{}); // this::removeLayer
        pushForwardButton.setOnAction(e->{}); // this::pushForward
        pushToBottomButton.setOnAction(e->{}); // this::pushToBottom


        // Creates the first 3 layers
        createLayer();
        createLayer();
        createLayer();
        layersChoiceBox.setValue("Capa 1");

        // Set up Hide and Show Layer Radio Buttons
        updateRadioButtons(1);
    }

    private void createLayer(ActionEvent event) {
        createLayer();
    }

    private void createLayer() {
//        int newLayerNumber = paintPane.canvasState.getNextLayerNumber();
//        String newLayerName = "Capa " + newLayerNumber;
//        paintPane.canvasState.increaseNextLayerNumber();
//        paintPane.layersChoiceBox.getItems().add(newLayerName);
//        System.out.println(newLayerName);
//        paintPane.canvasState.addLayer(newLayerName);
//        paintPane.canvasState.changeLayer(newLayerName);
//        paintPane.layersChoiceBox.setValue(newLayerName);
//        paintPane.redrawCanvas();
    }

    private void removeLayer(ActionEvent event) {
      /*  if(paintPane.canvasState.getWorkingLayer().compareTo("Capa 4") < 0){
            return;
        }
        String layerToDelete = paintPane.layersChoiceBox.getValue();
        System.out.println(layerToDelete);
        paintPane.layersChoiceBox.setValue("Capa 1");
        paintPane.layersChoiceBox.getItems().remove(layerToDelete);
        paintPane.canvasState.deleteLayer(layerToDelete);
        paintPane.canvasState.changeLayer("Capa 1");
        paintPane.redrawCanvas();
    */}

    private void hideToggle(ActionEvent event) {
    /*    if (paintPane.hideLayerRadioButton.isSelected()) {
            paintPane.canvasState.hideLayer();
        }
        paintPane.redrawCanvas();
    */}

    public void showToggle(ActionEvent event) {
//       if (paintPane.showLayerRadioButton.isSelected()) {
//            paintPane.canvasState.showLayer();
//        }
//        paintPane.redrawCanvas();
    }
    Integer selectedLayer;
    private void onLayerSelection(ActionEvent event) {
        //.canvasState.changeLayer(layersChoiceBox.getValue());
    //    updateRadioButtons();
      //  paintPane.redrawCanvas();
    }

    private void updateRadioButtons(Integer selectedLayer){
      //  Layer dt.getCanvasState().getOrInitializeLayer(selectedLayer);
    }

    private void pushForward(ActionEvent event){
      /*  System.out.println("push to top");
        paintPane.canvasState.pushForward();
        paintPane.redrawCanvas();
    */}

    private void pushToBottom(ActionEvent event){
       /* System.out.println("push to bottom");
        paintPane.canvasState.pushToBottom();
        paintPane.redrawCanvas();
    */}

}
