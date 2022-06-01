package posmy.interview.boot.exception;

/**
 * Common bad request exception, MUST extends RuntimeException or any exception beside Exception
 * otherwise the globalexceptionhandler will take ExceptionHandler as priority if you throw this exception
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
