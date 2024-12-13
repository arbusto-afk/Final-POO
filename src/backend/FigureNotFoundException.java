package backend;

public class FigureNotFoundException extends Exception {
    public FigureNotFoundException(String message) {
        super(message);
    }
    public FigureNotFoundException() {
    super();
  }
}
