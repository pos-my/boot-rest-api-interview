package posmy.interview.boot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHelper {

    @ExceptionHandler(value = { ResourceNotFoundException.class })
    public ResponseEntity<Object> handleResourceNotFoundExceptionException(ResourceNotFoundException ex) {
        return new ResponseEntity<Object>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }

    

    @ExceptionHandler(value = { ResourceAlreadyExists.class })
    public ResponseEntity<Object> handleResourceAlreadyExistsException(ResourceAlreadyExists ex) {

        return new ResponseEntity<Object>(ex.getMessage(),HttpStatus.BAD_REQUEST);

    }

    

    @ExceptionHandler(value = { CustomException.class })
    public ResponseEntity<Object> handleCustomException (CustomException  ex) {

        return new ResponseEntity<Object>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

    }

}