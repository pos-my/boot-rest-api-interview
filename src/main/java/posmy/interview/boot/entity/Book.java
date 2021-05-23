package posmy.interview.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import posmy.interview.boot.enums.BookStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String author;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "book_status")
    private BookStatus bookStatus = BookStatus.AVAILABLE;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @UpdateTimestamp
    @Column(name = "last_edit_date")
    private LocalDateTime lastEditDate;

    @Column(name = "borrow_by")
    private String borrowBy;
}
