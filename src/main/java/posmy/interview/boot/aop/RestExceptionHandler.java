package posmy.interview.boot.aop;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.IllegalFormatException;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity handleAll(Exception ex, WebRequest request) {
        HttpStatus status = INTERNAL_SERVER_ERROR;
        return buildResponseEntity(new APIError(status, ex.getLocalizedMessage(), getRequestURI(request)));
    }

    @ExceptionHandler({UnsupportedOperationException.class, NotImplementedException.class})
    public ResponseEntity handleUnsupportedOperationException(Exception ex, WebRequest request) {
        return buildResponseEntity(new APIError(NOT_IMPLEMENTED, ex.getLocalizedMessage(), getRequestURI(request)));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, IllegalFormatException.class})
    public ResponseEntity handleIllegalArgument(Exception ex, WebRequest request) {
        return buildResponseEntity(new APIError(UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(), getRequestURI(request)));
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity handleAccessDeniedException(Exception ex, WebRequest request) {
        return buildResponseEntity(new APIError(FORBIDDEN, ex.getLocalizedMessage(), new ArrayList(), getRequestURI(request)));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        return buildResponseEntity(new APIError(NOT_FOUND, ex.getMessage(), getRequestURI(request)));
    }

    @Override
    protected ResponseEntity handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        super.handleExceptionInternal(ex, body, headers, status, request);
        return buildResponseEntity(new APIError(status, ex.getLocalizedMessage(), getRequestURI(request)));
    }

    private ResponseEntity buildResponseEntity(APIError apiError) {
        return new ResponseEntity(apiError, new HttpHeaders(), apiError.getStatus());
    }

    private String getRequestURI(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest hsr = ((ServletWebRequest) request).getRequest();
            return hsr.getRequestURI();
        } else {
            logger.warn("Failed to get URI for request: " + request.toString());
        }

        return null;
    }
}
