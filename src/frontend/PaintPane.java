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
		Label formatText = new Label("Formato:");
		ChoiceBox<Shadow> shadowTypeBox = new ChoiceBox<>();
		CheckBox bevelCheckbox = new CheckBox("Biselado");
		Button copyFormatButton = new Button("Copiar formato");
		Button turnButton = new Button("Girar");
		// Selector de color de relleno

		ColorPicker fillColorPicker = new ColorPicker(defaultFillColor);
		ColorPicker fillSecondaryColorPicker = new ColorPicker(defaultSecondaryFillColor);
		//calculate shape gradient only from these color Picker
		final ColorPicker[] colorPickers = {fillColorPicker, fillSecondaryColorPicker};
		// Dibujar una figura
		Point startPoint;
		// Seleccionar una figura
		Figure selectedFigure;
		Figure copiedFigure;
		// StatusBar
		StatusPane statusPane;

		ToggleGroup tools = new ToggleGroup();

		final Integer bevelWidth = 7;


		// Colores de relleno de cada figura
		//Map<Figure, Paint> figureColorMap = new HashMap<>();
		private void onMousePressed(MouseEvent event){
			startPoint = new Point(event.getX(), event.getY());
		}
		private void onMouseRelease(MouseEvent event) {
			Point endPoint = new Point(event.getX(), event.getY());
			if (startPoint == null) {
				return;
			}
			if (endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				return;
			}
			Figure newFigure;
			Paint gradient;

			//	figureColorMap.put(newFigure, gradient);
			ToggleButton b = (ToggleButton) (tools.getSelectedToggle());
			if (b != null) {
				if (b.getText().equals(rectangleText) || b.getText().equals(squareText)) {
					gradient = new LinearGradient(0, 0, 1, 0, true,
							CycleMethod.NO_CYCLE,
							new Stop(0, fillColorPicker.getValue()),
							new Stop(1, fillSecondaryColorPicker.getValue()));

				} else {
					gradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
							CycleMethod.NO_CYCLE,
							new Stop(0, fillColorPicker.getValue()),
							new Stop(1, fillSecondaryColorPicker.getValue()));

				}
				switch (b.getText()) {
					case squareText:
						double size = Math.abs(endPoint.getX() - startPoint.getX());
						newFigure = new Square(startPoint, size, colorListFromColorPickerArr(colorPickers), shadowTypeBox.getValue());
						break;
					case rectangleText: {
						newFigure = new Rectangle(startPoint, endPoint,colorListFromColorPickerArr(colorPickers), shadowTypeBox.getValue());
						break;
					}
					case circleText:
						double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
						newFigure = new Circle(startPoint, circleRadius, colorListFromColorPickerArr(colorPickers), shadowTypeBox.getValue());
						break;
					case ellipseText: {
						Point centerPoint = new Point(Math.abs(endPoint.x + startPoint.x) / 2, (Math.abs((endPoint.y + startPoint.y)) / 2));
						double sMayorAxis = Math.abs(endPoint.x - startPoint.x);
						double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
						newFigure = new Ellipse(centerPoint, sMayorAxis, sMinorAxis, colorListFromColorPickerArr(colorPickers), shadowTypeBox.getValue());
						break;
					}
					default: {
						return;
					}
				}


				newFigure.setBevel(bevelCheckbox.isSelected());
				canvasState.addFigure(newFigure);

				startPoint = null;
				redrawCanvas();
			}
		}

		private void onMouseMoved(MouseEvent event) {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for (Figure figure : canvasState.figures()) {
				if (figureBelongs(figure, eventPoint)) {
					found = true;
					label.append(figure.toString());
				}
			}
			if (found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		}
		private void onMouseClicked(MouseEvent event){
			Point eventPoint = new Point(event.getX(), event.getY());
			if(selectionButton.isSelected()) {
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (Figure figure : canvasState.figures()) {
					if (figureBelongs(figure, eventPoint)) {
						found = true;
						selectedFigure = figure;
						label.append(figure.toString());
					}
				}
				if (found) {
					statusPane.updateStatus(label.toString());
				} else {
					selectedFigure = null;
					statusPane.updateStatus("Ninguna figura encontrada");
				}
				redrawCanvas();
			}
			if(copiedFigure != null) {
				boolean found;
				Figure figureToPasteFormatOnto = null;
				for (Figure figure : canvasState.figures()) {
					if (figureBelongs(figure, eventPoint)) {
						figureToPasteFormatOnto = figure;
						break;
					}
				}
				if (figureToPasteFormatOnto == null) {
					copiedFigure = null;
				}
				else {
					figureToPasteFormatOnto.setShadeType(copiedFigure.getShadeType());
					figureToPasteFormatOnto.setBevel(copiedFigure.getBevel());
					if(figureToPasteFormatOnto instanceof LinearFigure) {
						figureToPasteFormatOnto.setColors(copiedFigure.getColors());
					} else {
						figureToPasteFormatOnto.setColors(copiedFigure.getColors());
					}
					copiedFigure = null;
					redrawCanvas();
				}
			}
		}
		private void onMouseDragged(MouseEvent event){
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
				if(selectedFigure instanceof Rectangle) {
					Rectangle rectangle = (Rectangle) selectedFigure;
					rectangle.getTopLeft().x += diffX;
					rectangle.getBottomRight().x += diffX;
					rectangle.getTopLeft().y += diffY;
					rectangle.getBottomRight().y += diffY;
				} else if(selectedFigure instanceof Circle) {
					Circle circle = (Circle) selectedFigure;
					circle.getCenterPoint().x += diffX;
					circle.getCenterPoint().y += diffY;
				} else if(selectedFigure instanceof Square) {
					Square square = (Square) selectedFigure;
					square.getTopLeft().x += diffX;
					square.getBottomRight().x += diffX;
					square.getTopLeft().y += diffY;
					square.getBottomRight().y += diffY;
				} else if(selectedFigure instanceof Ellipse) {
					Ellipse ellipse = (Ellipse) selectedFigure;
					ellipse.getCenterPoint().x += diffX;
					ellipse.getCenterPoint().y += diffY;
				}
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
				if(selectedFigure instanceof Rectangle) {
					Rectangle rectangle = (Rectangle) selectedFigure.turnRight();
				}
				redrawCanvas();
			}
		}


		public PaintPane(CanvasState canvasState, StatusPane statusPane) {
			this.canvasState = canvasState;
			this.statusPane = statusPane;
			ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, deleteButton};
			for (ToggleButton tool : toolsArr) {
				tool.setMinWidth(90);
				tool.setToggleGroup(tools);
				tool.setCursor(Cursor.HAND);
			}

			//Set left buttonsBox
			VBox buttonsBox = new VBox(10);
			VBox rightButtonsBox = new VBox(20);
			Control[] additionalControls = {
					bevelCheckbox,
					formatText,
					shadowTypeBox,
					fillColorPicker,
					fillSecondaryColorPicker,
					copyFormatButton
			};

			rightButtonsBox.getChildren().addAll(turnButton);

			buttonsBox.getChildren().addAll(toolsArr);
			buttonsBox.getChildren().addAll(additionalControls);
			//setup choice box
			shadowTypeBox.setValue(Shadow.NONE);
			shadowTypeBox.getItems().addAll(Shadow.values());

			buttonsBox.setPadding(new Insets(5));
			buttonsBox.setStyle("-fx-background-color: #999");
			buttonsBox.setPrefWidth(100);

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
	
			setLeft(buttonsBox);
			//setRight(canvas);
			setCenter(canvas);
			setRight(rightButtonsBox);

		}
	
		void redrawCanvas() {
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			for(Figure figure : canvasState.figures()) {
				switch (figure) {
					case LinearFigure lf -> drawLinearFigure(lf);
					case RadialFigure rf -> drawRadialFigure(rf);
					default -> throw new RuntimeException("Unsupported figure");
				}
			}
		}

		private RadialGradient radialGradientFromColorList(List<java.awt.Color> colors){
			return new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
					CycleMethod.NO_CYCLE,
					createStopsFromColorList(colors));
		}
		private LinearGradient linearGradientFromColorList(List<java.awt.Color> colors) {
			return new LinearGradient(0, 0, 1, 0, true,
					CycleMethod.NO_CYCLE,
					createStopsFromColorList(colors));
		}

		private List<Stop> createStopsFromColorList(List<java.awt.Color> list){
			List<Stop> stops = new ArrayList<>();
			for(java.awt.Color c : list){
				double travelPercentage = (double)stops.size() / (list.size() - 1);
				stops.add(new Stop(travelPercentage, awtColorToFxColor(c)));
			}
			return stops;
		}
		private List<java.awt.Color> colorListFromColorPickerArr(ColorPicker[] arr){
			List<java.awt.Color> res = new ArrayList<>();
			for(ColorPicker p : arr){
				res.add(fxColorToAwtColor(p.getValue()));
			}
			return res;
		}

		private Color awtColorToFxColor(java.awt.Color c){
			return new Color(
					(double) c.getRed() / 255,
					(double) c.getGreen() / 255,
					(double)c.getBlue() / 255,
					(double)c.getAlpha() / 255);
		}
		private java.awt.Color fxColorToAwtColor(Color c){
			return new java.awt.Color(
					(float)c.getRed(),
					(float)c.getGreen(),
					(float)c.getBlue(),
					(float)c.getOpacity());
		}

		private void drawRadialFigure(RadialFigure figure){
			if(figure.getShadeType() != Shadow.NONE){
				gc.setFill(awtColorToFxColor(figure.getShadeType().getShadowColor(figure.getColors().getLast())));
				gc.fillOval(figure.getCenterPoint().getX() - figure.getWidth() / 2 + figure.getShadeType().getOffset(),
						figure.getCenterPoint().getY() - figure.getHeight() / 2 + figure.getShadeType().getOffset(), figure.getWidth(), figure.getHeight());

			}
			if(figure.getBevel()){
				drawRadialBevel(figure);
			}
			gc.setFill(radialGradientFromColorList(figure.getColors()));
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
				gc.setFill(awtColorToFxColor(figure.getShadeType().getShadowColor(figure.getColors().getLast())));
				gc.fillRect(figure.getTopLeft().getX() + figure.getShadeType().getOffset(),
						figure.getTopLeft().getY() + figure.getShadeType().getOffset(),
						Math.abs(figure.getTopLeft().getX() - figure.getBottomRight().getX()),
						Math.abs(figure.getTopLeft().getY() - figure.getBottomRight().getY()));
			}
			if(figure.getBevel()) {
				drawLinearBevel(figure);
			}
			gc.setFill(linearGradientFromColorList(figure.getColors()));
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

		boolean figureBelongs(Figure figure, Point eventPoint) {
			boolean found = false;
			if(figure instanceof Rectangle) {
				Rectangle rectangle = (Rectangle) figure;
				found = eventPoint.getX() > rectangle.getTopLeft().getX() && eventPoint.getX() < rectangle.getBottomRight().getX() &&
						eventPoint.getY() > rectangle.getTopLeft().getY() && eventPoint.getY() < rectangle.getBottomRight().getY();
			} else if(figure instanceof Circle) {
				Circle circle = (Circle) figure;
				found = Math.sqrt(Math.pow(circle.getCenterPoint().getX() - eventPoint.getX(), 2) +
						Math.pow(circle.getCenterPoint().getY() - eventPoint.getY(), 2)) < circle.getRadius();
			} else if(figure instanceof Square) {
				Square square = (Square) figure;
				found = eventPoint.getX() > square.getTopLeft().getX() && eventPoint.getX() < square.getBottomRight().getX() &&
						eventPoint.getY() > square.getTopLeft().getY() && eventPoint.getY() < square.getBottomRight().getY();
			} else if(figure instanceof Ellipse) {
				Ellipse ellipse = (Ellipse) figure;
				// Nota: Fórmula aproximada. No es necesario corregirla.
				found = ((Math.pow(eventPoint.getX() - ellipse.getCenterPoint().getX(), 2) / Math.pow(ellipse.getsMayorAxis(), 2)) +
						(Math.pow(eventPoint.getY() - ellipse.getCenterPoint().getY(), 2) / Math.pow(ellipse.getsMinorAxis(), 2))) <= 0.30;
			}
			return found;
		}
	
	}
