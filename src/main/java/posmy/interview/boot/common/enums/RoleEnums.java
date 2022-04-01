package posmy.interview.boot.common.enums;

public enum RoleEnums {

    LIBRARIAN("LIBRARIAN"), MEMBER("MEMBER");

    private String role = null;

    private RoleEnums(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return this.role;
    }
}