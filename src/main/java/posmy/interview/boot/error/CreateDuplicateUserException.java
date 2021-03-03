package posmy.interview.boot.error;

import org.springframework.http.HttpStatus;

public class CreateDuplicateUserException extends BusinessException {
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final int ERROR_CODE = 10001;
    private static final String MESSAGE = "Trying to create user which already exists: ";

    public CreateDuplicateUserException(String user) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+user);
    }

    public CreateDuplicateUserException(String user, Throwable cause) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+user, cause);
    }
}
