package posmy.interview.boot.models.daos;

import posmy.interview.boot.models.dtos.book.BookStatus;

import javax.persistence.*;

@Entity
@Table(name = "BOOKS")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "PUBLISHED_YEAR")
    private String publishedYear;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BookStatus status;

    public Book() {
    }

    public Book(String title, String author, String publishedYear, BookStatus status) {
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.status = status;
    }

    public long getBookId() {
        return id;
    }

    public void setBookId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(String publishedYear) {
        this.publishedYear = publishedYear;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Book [bookId=" + id + ", title=" + title + ", author=" + author + ", publishYear=" + publishedYear +
                ", status=" + status.name() + "]";
    }
}
