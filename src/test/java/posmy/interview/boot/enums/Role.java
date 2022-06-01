package posmy.interview.boot.enums;

public enum Role {
    ALL("ALL"),
    LIBRARIAN("LIBRARIAN"),
    MEMBER("MEMBER"),
    ;

    private final String description;

    Role(String description) {
        this.description = description;
    }

}
