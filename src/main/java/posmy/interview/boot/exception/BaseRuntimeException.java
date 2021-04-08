package posmy.interview.boot.exception;

import lombok.Getter;

@Getter
public class BaseRuntimeException extends RuntimeException {
    private final Object[] messageParams;

    public BaseRuntimeException(Object... messageParams) {
        this.messageParams = messageParams;
    }
}
