package posmy.interview.boot.exception;

public class UserNotFoundException extends BaseRuntimeException {
    public UserNotFoundException(Object... messageParams) {
        super(messageParams);
    }
}
