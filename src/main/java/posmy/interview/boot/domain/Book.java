package posmy.interview.boot.domain;

import lombok.*;
import posmy.interview.boot.enums.BookStatus;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Book")
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String ID = "id";
    private static final String ISBN = "isbn";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String BOOK_STATUS = "book_status";
    private static final String BORROWED_USER = "borrowed_user";
    private static final String CREATED_DATE = "created_date";
    private static final String UPDATED_DATE = "updated_date";

    @Id
    @Column(name = ID)
    private UUID id;

    @Column(name = ISBN, unique = true)
    private String isbn;

    @Column(name = TITLE)
    private String title;

    @Column(name = AUTHOR)
    private String author;

    @Column(name = BOOK_STATUS)
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    @Column(name = BORROWED_USER)
    private UUID borrowedUser;

    @Column(name = CREATED_DATE)
    private Date createdDate;

    @Column(name = UPDATED_DATE)
    private Date updatedDate;

    public Book(String isbn, String title, String author, BookStatus bookStatus) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.bookStatus = bookStatus;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", isbn=" + isbn + ", title=" + title + ", author=" + author + ", bookStatus="
                + bookStatus + ", borrowedUser=" + borrowedUser + ", created_date=" + createdDate
                + ", updated_date=" + updatedDate + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Book that = (Book) o;

        if (!isbn.equals(that.isbn)) {
            return false;
        } else if (!title.equals(that.title)) {
            return false;
        } else if (!author.equals(that.author)) {
            return false;
        } else if (!bookStatus.equals(that.bookStatus)) {
            return false;
        } else if (borrowedUser != null) {
            if (!borrowedUser.equals(that.borrowedUser)) {
                return false;
            }
            return false;
        } else {
            return true;
        }
    }
}
