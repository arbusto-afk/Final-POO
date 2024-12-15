	package frontend;
	
	import backend.CanvasState;
	import backend.model.*;
	import frontend.DrawingTool.DrawingTool;
	import frontend.Events.FigureCanvas;
	import frontend.Events.LeftVBox;
	import frontend.Events.RightVBox;
	import frontend.Events.TopHBox;
	import javafx.scene.canvas.Canvas;
	import javafx.scene.control.*;
	import javafx.scene.layout.BorderPane;
	import javafx.scene.paint.*;

	public class PaintPane extends BorderPane {

		CanvasState canvasState;
	
		//Canvas canvas = new Canvas(800, 600);

	/*
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
*/
		// Selector de color de relleno
	//	ColorPicker fillColorPicker = new ColorPicker(defaultFillColor);
	//	ColorPicker fillSecondaryColorPicker = new ColorPicker(defaultSecondaryFillColor);
		//calculate shape gradient only from these color Picker
	//		final ColorPicker[] colorPickers = {fillColorPicker, fillSecondaryColorPicker};

		//Barra lateral derecha
		/*

		Label actionLabel = new Label("Acciones");
		Button turnButton = new Button("Girar D");
		Button flipHorizontalButton = new Button("Voltear H");
		Button flipVerticalButton = new Button("Voltear v");
		Button duplicateButton = new Button("Duplicar");
		Button divideButton = new Button("Dividir");*/

		//Barra superior
//		Button pushForwardButton = new Button ("Traer al frente");
//		Button pushToBottomButton = new Button ("Enviar al fondo");
//		Label layerLabel = new Label("Capas");
//		ChoiceBox<String> layersChoiceBox = new ChoiceBox<>();
//		RadioButton showLayerRadioButton = new RadioButton("Mostrar");
//		RadioButton hideLayerRadioButton = new RadioButton("Ocultar");
//		ToggleGroup showHideToggle = new ToggleGroup();	// Para que solo se pueda seleccionar uno
//		Button addLayerButton = new Button("Agregar capa");
//		Button removeLayerButton = new Button("Eliminar capa");

		// Dibujar una figura
        public Point startPoint;
		// StatusBar
		StatusPane statusPane;

		ToggleGroup tools = new ToggleGroup();
		DrawingTool drawingTool;


        public PaintPane(CanvasState canvasState, StatusPane statusPane) {
			this.canvasState = canvasState;
			this.statusPane = statusPane;
			FigureCanvas canvas = new FigureCanvas(canvasState, 800, 600);
			drawingTool = canvas.getDrawingTool();
			LeftVBox test1 = new LeftVBox(drawingTool);
			this.setLeft(test1);
			RightVBox test2 = new RightVBox(drawingTool);
			this.setRight(test2);
			TopHBox test3 = new TopHBox(10);
			this.setTop(test3);
			this.setCenter(canvas);
		}
	}
