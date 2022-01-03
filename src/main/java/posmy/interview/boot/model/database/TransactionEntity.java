package posmy.interview.boot.model.database;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int transactionId;

//    @OneToOne
//    @JoinColumn(name = "book_id", referencedColumnName = "id")
//    private BookEntity bookEntity;
//
//    @OneToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private UserEntity userEntity;

    @Column(name = "status")
    private String status;

    @Column(name = "record_create_date")
    private Timestamp recordCreateDate;

    @Column(name = "record_update_date")
    private Timestamp recordUpdateDate;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

//    public BookEntity getBookEntity() {
//        return bookEntity;
//    }
//
//    public void setBookEntity(BookEntity bookEntity) {
//        this.bookEntity = bookEntity;
//    }
//
//    public UserEntity getUserEntity() {
//        return userEntity;
//    }
//
//    public void setUserEntity(UserEntity userEntity) {
//        this.userEntity = userEntity;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
