package posmy.interview.boot.model.database;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int bookId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private long status; //it is in sens

    @Column(name = "record_create_date")
    private Timestamp recordCreateDate;

    @Column(name = "record_update_date")
    private Timestamp recordUpdateDate;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public Timestamp getRecordCreateDate() {
        return recordCreateDate;
    }

    public void setRecordCreateDate(Timestamp recordCreateDate) {
        this.recordCreateDate = recordCreateDate;
    }

    public Timestamp getRecordUpdateDate() {
        return recordUpdateDate;
    }

    public void setRecordUpdateDate(Timestamp recordUpdateDate) {
        this.recordUpdateDate = recordUpdateDate;
    }
}
