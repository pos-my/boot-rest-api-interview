package posmy.interview.boot.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRuntimeException extends RuntimeException {
    private Object[] messageParams;

    public BaseRuntimeException(Object... messageParams) {
        this.messageParams = messageParams;
    }
}
