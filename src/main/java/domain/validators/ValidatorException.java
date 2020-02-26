package domain.validators;
import domain.validators.CatalogException;


public class ValidatorException extends CatalogException {
    public ValidatorException(String message) {
        super(message);
    }
    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }
    public ValidatorException(Throwable cause) {
        super(cause);
    }
}
