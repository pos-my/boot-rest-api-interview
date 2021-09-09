package posmy.interview.boot.model.librarian;

public class AddMemberReq {  
  private String usrId;
  private String name;
  private String email;
  private String role;
  private String status;
  
  public String getUsrId() {
    return usrId;
  }
  public void setUsrId(String usrId) {
    this.usrId = usrId;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getRole() {
    return role;
  }
  public void setRole(String role) {
    this.role = role;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  @Override
  public String toString() {
    return "AddMemberReq [email=" + email + ", name=" + name + ", role=" + role + ", status=" + status + ", usrId="
        + usrId + "]";
  }
  
  
  
}
