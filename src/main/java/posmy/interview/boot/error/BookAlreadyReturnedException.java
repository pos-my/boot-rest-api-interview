package posmy.interview.boot.error;

import org.springframework.http.HttpStatus;

public class BookAlreadyReturnedException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final int ERROR_CODE = 10004;
    private static final String MESSAGE = "Trying to return book not being borrowed with ID: ";

    public BookAlreadyReturnedException(String id) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+id);
    }

    public BookAlreadyReturnedException(String id, Throwable cause) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+id, cause);
    }
}
