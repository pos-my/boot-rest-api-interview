package posmy.interview.boot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import posmy.interview.boot.enums.BookStatus;

import javax.persistence.*;
import java.time.Instant;

@Builder
@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title", unique = true)
    private String title;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookStatus status;
    @Column(name = "created_time", insertable = false, updatable = false)
    public Instant createdTime;
    @Basic(optional = false)
    @Column(name = "updated_time", insertable = false, updatable = false)
    private Instant updatedTime;

    @PrePersist
    void createTime() {
        this.createdTime = this.updatedTime = Instant.now();
    }

    @PreUpdate
    void updatedTime() {
        this.updatedTime = Instant.now();
    }
}
