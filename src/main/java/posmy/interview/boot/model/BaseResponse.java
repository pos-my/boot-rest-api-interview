package posmy.interview.boot.model;

public class BaseResponse {
  private String errCode;
  private String errMsg;
  private String status;
  private String message;

  public BaseResponse() {
  }

  public String getErrCode() {
    return errCode;
  }

  public void setErrCode(String errCode) {
    this.errCode = errCode;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "BaseResponse [errCode=" + errCode + ", errMsg=" + errMsg + ", message=" + message + ", status=" + status
        + "]";
  }
}
