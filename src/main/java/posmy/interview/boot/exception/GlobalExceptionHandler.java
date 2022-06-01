package posmy.interview.boot.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Global capture all the different exception and handler with standard logic
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BadRequestException.class)
    @ResponseBody
    public ErrorHandler handleBadRequestException(BadRequestException ex) {
        return getErrorHandler(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorHandler handleAuthenticationException(AuthenticationException ex) {
        return getErrorHandler(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorHandler handleInterServerException(Exception ex) {
        //handle internal error
        var uuid = UUID.randomUUID();
        log.error("internal server exception id: " + uuid, ex);
        return getErrorHandler("Internal error, please contact support and provide uuid: " + uuid);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorHandler handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        //handle invalid json input
        return getErrorHandler("Invalid input, please verify");
    }


    private ErrorHandler getErrorHandler(String message) {
        return ErrorHandler.builder()
                .error(message)
                .build();
    }

}
