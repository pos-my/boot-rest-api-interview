package posmy.interview.boot.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import posmy.interview.boot.dto.response.ErrorDto;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDto> handleValidationException(Exception e) {
        log.warn("Validation caught: " + e.getMessage());

        return new ResponseEntity<>(
            ErrorDto.builder()
                .responseCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build(),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(Exception e) {
        log.warn("Not found caught: " + e.getMessage());

        return new ResponseEntity<>(
            ErrorDto.builder()
                .responseCode(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build(),
            HttpStatus.NOT_FOUND
        );
    }
}
