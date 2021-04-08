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

    public enum BookAction {
        BORROW,
        RETURN;

        public static boolean isBorrow(String action) {
            return BORROW.name().equalsIgnoreCase(action);
        }

        public static boolean isReturn(String action) {
            return RETURN.name().equalsIgnoreCase(action);
        }
    }
}
