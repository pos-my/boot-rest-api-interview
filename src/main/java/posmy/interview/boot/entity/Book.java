package posmy.interview.boot.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "book")
public class Book implements JpaEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Status status;

    @Override
    public Long getId() {
        return id;
    }

    public enum Status {
        AVAILABLE,
        BORROWED
    }
}
