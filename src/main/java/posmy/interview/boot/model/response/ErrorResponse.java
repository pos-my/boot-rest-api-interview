package posmy.interview.boot.model.response;

public class ErrorResponse {

    private Error error;

    public void setError(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    public ErrorResponse(Error error){
        this.error = error;
    }

    public static class Error {
        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Builder{
        private String code;
        private String message;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponse build(){
            Error error = new Error();
            error.setCode(this.code);
            error.setMessage(this.message);
            return new ErrorResponse(error);
        }
    }

}
