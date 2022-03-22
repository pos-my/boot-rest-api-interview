package posmy.interview.boot.constants;

public enum Error {

    INVALID_EMAIL("1000", "Invalid email. Please try again!");

    private final String code;
    private final String description;

    Error(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
