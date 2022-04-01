package posmy.interview.boot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateRecordException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -4213467213197012867L;

    public DuplicateRecordException(String message) {
        super(message);
    }
}