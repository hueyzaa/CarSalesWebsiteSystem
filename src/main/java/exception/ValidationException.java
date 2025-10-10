package exception;

/**
 * Exception thrown when input validation fails
 */
public class ValidationException extends Exception {
    private String fieldName;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}