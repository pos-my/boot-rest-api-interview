package posmy.interview.boot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@Entity
@Table(name = "ROLES")
public class Role extends BaseModel {

    @Id
    @GeneratedValue(generator = ID_GENERATOR_NAME)
    @GenericGenerator(name = ID_GENERATOR_NAME, strategy = ID_GENERATOR_STRATEGY)
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @JsonBackReference
    @OneToMany
    @JoinTable(name = "AUTHORITIES",
            joinColumns = @JoinColumn(name = "ROLEID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "USERID"))
    private List<User> users;
}
