package posmy.interview.boot.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
@Table(name = "USERS")
public class User extends BaseModel {

    @Id
    @GeneratedValue(generator = ID_GENERATOR_NAME)
    @GenericGenerator(name = ID_GENERATOR_NAME, strategy = ID_GENERATOR_STRATEGY)
    @Column(name = "ID")
    private String id;

    @NotBlank(message = "Login ID cannot be blank")
    @Column(name = "LOGINID")
    private String loginId;

    @NotBlank(message = "User name cannot be blank")
    @Column(name = "NAME")
    private String name;

    @NotBlank(message = "Password cannot be blank")
    @Column(name = "PASS")
    private String pass;

    @OneToMany
    @JoinTable(name = "AUTHORITIES",
            joinColumns = @JoinColumn(name = "USERID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLEID"))
    private List<Role> roles;
}
