package posmy.interview.boot.model.entity;

import lombok.Data;
import posmy.interview.boot.helper.IdHelper;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "Book")
@Table(name = "book_tab")
public class BookEntity {

    @Id
    @Column(name = "bt_id", updatable = false)
    private String bookId;
    @Column(name = "bt_title")
    private String bookTitle;
    @Column(name = "bt_genre")
    private String genre;
    @Column(name = "bt_status")
    private String status;
    @Column(name = "bt_record_create_date")
    private Date dateCreated;
    @Column(name = "bt_record_update_date")
    private Date dateUpdated;

    public BookEntity() {
        this.bookId = IdHelper.generateId();
    }

    public BookEntity(String bookTitle, String genre, String status, Date dateCreated, Date dateUpdated) {
        this.bookId = IdHelper.generateId();
        this.bookTitle = bookTitle;
        this.genre = genre;
        this.status = status;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }
}
