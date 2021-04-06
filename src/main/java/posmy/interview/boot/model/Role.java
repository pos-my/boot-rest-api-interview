package posmy.interview.boot.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
@Table(name = "ROLES")
public class Role extends BaseModel {

    @Id
    @GeneratedValue(generator = ID_GENERATOR_NAME)
    @GenericGenerator(name = ID_GENERATOR_NAME, strategy = ID_GENERATOR_STRATEGY)
    @Column(name = "ID")
    private String id;

    @NotBlank(message = "Role name cannot be blank")
    @Column(name = "NAME")
    private String name;

    @OneToMany
    @JoinTable(name = "AUTHORITIES",
            joinColumns = @JoinColumn(name = "ROLEID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "USERID"))
    private List<User> users;
}
