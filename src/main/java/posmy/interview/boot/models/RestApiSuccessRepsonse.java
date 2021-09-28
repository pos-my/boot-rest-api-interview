package posmy.interview.boot.models;

import org.springframework.http.HttpStatus;

public class RestApiSuccessRepsonse {

    public int code;
    public String message;

    public RestApiSuccessRepsonse(HttpStatus status, String message) {
        this.code = status.value();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
