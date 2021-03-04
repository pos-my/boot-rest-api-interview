package posmy.interview.boot.error;

import org.springframework.http.HttpStatus;

public class BorrowRecordMismatchException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final int ERROR_CODE = 10005;
    private static final String MESSAGE = "Trying to return book borrowed by others with ID, username: ";

    public BorrowRecordMismatchException(String id, String username) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+id+", "+username);
    }

    public BorrowRecordMismatchException(String id, String username, Throwable cause) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+id+", "+username, cause);
    }
}
