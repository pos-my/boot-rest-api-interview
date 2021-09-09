package posmy.interview.boot.enums;

public enum UserRole {
  LIBRARIAN("LIBRARIAN"), 
  MEMBER("MEMBER");

  private final String code;

  private UserRole(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
