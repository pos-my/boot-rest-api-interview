package posmy.interview.boot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserStatus;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";
    private static final String STATUS = "status";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

    @Id
    @Column(name = ID)
    private UUID id;

    @Column(name = USERNAME, unique = true)
    private String username;

    @Column(name = PASSWORD)
    private String password;

    @Column(name = ROLE)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = STATUS)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = CREATED_DATE)
    private Date createdDate;

    @Column(name = UPDATED_DATE)
    private Date updatedDate;

    public User(String username, String password, UserRole role, UserStatus status) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", role=" + role
                + ", status=" + status + ", created_date=" + createdDate + ", updated_date=" + updatedDate + "]";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        User that = (User) o;

        if (!username.equals(that.username)) {
            return false;
        } else if (!role.equals(that.role)) {
            return false;
        } else if (!status.equals(that.status)) {
            return false;
        } else {
            return true;
        }
    }
}
