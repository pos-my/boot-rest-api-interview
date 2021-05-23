package posmy.interview.boot.entity;

import javax.persistence.*;

@Entity
public class UsersAuthorities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "authorities_id")
    private Authorities authorities;
}
