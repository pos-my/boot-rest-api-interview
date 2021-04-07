package posmy.interview.boot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User extends BaseModel {

    @Id
    @GeneratedValue(generator = ID_GENERATOR_NAME)
    @GenericGenerator(name = ID_GENERATOR_NAME, strategy = ID_GENERATOR_STRATEGY)
    @Column(name = "ID")
    private String id;

    @Column(name = "LOGINID")
    private String loginId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASS")
    private String pass;

    @JsonManagedReference
    @OneToMany
    @JoinTable(name = "AUTHORITIES",
            joinColumns = @JoinColumn(name = "USERID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLEID"))
    private List<Role> roles;

    @OneToMany
    @JoinTable(name = "BORROWED_BOOKS",
            joinColumns = @JoinColumn(name = "USERID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "BOOKID"))
    private List<Book> borrowedBooks;
}
