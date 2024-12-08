	package frontend;
	
	import backend.CanvasState;
	import backend.model.*;
	import javafx.geometry.Insets;
	import javafx.geometry.Pos;
	import javafx.scene.Cursor;
	import javafx.scene.canvas.Canvas;
	import javafx.scene.canvas.GraphicsContext;
	import javafx.scene.control.*;
	import javafx.scene.layout.BorderPane;
	import javafx.scene.layout.HBox;
	import javafx.scene.layout.VBox;
	import javafx.scene.paint.*;

	import javafx.event.ActionEvent;
	import javafx.scene.input.MouseEvent;

	import java.util.ArrayList;
	import java.util.List;

	public class PaintPane extends BorderPane {
	
		// BackEnd
		CanvasState canvasState;
	
		// Canvas y relacionados
		Canvas canvas = new Canvas(800, 600);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		Color defaultFillColor = Color.YELLOW;
		Color defaultSecondaryFillColor = Color.ORANGE;
	
		// Botones Barra Izquierda
		static final String rectangleText = "Rectángulo";
		static final String squareText = "Cuadrado";
		static final String circleText = "Círculo";
		static final String ellipseText = "Elipse";
		ToggleButton selectionButton = new ToggleButton("Seleccionar");
		ToggleButton rectangleButton = new ToggleButton(rectangleText);
		ToggleButton circleButton = new ToggleButton(circleText);
		ToggleButton squareButton = new ToggleButton(squareText);
		ToggleButton ellipseButton = new ToggleButton(ellipseText);
		ToggleButton deleteButton = new ToggleButton("Borrar");
		Label formatLabel = new Label("Formato:");
		ChoiceBox<Shadow> shadowTypeBox = new ChoiceBox<>();
		CheckBox bevelCheckbox = new CheckBox("Biselado");

		// Selector de color de relleno
		ColorPicker fillColorPicker = new ColorPicker(defaultFillColor);
		ColorPicker fillSecondaryColorPicker = new ColorPicker(defaultSecondaryFillColor);
		//calculate shape gradient only from these color Picker
		final ColorPicker[] colorPickers = {fillColorPicker, fillSecondaryColorPicker};

		//Barra lateral derecha
		Button copyFormatButton = new Button("Copiar formato");
		Label actionLabel = new Label("Acciones");
		Button turnButton = new Button("Girar D");
		Button flipHorizontalButton = new Button("Voltear H");
		Button flipVerticalButton = new Button("Voltear v");
		Button duplicateButton = new Button("Duplicar");
		Button divideButton = new Button("Dividir");

		//Barra superior
		Button pushForwardButton = new Button ("Traer al frente");
		Button pushToBottomButton = new Button ("Enviar al fondo");
		Label layerLabel = new Label("Capas");
		ChoiceBox<String> layersChoiceBox = new ChoiceBox<>();
		RadioButton showLayerRadioButton = new RadioButton("Mostrar");
		RadioButton hideLayerRadioButton = new RadioButton("Ocultar");
		ToggleGroup showHideToggle = new ToggleGroup();	// Para que solo se pueda seleccionar uno
		Button addLayerButton = new Button("Agregar capa");
		Button removeLayerButton = new Button("Eliminar capa");

		// Dibujar una figura
		Point startPoint;
		Point selectionDragStartOffset;
		// Seleccionar una figura
		Figure selectedFigure;
		Figure copiedFigure;
		// StatusBar
		StatusPane statusPane;

		ToggleGroup tools = new ToggleGroup();
		DrawGraphicContext drawingTool = new DrawGraphicContext(gc);

		private PaintPaneEvents paintPaneEvents;


		public PaintPane(CanvasState canvasState, StatusPane statusPane) {
			paintPaneEvents = new PaintPaneEvents(this);
			this.canvasState = canvasState;
			this.statusPane = statusPane;
			ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, deleteButton};
			for (ToggleButton tool : toolsArr) {
				tool.setMinWidth(90);
				tool.setToggleGroup(tools);
				tool.setCursor(Cursor.HAND);
			}

			//Set left leftButtonsBox
			VBox leftButtonsBox = new VBox(10);
			VBox rightButtonsBox = new VBox(10);
			HBox topButtonsBox = new HBox(10);
			Control[] leftControls = {
					bevelCheckbox,
					formatLabel,
					shadowTypeBox,
					fillColorPicker,
					fillSecondaryColorPicker,
					copyFormatButton
			};
			Control[] rightControls = {
					actionLabel,
					turnButton,
					flipHorizontalButton,
					flipVerticalButton,
					duplicateButton,
					divideButton,
			};

			Control[] topControls = {
					pushForwardButton,
					pushToBottomButton,
					layerLabel,
					layersChoiceBox,
					showLayerRadioButton,
					hideLayerRadioButton,
					addLayerButton,
					removeLayerButton
			};

			topButtonsBox.getChildren().addAll(topControls);
			rightButtonsBox.getChildren().addAll(rightControls);
			leftButtonsBox.getChildren().addAll(toolsArr);
			leftButtonsBox.getChildren().addAll(leftControls);
			//setup choice box
			shadowTypeBox.setValue(Shadow.NONE);
			shadowTypeBox.getItems().addAll(Shadow.values());

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

			//setup event
			shadowTypeBox.setOnAction(paintPaneEvents::onChoiceBoxSelection);
			canvas.setOnMousePressed(paintPaneEvents::onMousePressed);
			canvas.setOnMouseReleased(paintPaneEvents::onMouseRelease);
			canvas.setOnMouseMoved(paintPaneEvents::onMouseMoved);
			canvas.setOnMouseClicked(paintPaneEvents::onMouseClicked);
			canvas.setOnMouseDragged(paintPaneEvents::onMouseDragged);
			deleteButton.setOnAction(paintPaneEvents::onDeleteButtonClick);
			copyFormatButton.setOnAction(paintPaneEvents::onCopyFormatButtonClick);
			turnButton.setOnAction(paintPaneEvents::onTurnButtonClick);
			flipHorizontalButton.setOnAction(paintPaneEvents::onFlipHorizontalButtonCLick);
			flipVerticalButton.setOnAction(paintPaneEvents::onFlipVerticalButton);
			duplicateButton.setOnAction(paintPaneEvents::onDuplicateButton);
			divideButton.setOnAction(paintPaneEvents::onDivideButtonClick);

			setLeft(leftButtonsBox);
			setTop(topButtonsBox);
			setCenter(canvas);
			setRight(rightButtonsBox);

			showLayerRadioButton.setToggleGroup(showHideToggle);
			hideLayerRadioButton.setToggleGroup(showHideToggle);
		}

		public void redrawCanvas() {
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			for(Figure figure : canvasState.figures()) {
				drawingTool.drawFigure(figure, figure == selectedFigure, List.of(Color.YELLOW));
			}
		}
	}
