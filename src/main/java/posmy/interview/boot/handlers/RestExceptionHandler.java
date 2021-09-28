package posmy.interview.boot.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import posmy.interview.boot.exceptions.CustomRestApiException;
import posmy.interview.boot.models.RestApiErrorResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Autowired
    public RestExceptionHandler() {}

    @ExceptionHandler()
    @ResponseBody
    public final ResponseEntity<?> handleAndLogException(Exception ex, WebRequest request) {
        logger.error("WebRequest to {} failed with {} with message {}", request.getContextPath(), ex.getClass().getName(), ex.getMessage());
        if (ex instanceof CustomRestApiException) {
            return ((CustomRestApiException) ex).toHttpResponseEntity(request);
        } else {
            return RestApiErrorResponse
                    .fromException(ex, request)
                    .toHttpResponseEntity();
        }
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("WebRequest to {} failed with {} with message {}", request.getContextPath(), ex.getClass().getName(), ex.getMessage());
        return RestApiErrorResponse
                .fromException(ex, request)
                .toHttpResponseEntity();
    }
}
