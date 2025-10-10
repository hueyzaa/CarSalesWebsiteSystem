package exception;

/**
 * Exception thrown when a requested resource is not found
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String resourceType, int resourceId) {
        super(String.format("%s with ID %d not found", resourceType, resourceId));
    }
}