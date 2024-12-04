	package frontend;
	
	import backend.CanvasState;
	import backend.model.*;
	import javafx.geometry.Insets;
	import javafx.scene.Cursor;
	import javafx.scene.canvas.Canvas;
	import javafx.scene.canvas.GraphicsContext;
	import javafx.scene.control.*;
	import javafx.scene.layout.BorderPane;
	import javafx.scene.layout.VBox;
	import javafx.scene.shape.ArcType;
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
		ColorHandler ch = new ColorHandler();
		Color lineColor = Color.BLACK;
		Color selectedLineColor = Color.RED;
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

		// Dibujar una figura
		Point startPoint;
		// Seleccionar una figura
		Figure selectedFigure;
		Figure copiedFigure;
		// StatusBar
		StatusPane statusPane;

		ToggleGroup tools = new ToggleGroup();
		final Integer bevelWidth = 7;

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
					duplicateButton
			};

			rightButtonsBox.getChildren().addAll(rightControls);
			leftButtonsBox.getChildren().addAll(toolsArr);
			leftButtonsBox.getChildren().addAll(leftControls);
			//setup choice box
			shadowTypeBox.setValue(Shadow.NONE);
			shadowTypeBox.getItems().addAll(Shadow.values());

			leftButtonsBox.setPadding(new Insets(5));
			leftButtonsBox.setStyle("-fx-background-color: #999");
			leftButtonsBox.setPrefWidth(100);

			rightButtonsBox.setPadding(new Insets(10));
			rightButtonsBox.setStyle("-fx-background-color: #999");
			rightButtonsBox.setPrefWidth(100);

			gc.setLineWidth(1);

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

			setLeft(leftButtonsBox);
			//setRight(canvas);
			setCenter(canvas);
			setRight(rightButtonsBox);

		}


		private void onMousePressed(MouseEvent event){
			startPoint = new Point(event.getX(), event.getY());
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
					List<java.awt.Color> colors = colorListFromColorPickerArr(colorPickers);
					Shadow shadowType = shadowTypeBox.getValue();
					switch (b.getText()) {
						case squareText:
							double size = Math.abs(endPoint.getX() - startPoint.getX());
							newFigure = new Square(startPoint, size, colors, shadowType);
							break;
						case rectangleText: {
							newFigure = new Rectangle(startPoint, endPoint, colors, shadowType);
							break;
						}
						case circleText:
							double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
							newFigure = new Circle(startPoint, circleRadius, colors, shadowType);
							break;
						case ellipseText: {
							Point centerPoint = new Point(Math.abs(endPoint.x + startPoint.x) / 2, (Math.abs((endPoint.y + startPoint.y)) / 2));
							double sMayorAxis = Math.abs(endPoint.x - startPoint.x);
							double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
							newFigure = new Ellipse(centerPoint, sMayorAxis, sMinorAxis, colors, shadowType);
							break;
						}
						default: {
							return;
						}
					}


				newFigure.setHasBevel(bevelCheckbox.isSelected());
				canvasState.addFigure(newFigure);

				startPoint = null;
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
					figureToPasteFormatOnto.setColors(copiedFigure.getColors());
					redrawCanvas();

				}
				copiedFigure = null;
			}
		}
		private void onMouseDragged(MouseEvent event){
			Point eventPoint = new Point(event.getX(), event.getY());
			if(selectionButton.isSelected() && selectedFigure != null) {
				selectedFigure.move(eventPoint);
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
				Figure rotatedFigure = null;

				if(selectedFigure instanceof Rectangle) {
					Rectangle rectangle = (Rectangle) selectedFigure;
					rotatedFigure = (Rectangle) selectedFigure.turnRight();
				}
				if(selectedFigure instanceof Ellipse){
					Ellipse ellipse = (Ellipse) selectedFigure;
					rotatedFigure= (Ellipse) selectedFigure.turnRight();
				}
				if (rotatedFigure != null) {
					canvasState.addFigure(rotatedFigure);
					// Opcional: Eliminar la figura original
					//canvasState.deleteFigure(selectedFigure);
					selectedFigure = rotatedFigure;
					redrawCanvas();
				}
				redrawCanvas();
			}
		}
		private void onFlipHorizontalButtonCLick(ActionEvent event){
			if( selectedFigure!= null ){
				Figure flipFigure = null;

				if(selectedFigure instanceof Circle) {
					Circle circle = (Circle) selectedFigure;
					flipFigure = (Circle) selectedFigure.flipH();
				}
				if(selectedFigure instanceof Ellipse) {
					Ellipse ellipse = (Ellipse) selectedFigure;
					flipFigure = (Ellipse) selectedFigure.flipH();
				}
				if(selectedFigure instanceof Rectangle) {
					Rectangle rectangle = (Rectangle) selectedFigure;
					flipFigure = (Rectangle) selectedFigure.flipH();
				}
				if(selectedFigure instanceof Square) {
					Square square = (Square) selectedFigure;
					flipFigure = (Square) selectedFigure.flipH();
				}

				if (flipFigure != null) {
					canvasState.addFigure(flipFigure);
					// Opcional: Eliminar la figura original
					//canvasState.deleteFigure(selectedFigure);
					selectedFigure = flipFigure;
					redrawCanvas();
				}

			}
		}
		private void onFlipVerticalButton(ActionEvent event){
			if( selectedFigure!= null ){
				Figure flipFigure = null;

				if(selectedFigure instanceof Circle) {
					Circle circle = (Circle) selectedFigure;
					flipFigure = (Circle) selectedFigure.flipV();
				}
				if(selectedFigure instanceof Ellipse) {
					Ellipse ellipse = (Ellipse) selectedFigure;
					flipFigure = (Ellipse) selectedFigure.flipV();
				}
				if(selectedFigure instanceof Rectangle) {
					Rectangle rectangle = (Rectangle) selectedFigure;
					flipFigure = (Rectangle) selectedFigure.flipV();
				}
				if(selectedFigure instanceof Square) {
					Square square = (Square) selectedFigure;
					flipFigure = (Square) selectedFigure.flipV();
				}

				if (flipFigure != null) {
					canvasState.addFigure(flipFigure);
					// Opcional: Eliminar la figura original
					//canvasState.deleteFigure(selectedFigure);
					selectedFigure = flipFigure;
					redrawCanvas();
				}

			}
		}
		private void onDuplicateButton( ActionEvent event){
			if( selectedFigure!= null ){
				Figure duplicateFigure = null;

				if(selectedFigure instanceof Circle) {
					Circle circle = (Circle) selectedFigure;
					duplicateFigure = (Circle) selectedFigure.duplicate();
				}
				if(selectedFigure instanceof Ellipse) {
					Ellipse ellipse = (Ellipse) selectedFigure;
					duplicateFigure = (Ellipse) selectedFigure.duplicate();
				}
				if(selectedFigure instanceof Rectangle) {
					Rectangle rectangle = (Rectangle) selectedFigure;
					duplicateFigure = (Rectangle) selectedFigure.duplicate();
				}
				if(selectedFigure instanceof Square) {
					Square square = (Square) selectedFigure;
					duplicateFigure = (Square) selectedFigure.duplicate();
				}

				if (duplicateFigure != null) {
					canvasState.addFigure(duplicateFigure);
					// Opcional: Eliminar la figura original
					//canvasState.deleteFigure(selectedFigure);
					selectedFigure = duplicateFigure;
					redrawCanvas();
				}

			}
		}

		private void redrawCanvas() {
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			for(Figure figure : canvasState.figures()) {
				switch (figure) {
					case LinearFigure lf -> drawLinearFigure(lf);
					case RadialFigure rf -> drawRadialFigure(rf);
					default -> throw new RuntimeException("Unsupported figure");
				}
			}
		}
		private List<java.awt.Color> colorListFromColorPickerArr(ColorPicker[] arr){
			List<java.awt.Color> res = new ArrayList<>();
			for(ColorPicker p : arr){
				res.add(ch.fxColorToAwtColor(p.getValue()));
			}
			return res;
		}
		private void drawRadialFigure(RadialFigure figure){
			if(figure.getShadeType() != Shadow.NONE){
				gc.setFill(ch.awtColorToFxColor(figure.getShadeType().getShadowColor(figure.getColors().getLast())));
				gc.fillOval(figure.getCenterPoint().getX() - figure.getWidth() / 2 + figure.getShadeType().getOffset(),
						figure.getCenterPoint().getY() - figure.getHeight() / 2 + figure.getShadeType().getOffset(), figure.getWidth(), figure.getHeight());

			}
			if(figure.getHasBevel()){
				drawRadialBevel(figure);
			}
			gc.setFill(ch.radialGradientFromColorList(figure.getColors()));
			gc.setStroke(figure == selectedFigure ? selectedLineColor : lineColor);
			gc.strokeOval(figure.getCenterPoint().getX() - (figure.getWidth() / 2),  figure.getCenterPoint().getY() - (figure.getHeight() / 2), figure.getWidth(), figure.getHeight());
			gc.fillOval(figure.getCenterPoint().getX() - (figure.getWidth() / 2), figure.getCenterPoint().getY() - (figure.getHeight() / 2), figure.getWidth(), figure.getHeight());

		}
		private void drawRadialBevel(RadialFigure figure){
			gc.setStroke(Color.LIGHTGRAY);
			double aux = gc.getLineWidth();
			gc.setLineWidth(bevelWidth);
			gc.strokeArc(
					figure.getCenterPoint().getX() - (figure.getWidth() / 2),
					figure.getCenterPoint().getY() - (figure.getHeight() / 2),
					figure.getWidth(),
					figure.getHeight(),
					45,
					180,
					ArcType.OPEN
			);
			gc.setStroke(Color.BLACK);
			gc.strokeArc(
					figure.getCenterPoint().getX() - (figure.getWidth() / 2),
					figure.getCenterPoint().getY() - (figure.getHeight() / 2),
					figure.getWidth(),
					figure.getHeight(),
					225,
					180,
					ArcType.OPEN
			);
			gc.setLineWidth(aux);
		}

		private void drawLinearFigure(LinearFigure figure){
			if(figure.getShadeType() != Shadow.NONE) {
				gc.setFill(ch.awtColorToFxColor(figure.getShadeType().getShadowColor(figure.getColors().getLast())));
				gc.fillRect(figure.getTopLeft().getX() + figure.getShadeType().getOffset(),
						figure.getTopLeft().getY() + figure.getShadeType().getOffset(),
						Math.abs(figure.getTopLeft().getX() - figure.getBottomRight().getX()),
						Math.abs(figure.getTopLeft().getY() - figure.getBottomRight().getY()));
			}
			if(figure.getHasBevel()) {
				drawLinearBevel(figure);
			}
			gc.setFill(ch.linearGradientFromColorList(figure.getColors()));
			gc.fillRect(figure.getTopLeft().getX(), figure.getTopLeft().getY(),
					Math.abs(figure.getTopLeft().getX() - figure.getBottomRight().getX()), Math.abs(figure.getTopLeft().getY() - figure.getBottomRight().getY()));
			gc.setStroke(figure == selectedFigure ? selectedLineColor : lineColor);
			gc.strokeRect(figure.getTopLeft().getX(), figure.getTopLeft().getY(),
					Math.abs(figure.getTopLeft().getX() - figure.getBottomRight().getX()), Math.abs(figure.getTopLeft().getY() - figure.getBottomRight().getY()));
		}
		private void drawLinearBevel(LinearFigure figure){
			double aux = gc.getLineWidth();
			double x = figure.getTopLeft().getX();
			double y = figure.getTopLeft().getY();
			double width = Math.abs(x - figure.getBottomRight().getX());
			double height = Math.abs(y - figure.getBottomRight().getY());
			gc.setLineWidth(this.bevelWidth);
			gc.setStroke(Color.LIGHTGRAY);
			gc.strokeLine(x, y, x + width, y);
			gc.strokeLine(x, y, x, y + height);
			gc.setStroke(Color.BLACK);
			gc.strokeLine(x + width, y, x + width, y + height);
			gc.strokeLine(x, y + height, x + width, y + height);
			gc.setLineWidth(aux);
		}

	}
