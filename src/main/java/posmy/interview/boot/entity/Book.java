package posmy.interview.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import posmy.interview.boot.enums.BookStatus;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(indexes = @Index(name = "idx_name", columnList = "name"))
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String desc;

    private String imageUrl;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private BookStatus status;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BorrowRecord> borrowRecords;

    @Column(nullable = false)
    private ZonedDateTime lastUpdateDt;

    @PrePersist
    @PreUpdate
    public void updatedOn() {
        this.lastUpdateDt = ZonedDateTime.now();
    }
}
