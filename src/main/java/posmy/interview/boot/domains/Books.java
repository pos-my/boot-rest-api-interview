package posmy.interview.boot.domains;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Books implements Serializable {
    static final long serialVersionUID = 1L;
    public static final String STS_BORROWED = "BORROWED";
    public static final String STS_AVAILABLE = "AVAILABLE";

    public Books(String bookName, BookStatus status) {
        this.bookName = bookName;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookid", nullable = false, updatable = false)
    private Long id;

    @Column(name = "bookname", nullable = false, updatable = true)
    private String bookName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookStatus status;

    @Column(name = "createtime", insertable = true, updatable = true)
    private LocalDateTime createTime;

    @Column(name = "updatetime", insertable = false, updatable = true)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
