package posmy.interview.boot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Books implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long book_id;

    private String title;

    private String publisher;

    private String year_establish;

    private String status;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Member> member;

    public Books(String title, String publisher, String year_establish, String status) {
        this.title = title;
        this.publisher = publisher;
        this.year_establish = year_establish;
        this.status = status;
    }
}
