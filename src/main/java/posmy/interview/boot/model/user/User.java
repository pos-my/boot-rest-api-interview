package posmy.interview.boot.model.user;

public class User {
    private String username;
    private String fullName;
    private String role;
    private String status;
    private String recordCreateDate;
    private String recordUpdateDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getRecordCreateDate() {
        return recordCreateDate;
    }

    public void setRecordCreateDate(String recordCreateDate) {
        this.recordCreateDate = recordCreateDate;
    }

    public String getRecordUpdateDate() {
        return recordUpdateDate;
    }

    public void setRecordUpdateDate(String recordUpdateDate) {
        this.recordUpdateDate = recordUpdateDate;
    }
}
