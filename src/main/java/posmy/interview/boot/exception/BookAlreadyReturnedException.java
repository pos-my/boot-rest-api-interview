package posmy.interview.boot.exception;

public class BookAlreadyReturnedException extends BaseRuntimeException {
    public BookAlreadyReturnedException(Object... messageParams) {
        super(messageParams);
    }
}
