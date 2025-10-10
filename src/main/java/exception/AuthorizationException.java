package exception;

/**
 * Exception thrown when user doesn't have permission
 */
public class AuthorizationException extends Exception {
    public AuthorizationException(String message) {
        super(message);
    }
}