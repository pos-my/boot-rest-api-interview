package posmy.interview.boot.enums;

public enum ResponseStatus {

  SUCCESS("SUCCESS"),
  FAILED("FAILED");

  private final String code;

  private ResponseStatus(String code) {
      this.code = code;
  }

  public String getCode() {
      return code;
  }
}
