package backend;

public class FigureNotFoundException extends RuntimeException {
    public FigureNotFoundException(String message) {
        super(message);
    }
    public FigureNotFoundException() {
    super();
  }
}
