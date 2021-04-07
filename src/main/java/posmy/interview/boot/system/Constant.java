package posmy.interview.boot.system;

public class Constant {

    public static final String SEC_ROLE_PREFIX = "ROLE_";

    public enum BookState {
        AVAILABLE,
        BORROWED
    }

    public enum UserRole {
        ADMIN,
        LIBRARIAN,
        MEMBER
    }
}
