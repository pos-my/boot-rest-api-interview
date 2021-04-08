package posmy.interview.boot.exception;

public class UserAlreadyExistsException extends BaseRuntimeException {
    public UserAlreadyExistsException(Object... messageParams) {
        super(messageParams);
    }
}
