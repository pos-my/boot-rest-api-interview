package posmy.interview.boot.model.database;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int userId;

    @Column(name = "name")
    private String userName;

    @Column(name = "role")
    private String userRole;

    @Column(name = "password")
    private String userPassword;

    @Column(name = "status")
    private long status;

    @Column(name = "record_create_date")
    private Timestamp recordCreateDate;

    @Column(name = "record_update_date")
    private Timestamp recordUpdateDate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public Timestamp getRecordCreateDate() {
        return recordCreateDate;
    }

    public void setRecordCreateDate(Timestamp recordCreateDate) {
        this.recordCreateDate = recordCreateDate;
    }

    public Timestamp getRecordUpdateDate() {
        return recordUpdateDate;
    }

    public void setRecordUpdateDate(Timestamp recordUpdateDate) {
        this.recordUpdateDate = recordUpdateDate;
    }
}
