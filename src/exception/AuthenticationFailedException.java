package exception;

import java.nio.file.AccessDeniedException;

public class AuthenticationFailedException extends AccessDeniedException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
