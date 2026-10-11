// Our SECOND custom exception.
// We throw it when we look for (or remove) a sale with an ID that does not exist.
public class SaleNotFoundException extends Exception {

    public SaleNotFoundException(String message) {
        super(message);
    }
}
