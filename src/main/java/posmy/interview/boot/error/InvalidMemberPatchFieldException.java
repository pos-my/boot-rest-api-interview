package posmy.interview.boot.error;

import org.springframework.http.HttpStatus;

public class InvalidMemberPatchFieldException extends BusinessException {

    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final int ERROR_CODE = 10002;
    private static final String MESSAGE = "Invalid MemberPatchField enum: ";

    public InvalidMemberPatchFieldException(String field) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+field);
    }

    public InvalidMemberPatchFieldException(String field, Throwable cause) {
        super(HTTP_STATUS, ERROR_CODE, MESSAGE+field, cause);
    }
}
