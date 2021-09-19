package posmy.interview.boot.exception;

public class ResourceNotFoundException  extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}