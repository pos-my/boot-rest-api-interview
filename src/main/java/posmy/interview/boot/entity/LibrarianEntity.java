package posmy.interview.boot.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "LIBRARIAN")
public class LibrarianEntity {

    @Id
    @Column(name = "LIB_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long libId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;
}
