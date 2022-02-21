package posmy.interview.boot.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import posmy.interview.boot.Exception.model.ErrorResp;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(value = GenericException.class)
    protected ResponseEntity<ErrorResp> handleApiFilterServiceException(GenericException exception) {

        log.info("Generic Exception Happened, Code : {}, Detail: {}", exception.getErrorCode(), exception.getErrorMessage());

        return ResponseEntity.status(Integer.parseInt(exception.getErrorCode()))
                .body(new ErrorResp(exception.getErrorCode(), exception.getErrorMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(value = UncaughtException.class)
    protected ResponseEntity<ErrorResp> handleApiFilterServiceException(UncaughtException exception) {

        return ResponseEntity.status(Integer.parseInt(exception.getErrorCode()))
                .body(new ErrorResp(exception.getErrorCode(), exception.getErrorMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        //Get all errors
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.joining(","));


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResp(status.name(), errors));
    }

}
