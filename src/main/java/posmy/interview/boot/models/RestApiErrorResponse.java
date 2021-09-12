package posmy.interview.boot.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.HtmlUtils;
import posmy.interview.boot.exceptions.CustomRestApiException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RestApiErrorResponse {

    public int code;
    public String status;
    public String message;
    public String request;
    public List<String> stackTrace;

    public RestApiErrorResponse(String message) {
        this.code = HttpStatus.BAD_REQUEST.value();
        this.status = HttpStatus.BAD_REQUEST.getReasonPhrase();
        this.message = message;
        this.request = "";
        this.stackTrace = new ArrayList<>();
    }

    public RestApiErrorResponse setCode(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.status = httpStatus.getReasonPhrase();
        return this;
    }

    public RestApiErrorResponse addStackTrace(Collection<String> stackTrace) {
        this.stackTrace.addAll(stackTrace);
        return this;
    }

    public RestApiErrorResponse setRequest(@Nullable WebRequest request) {
        if (request != null) {
            this.request = request.getDescription(false);
        }
        return this;
    }

    public ResponseEntity toHttpResponseEntity() {
        return ResponseEntity.status(this.code).contentType(MediaType.APPLICATION_JSON).body(this);
    }

    public static RestApiErrorResponse fromException(Exception ex, @Nullable WebRequest request) {
        RestApiErrorResponse error = (new RestApiErrorResponse(HtmlUtils.htmlEscape(ex.getMessage()))).setRequest(request);
        if (ex instanceof CustomRestApiException) {
            error.setCode(((CustomRestApiException)ex).getStatusCode());
        }

        return error;
    }
}
