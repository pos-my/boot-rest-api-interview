package posmy.interview.boot.enums;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    ;

    private final String description;

    HttpMethod(String description) {
        this.description = description;
    }

}
