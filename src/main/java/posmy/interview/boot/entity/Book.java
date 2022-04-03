package posmy.interview.boot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String name;

    public Book() {
    }

    public Book(Long id, String title) {
        this.id  = id;
        this.name = title;
    }
}