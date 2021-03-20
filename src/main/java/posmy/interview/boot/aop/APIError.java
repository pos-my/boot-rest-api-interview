package posmy.interview.boot.aop;


import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class APIError {

    private Date timestamp;
    private HttpStatus status;
    private String message;
    private Object[] errors;
    private String uri;

    public APIError(HttpStatus status, String message, List errors, String uri) {
        this.setTimestamp(new Date());
        this.setStatus(status);
        this.setMessage(message);
        this.setErrors(errors.toArray());
        this.setUri(uri);
    }

    public APIError(HttpStatus status, String message, List errors) {
        this(status, message, "");
        this.setErrors(errors.toArray());
    }

    public APIError(HttpStatus status, String message, String uri) {
        this(status, message, new ArrayList<>(), uri);
    }

    public APIError(HttpStatus status, String message) {
        this(status, message, "");
        this.setErrors(new Object[0]);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getErrors() {
        return errors;
    }

    public void setErrors(Object[] errors) {
        this.errors = errors;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
