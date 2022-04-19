package custom_exceptions;

public class NoNewNeighbourException extends Exception {

    public NoNewNeighbourException(String message) {
        super(message);
    }

    public NoNewNeighbourException() {
        super();
    }
}
