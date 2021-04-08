package posmy.interview.boot.exception;

public class BookAlreadyBorrowedException extends BaseRuntimeException {
    public BookAlreadyBorrowedException(Object... messageParams) {
        super(messageParams);
    }
}
