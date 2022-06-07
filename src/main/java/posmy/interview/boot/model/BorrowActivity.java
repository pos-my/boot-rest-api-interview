package posmy.interview.boot.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class BorrowActivity {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;
    private Long bookId;
    private BorrowStatus status;
}
