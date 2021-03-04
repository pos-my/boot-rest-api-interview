package posmy.interview.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(indexes = @Index(name = "idx_username", columnList = "username"))
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecord implements Comparable<BorrowRecord> {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private Long borrowTimestamp;

    private Long returnTimestamp;

    @PrePersist
    public void borrowTimestampOn() {
        this.borrowTimestamp = System.currentTimeMillis();
    }

    @PreUpdate
    public void returnTimestampOn() {
        this.returnTimestamp = System.currentTimeMillis();
    }

    @Override
    public int compareTo(BorrowRecord o) {
        if (this.borrowTimestamp > o.borrowTimestamp)
            return -1;
        else if (this.borrowTimestamp < o.borrowTimestamp)
            return 1;
        return 0;
    }
}
