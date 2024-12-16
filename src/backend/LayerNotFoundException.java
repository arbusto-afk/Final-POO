package backend;

public class LayerNotFoundException extends RuntimeException {
    public LayerNotFoundException(String message) { super(message); }
    public LayerNotFoundException() { super();}
}
