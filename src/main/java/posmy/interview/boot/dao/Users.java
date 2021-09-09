package posmy.interview.boot.dao;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "USERS", schema = "C##TEST")
public class Users implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @Column(name = "USR_ID")
  private String usrId;
  
  @Column(name = "NAME")
  private String name;
  
  @Column(name = "EMAIL")
  private String email;
  
  @Column(name = "ROLE")
  private String role;
  
  @Column(name = "STATUS")
  private String status;

  public Users() {
  }

  public Users(String usrId, String name, String email, String role, String status) {
    this.usrId = usrId;
    this.name = name;
    this.email = email;
    this.role = role;
    this.status = status;
  }

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
    return "Users [email=" + email + ", name=" + name + ", role=" + role + ", status=" + status + ", usrId=" + usrId
        + "]";
  }
  
}
