package posmy.interview.boot.model.entity;

import lombok.Data;
import posmy.interview.boot.helper.IdHelper;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "User")
@Table(name = "user_tab")
public class UserEntity {

    @Id
    @Column(name = "ut_id", updatable = false)
    private String userId;
    @Column(name = "ut_username")
    private String userName;
    @Column(name = "ut_email")
    private String email;
    @Column(name = "ut_password")
    private String passowrd;
    @Column(name = "ut_role")
    private String role;
    @Column(name = "ut_status")
    private String status;
    @Column(name = "ut_record_create_date")
    private Date dateCreated;
    @Column(name = "ut_record_update_date")
    private Date dateUpdated;

    public UserEntity() {
        this.userId = IdHelper.generateId();
    }

    public UserEntity(String userName, String email, String passowrd, String role, String status, Date dateCreated, Date dateUpdated) {
        this.userId = IdHelper.generateId();
        this.userName = userName;
        this.email = email;
        this.passowrd = passowrd;
        this.role = role;
        this.status = status;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }
}
