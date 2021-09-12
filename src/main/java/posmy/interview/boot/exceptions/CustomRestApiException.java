package posmy.interview.boot.exceptions;

import com.sun.istack.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import posmy.interview.boot.models.RestApiErrorResponse;

public class CustomRestApiException extends Exception {

    private HttpStatus statusCode;

    public CustomRestApiException(String errorMessage, HttpStatus statusCode) {
        super(errorMessage);
        this.statusCode = statusCode;
    }

    public ResponseEntity toHttpResponseEntity(@Nullable WebRequest request) {
        return RestApiErrorResponse.fromException(this, request).toHttpResponseEntity();
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }
}
