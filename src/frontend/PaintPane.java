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


		Button pushForwardButton = new Button ("Traer al frente");
		Button pushToBottomButton = new Button ("Enviar al fondo");
		Label layerLabel = new Label("Capas");
		ChoiceBox<String> layersChoiceBox = new ChoiceBox<>();
		RadioButton showLayerRadioButton = new RadioButton("Mostrar");
		RadioButton hideLayerRadioButton = new RadioButton("Ocultar");
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

		public PaintPane(CanvasState canvasState, StatusPane statusPane) {
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
			shadowTypeBox.setOnAction(this::onChoiceBoxSelection);
			canvas.setOnMousePressed(this::onMousePressed);
			canvas.setOnMouseReleased(this::onMouseRelease);
			canvas.setOnMouseMoved(this::onMouseMoved);
			canvas.setOnMouseClicked(this::onMouseClicked);
			canvas.setOnMouseDragged(this::onMouseDragged);
			deleteButton.setOnAction(this::onDeleteButtonClick);
			copyFormatButton.setOnAction(this::onCopyFormatButtonClick);
			turnButton.setOnAction(this::onTurnButtonClick);
			flipHorizontalButton.setOnAction(this::onFlipHorizontalButtonCLick);
			flipVerticalButton.setOnAction(this::onFlipVerticalButton);
			duplicateButton.setOnAction(this::onDuplicateButton);
			divideButton.setOnAction(this::onDivideButtonClick);

			setLeft(leftButtonsBox);
			setTop(topButtonsBox);
			setCenter(canvas);
			setRight(rightButtonsBox);
		}


		private void onMousePressed(MouseEvent event){
			startPoint = new Point(event.getX(), event.getY());
			Point eventPoint = new Point(event.getX(), event.getY());
			if(selectedFigure != null){
				selectionDragStartOffset = selectedFigure.getCenterPoint().getDifference(eventPoint);
			}
		}
		private void onMouseRelease(MouseEvent event) {
			if (startPoint == null) {
				return;
			}
			Point endPoint = new Point(event.getX(), event.getY());
			if (endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				return;
			}
			Figure newFigure;
				ToggleButton b = (ToggleButton) (tools.getSelectedToggle());
				if (b != null) {
					List<Color> colors = colorListFromColorPickerArr(colorPickers);
					Shadow shadowType = shadowTypeBox.getValue();
                    boolean hasBevel = this.bevelCheckbox.isSelected();
					switch (b.getText()) {
						case squareText:
							double size = Math.abs(endPoint.getX() - startPoint.getX());
							newFigure = new Square(startPoint, size, shadowType, hasBevel);
							break;
						case rectangleText: {
							newFigure = new Rectangle(startPoint, endPoint, shadowType, hasBevel);
							break;
						}
						case circleText:
							double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
							newFigure = new Circle(startPoint, circleRadius, shadowType, hasBevel);
							break;
						case ellipseText: {
							Point centerPoint = new Point(Math.abs(endPoint.x + startPoint.x) / 2, (Math.abs((endPoint.y + startPoint.y)) / 2));
							double sMayorAxis = Math.abs(endPoint.x - startPoint.x);
							double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
							newFigure = new Ellipse(centerPoint, sMayorAxis, sMinorAxis, shadowType, hasBevel);
							break;
						}
						default: {
							return;
						}
					}


				newFigure.setHasBevel(bevelCheckbox.isSelected());
				canvasState.addFigure(newFigure);

				startPoint = null;
				selectionDragStartOffset = null;
				redrawCanvas();
			}
		}
		private void onMouseMoved(MouseEvent event) {
			Point eventPoint = new Point(event.getX(), event.getY());
			StringBuilder strB = new StringBuilder();
			for(Figure fig : canvasState.	figuresAtPoint(eventPoint)){
				strB.append(fig.toString());
			}
			if (strB.isEmpty()) {
				statusPane.updateStatus(eventPoint.toString());
			}
			else {
				statusPane.updateStatus(strB.toString());
			}
		}
		private void onMouseClicked(MouseEvent event){
			Point eventPoint = new Point(event.getX(), event.getY());
			if(selectionButton.isSelected()) {
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (Figure fig : canvasState.figuresAtPoint(eventPoint)) {
					label.append(fig.toString());
					selectedFigure = fig;
					found = true;
				}
				if (found) {
					statusPane.updateStatus(label.toString());
				} else {
					statusPane.updateStatus("Ninguna figura encontrada");
					selectedFigure = null;
				}
				redrawCanvas();
			}
			if(copiedFigure != null) {
				Figure figureToPasteFormatOnto = null;
				for (Figure figure : canvasState.figuresAtPoint(eventPoint)) {
					figureToPasteFormatOnto = figure;
					break;
				}
				if (figureToPasteFormatOnto != null) {
					figureToPasteFormatOnto.setShadeType(copiedFigure.getShadeType());
					figureToPasteFormatOnto.setHasBevel(copiedFigure.getHasBevel());
				//	figureToPasteFormatOnto.setColors(copiedFigure.getColors());
					redrawCanvas();

				}
				copiedFigure = null;
			}
		}
		private void onMouseDragged(MouseEvent event){
			Point eventPoint = new Point(event.getX(), event.getY());
			if(selectionButton.isSelected() && selectedFigure != null) {
				selectedFigure.move(eventPoint.add(selectionDragStartOffset));
				redrawCanvas();
			}
		}
		private void onChoiceBoxSelection(ActionEvent event){
			if(selectedFigure != null){
				selectedFigure.setShadeType(shadowTypeBox.getValue());
				redrawCanvas();
			}
		}
		private void onDeleteButtonClick(ActionEvent event){
			if (selectedFigure != null) {
				canvasState.deleteFigure(selectedFigure);
				selectedFigure = null;
				redrawCanvas();
			}
		}
		private void onCopyFormatButtonClick(ActionEvent event){
			if(selectedFigure == null)
				return;
			copiedFigure = selectedFigure;
		}

		private void onTurnButtonClick(ActionEvent event){
			if (selectedFigure != null) {
				selectedFigure.turnRight();
				redrawCanvas();
			}
		}
	private void onFlipHorizontalButtonCLick(ActionEvent event) {
		if (selectedFigure != null) {
			selectedFigure.flipHorizontal();
			redrawCanvas();
		}
	}

		private void onFlipVerticalButton(ActionEvent event) {
			if (selectedFigure != null) {
				selectedFigure.flipVertical();
				redrawCanvas();
			}
		}
		private void onDuplicateButton( ActionEvent event){
			if( selectedFigure!= null ) {
				canvasState.addFigure(selectedFigure.duplicate());
				redrawCanvas();
			}
		}
		private void onDivideButtonClick(ActionEvent event){
			if(selectedFigure == null)
				return;
			canvasState.divideFigure(selectedFigure);
			redrawCanvas();
		}

		private void redrawCanvas() {
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			for(Figure figure : canvasState.figures()) {
				drawingTool.drawFigure(figure, figure == selectedFigure, List.of(Color.PURPLE));
			}
		}
		private List<Color> colorListFromColorPickerArr(ColorPicker[] arr){
			List<Color> res = new ArrayList<>();
			for(ColorPicker p : arr){
				res.add(p.getValue());
			}
			return res;
		}
	}
