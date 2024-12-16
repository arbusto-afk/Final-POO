	package frontend;
	
	import backend.CanvasState;
	import backend.model.*;
	import frontend.DrawingTool.DrawingTool;
	import frontend.Events.FigureCanvas;
	import frontend.Events.LeftVBox;
	import frontend.Events.RightVBox;
	import frontend.Events.TopHBox;
	import javafx.scene.control.*;
	import javafx.scene.layout.BorderPane;


	public class PaintPane extends BorderPane {

		CanvasState canvasState;

		// Dibujar una figura
        public Point startPoint;
		// StatusBar
		StatusPane statusPane;

		ToggleGroup tools = new ToggleGroup();
		DrawingTool drawingTool;


        public PaintPane(CanvasState canvasState, StatusPane statusPane) {
			this.canvasState = canvasState;
			this.statusPane = statusPane;
			FigureCanvas canvas = new FigureCanvas(canvasState, 800, 600, statusPane);
			drawingTool = canvas.getDrawingTool();
			LeftVBox test1 = new LeftVBox(drawingTool, statusPane);
			this.setLeft(test1);
			RightVBox test2 = new RightVBox(drawingTool);
			this.setRight(test2);
			TopHBox test3 = new TopHBox(10, drawingTool);
			this.setTop(test3);
			this.setCenter(canvas);
		}
	}
