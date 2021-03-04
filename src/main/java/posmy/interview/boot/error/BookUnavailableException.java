package posmy.interview.boot.error;

import org.springframework.http.HttpStatus;

public class BookUnavailableException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final int ERROR_CODE = 10003;
    private static final String MESSAGE = "Trying to borrow unavailable book ID: ";

    public BookUnavailableException(String id) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+id);
    }

    public BookUnavailableException(String id, Throwable cause) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+id, cause);
    }
}
