package posmy.interview.boot.exception;

public class UsernameNotFoundException extends BaseRuntimeException {
    public UsernameNotFoundException(Object... messageParams) {
        super(messageParams);
    }
}
