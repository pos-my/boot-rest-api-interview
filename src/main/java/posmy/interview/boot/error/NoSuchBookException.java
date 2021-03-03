package posmy.interview.boot.error;

import org.springframework.http.HttpStatus;

public class NoSuchBookException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final int ERROR_CODE = 10000;
    private static final String MESSAGE = "No such book in the library of ID: ";

    public NoSuchBookException(String id) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+id);
    }

    public NoSuchBookException(String id, Throwable cause) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+id, cause);
    }
}
