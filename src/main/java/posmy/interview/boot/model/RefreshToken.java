package posmy.interview.boot.model;

import lombok.Data;
import posmy.interview.boot.enums.BookStatus;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id", unique = true)
    private Long userId;
    @Column(name = "token")
    private String token;
    @Column(name = "expiry_time", insertable = true, updatable = false)
    private Instant expiryTime;
    @Column(name = "created_time", insertable = false, updatable = false)
    public Instant createdTime;

    @PrePersist
    void createTime() {
        this.createdTime = Instant.now();
    }
}
