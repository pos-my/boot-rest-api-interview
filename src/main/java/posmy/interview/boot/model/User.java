package posmy.interview.boot.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "USER_TABLE")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERID")
    private Integer userId;
    @Column(name = "USERNAME")
    private String userName;
    @Column(name = "USERPASSWORD")
    private String userPassword;
    @Column(name = "USERROLE")
    private String userRole;
}
