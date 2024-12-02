	package frontend;
	
	import backend.CanvasState;
	import backend.model.*;
	import javafx.beans.Observable;
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

	public class PaintPane extends BorderPane {
	
		// BackEnd
		CanvasState canvasState;
	
		// Canvas y relacionados
		Canvas canvas = new Canvas(800, 600);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Color lineColor = Color.BLACK;
		Color defaultFillColor = Color.YELLOW;
		Color defaultSecondaryFillColor = Color.ORANGE;
	
		// Botones Barra Izquierda
		ToggleButton selectionButton = new ToggleButton("Seleccionar");
		ToggleButton rectangleButton = new ToggleButton("Rectángulo");
		ToggleButton circleButton = new ToggleButton("Círculo");
		ToggleButton squareButton = new ToggleButton("Cuadrado");
		ToggleButton ellipseButton = new ToggleButton("Elipse");
		ToggleButton deleteButton = new ToggleButton("Borrar");
		Label formatText = new Label("Formato:");
		ChoiceBox<Shadow> shadowTypeBox = new ChoiceBox<>();
		CheckBox bevelCheckbox = new CheckBox("Biselado");

		// Selector de color de relleno
		ColorPicker fillColorPicker = new ColorPicker(defaultFillColor);
		ColorPicker fillSecondaryColorPicker = new ColorPicker(defaultSecondaryFillColor);
		// Dibujar una figura
		Point startPoint;
		// Seleccionar una figura
		Figure selectedFigure;
		// StatusBar
		StatusPane statusPane;

		ToggleGroup tools = new ToggleGroup();



		// Colores de relleno de cada figura
		//Map<Figure, Paint> figureColorMap = new HashMap<>();
		private void onMousePressed(MouseEvent event){
			startPoint = new Point(event.getX(), event.getY());
		}
		private void onMouseRelease(MouseEvent event){
				Point endPoint = new Point(event.getX(), event.getY());
				if(startPoint == null) {
					return ;
				}
				if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
					return ;
				}
				Figure newFigure = null;
				Paint gradient;
				if(rectangleButton.isSelected()) {
					gradient = new LinearGradient(0, 0, 1, 0, true,
							CycleMethod.NO_CYCLE,
							new Stop(0, fillColorPicker.getValue()),
							new Stop(1, fillSecondaryColorPicker.getValue()));
					newFigure = new Rectangle(startPoint, endPoint, gradient, shadowTypeBox.getValue());

				}
				else if(circleButton.isSelected()) {
					gradient =  new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
							CycleMethod.NO_CYCLE,
							new Stop(0, fillColorPicker.getValue()),
							new Stop(1, fillSecondaryColorPicker.getValue()));
					double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
					newFigure = new Circle(startPoint, circleRadius, gradient, shadowTypeBox.getValue());

				} else if(squareButton.isSelected()) {
					double size = Math.abs(endPoint.getX() - startPoint.getX());
					gradient = new LinearGradient(0, 0, 1, 0, true,
							CycleMethod.NO_CYCLE,
							new Stop(0, fillColorPicker.getValue()),
							new Stop(1, fillSecondaryColorPicker.getValue()));
					newFigure = new Square(startPoint, size, gradient, shadowTypeBox.getValue());

				} else if(ellipseButton.isSelected()) {
					Point centerPoint = new Point(Math.abs(endPoint.x + startPoint.x) / 2, (Math.abs((endPoint.y + startPoint.y)) / 2));
					double sMayorAxis = Math.abs(endPoint.x - startPoint.x);
					double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
					gradient =  new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
							CycleMethod.NO_CYCLE,
							new Stop(0, fillColorPicker.getValue()),
							new Stop(1, fillSecondaryColorPicker.getValue()));
					newFigure = new Ellipse(centerPoint, sMayorAxis, sMinorAxis, gradient, shadowTypeBox.getValue());

				} else {
					return ;
				}
				//	figureColorMap.put(newFigure, gradient);
		/*		switch(tools.getSelectedToggle()){
					case 1:
					case 3: {

					}
					case 2:
					case 4: {

					}
				}
*/
				newFigure.setBevel(bevelCheckbox.isSelected());
				canvasState.addFigure(newFigure);

				startPoint = null;
				redrawCanvas();
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
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
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
			Control[] additionalControls = {bevelCheckbox, formatText, shadowTypeBox, fillColorPicker, fillSecondaryColorPicker};
			buttonsBox.getChildren().addAll(toolsArr);
			buttonsBox.getChildren().addAll(additionalControls);

			//setup choice box
			shadowTypeBox.setValue(Shadow.NONE);
			shadowTypeBox.getItems().addAll(Shadow.values());

			buttonsBox.setPadding(new Insets(5));
			buttonsBox.setStyle("-fx-background-color: #999");
			buttonsBox.setPrefWidth(100);
			gc.setLineWidth(1);

			//setup event
			shadowTypeBox.setOnAction(this::onChoiceBoxSelection);
			canvas.setOnMousePressed(this::onMousePressed);
			canvas.setOnMouseReleased(this::onMouseRelease);
			canvas.setOnMouseMoved(this::onMouseMoved);
			canvas.setOnMouseClicked(this::onMouseClicked);
			canvas.setOnMouseDragged(this::onMouseDragged);
			deleteButton.setOnAction(this::onDeleteButtonClick);
	
			setLeft(buttonsBox);
			setRight(canvas);
		}
	
		void redrawCanvas() {
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			for(Figure figure : canvasState.figures()) {
				if(figure == selectedFigure) {
					gc.setStroke(Color.RED);
				} else {
					gc.setStroke(lineColor);
				}
				gc.setFill(figure.getGradient());
				double aux = gc.getLineWidth();

				switch (figure) {
					case Rectangle rectangle -> {
						if(figure.getBevel()) {
							double x = rectangle.getTopLeft().getX();
							double y = rectangle.getTopLeft().getY();
							double width = Math.abs(x - rectangle.getBottomRight().getX());
							double height = Math.abs(y - rectangle.getBottomRight().getY());
							gc.setLineWidth(10);
							gc.setStroke(Color.LIGHTGRAY);
							gc.strokeLine(x, y, x + width, y);
							gc.strokeLine(x, y, x, y + height);
							gc.setStroke(Color.BLACK);
							gc.strokeLine(x + width, y, x + width, y + height);
							gc.strokeLine(x, y + height, x + width, y + height);
							gc.setLineWidth(aux);
						}
						if(!figure.getShadeType().equals(Shadow.NONE)){
						gc.setFill(figure.getShadeType().getPaint(figure.getGradient()));
						gc.fillRect(rectangle.getTopLeft().getX() + figure.getShadeType().getOffset(),
								rectangle.getTopLeft().getY() + figure.getShadeType().getOffset(),
								Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()),
								Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
						}
						gc.setFill(figure.getGradient());
	
						gc.fillRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
								Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
	
						gc.strokeRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
								Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
	
					}
					case Circle circle -> {
						double diameter = circle.getRadius() * 2;
	
						if(figure.getBevel()) {
							double arcX = circle.getCenterPoint().getX() - circle.getRadius();
							double arcY = circle.getCenterPoint().getY() - circle.getRadius();
							gc.setLineWidth(10);
							gc.setStroke(Color.LIGHTGRAY);
							gc.strokeArc(arcX, arcY, diameter, diameter, 45, 180, ArcType.OPEN);
							gc.setStroke(Color.BLACK);
							gc.strokeArc(arcX, arcY, diameter, diameter, 225, 180, ArcType.OPEN);
							gc.setLineWidth(aux);

						}
						if(!figure.getShadeType().equals(Shadow.NONE)) {
							gc.setFill(figure.getShadeType().getPaint(figure.getGradient()));
							gc.fillOval(circle.getCenterPoint().getX() - circle.getRadius() + figure.getShadeType().getOffset(),
									circle.getCenterPoint().getY() - circle.getRadius() + figure.getShadeType().getOffset(), diameter, diameter);
						}
						gc.setFill(figure.getGradient());
	
						gc.fillOval(circle.getCenterPoint().getX() - circle.getRadius(), circle.getCenterPoint().getY() - circle.getRadius(), diameter, diameter);
						gc.strokeOval(circle.getCenterPoint().getX() - circle.getRadius(), circle.getCenterPoint().getY() - circle.getRadius(), diameter, diameter);
					}
					case Square square -> {
						if(figure.getBevel()) {
							double x = square.getTopLeft().getX();
							double y = square.getTopLeft().getY();
							double width = Math.abs(x - square.getBottomRight().getX());
							double height = Math.abs(y - square.getBottomRight().getY());
							gc.setLineWidth(10);
							gc.setStroke(Color.LIGHTGRAY);
							gc.strokeLine(x, y, x + width, y);
							gc.strokeLine(x, y, x, y + height);
							gc.setStroke(Color.BLACK);
							gc.strokeLine(x + width, y, x + width, y + height);
							gc.strokeLine(x, y + height, x + width, y + height);
							gc.setLineWidth(aux);

						}
						if(!figure.getShadeType().equals(Shadow.NONE)) {
							gc.setFill(figure.getShadeType().getPaint(figure.getGradient()));
							gc.fillRect(square.getTopLeft().getX() + figure.getShadeType().getOffset(),
									square.getTopLeft().getY() + figure.getShadeType().getOffset(),
									Math.abs(square.getTopLeft().getX() - square.getBottomRight().getX()),
									Math.abs(square.getTopLeft().getY() - square.getBottomRight().getY()));
						}
						gc.setFill(figure.getGradient());
	
						gc.fillRect(square.getTopLeft().getX(), square.getTopLeft().getY(),
								Math.abs(square.getTopLeft().getX() - square.getBottomRight().getX()), Math.abs(square.getTopLeft().getY() - square.getBottomRight().getY()));
						gc.strokeRect(square.getTopLeft().getX(), square.getTopLeft().getY(),
								Math.abs(square.getTopLeft().getX() - square.getBottomRight().getX()), Math.abs(square.getTopLeft().getY() - square.getBottomRight().getY()));
					}
					case Ellipse ellipse -> {
						if(figure.getBevel()) {
	
							gc.setStroke(Color.LIGHTGRAY);
							gc.setLineWidth(10);
							gc.strokeOval(
									ellipse.getCenterPoint().getX() - (ellipse.getsMayorAxis() / 2) + figure.getShadeType().getOffset(),
									ellipse.getCenterPoint().getY() - (ellipse.getsMinorAxis() / 2) + figure.getShadeType().getOffset(),
									ellipse.getsMayorAxis(),
									ellipse.getsMinorAxis()
							);
							gc.setLineWidth(aux);

						}
						gc.setFill(figure.getGradient());
	
						gc.strokeOval(ellipse.getCenterPoint().getX() - (ellipse.getsMayorAxis() / 2), ellipse.getCenterPoint().getY() - (ellipse.getsMinorAxis() / 2), ellipse.getsMayorAxis(), ellipse.getsMinorAxis());
						gc.fillOval(ellipse.getCenterPoint().getX() - (ellipse.getsMayorAxis() / 2), ellipse.getCenterPoint().getY() - (ellipse.getsMinorAxis() / 2), ellipse.getsMayorAxis(), ellipse.getsMinorAxis());
					}
					default -> {
					}
				}
			}
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
