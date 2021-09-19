package posmy.interview.boot.exception;

public class ResourceAlreadyExists  extends RuntimeException {

    public ResourceAlreadyExists(String message) {
        super(message);
    }

}