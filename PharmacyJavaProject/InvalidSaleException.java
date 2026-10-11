// Our FIRST custom exception.
// "extends Exception" means it is a "checked" exception,
// so Java forces us to catch it with try / catch or to declare it with "throws".
// We use it when a value is invalid (blank name, negative quantity, etc.).
public class InvalidSaleException extends Exception {

    // The constructor receives an error message and sends it to the parent class (Exception).
    public InvalidSaleException(String message) {
        super(message);
    }
}
