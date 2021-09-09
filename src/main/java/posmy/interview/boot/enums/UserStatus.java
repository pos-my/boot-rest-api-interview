package posmy.interview.boot.enums;

public enum UserStatus {
  ACTIVE("ACTIVE"),
  DISABLED("DISABLED");
  
  private final String code;
  
  private UserStatus(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
