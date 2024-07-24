package exception;

import java.nio.file.AccessDeniedException;

public class AuthorizationFailedException extends AccessDeniedException {
    public AuthorizationFailedException(String message) {
        super(message);
    }
}
