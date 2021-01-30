package posmy.interview.boot.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import posmy.interview.boot.model.ApiError;

import java.time.LocalDateTime;

@ControllerAdvice
public class NoDataExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { NoDataFoundException.class})
    protected ResponseEntity<Object> handleException(NoDataFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), LocalDateTime.now());
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
