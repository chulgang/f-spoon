package exception;

import com.sun.jdi.request.DuplicateRequestException;

public class DuplicateTrialException extends DuplicateRequestException {
    public DuplicateTrialException(String message) {
        super(message);
    }
}
