package posmy.interview.boot.enums;

public enum BookStatus {
  
  BORROWED("BORROWED"),
  AVAILABLE("AVAILABLE");
  
  private final String code;

  private BookStatus(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
