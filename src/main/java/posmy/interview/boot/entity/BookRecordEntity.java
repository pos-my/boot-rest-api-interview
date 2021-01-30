package posmy.interview.boot.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "BOOK_RECORD")
public class BookRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "BOOK_ID")
    private long bookId;
}
