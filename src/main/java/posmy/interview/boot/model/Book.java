package posmy.interview.boot.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "BOOK_TABLE")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKID")
    private Integer bookId;
    @Column(name = "BOOKTITLE")
    private String bookTitle;
    @Column(name = "BOOKSTATUS")
    private String bookStatus;
}
