package posmy.interview.boot.exception;

public class InvalidBookActionException extends BaseRuntimeException {
    public InvalidBookActionException(Object... messageParams) {
        super(messageParams);
    }
}
