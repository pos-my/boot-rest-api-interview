package posmy.interview.boot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -4213467213197012867L;

    public RecordNotFoundException(String message) {
        super(message);
    }
}