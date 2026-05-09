package andrey.project.warehouse.exception;

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException(String message) {
        super(message);
    }
}
