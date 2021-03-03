package posmy.interview.boot.enums;

public enum MyRole {
    LIBRARIAN("ROLE_LIBRARIAN"),
    MEMBER("ROLE_MEMBER");

    public final String authority;

    MyRole(String authority) {
        this.authority = authority;
    }
}
