package posmy.interview.boot.exception;

public class BookNotFoundException extends BaseRuntimeException {
    public BookNotFoundException(Object... messageParams) {
        super(messageParams);
    }
}
