package posmy.interview.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "custom_users", indexes = @Index(name = "idx_username", columnList = "username"))
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MyUser {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @Column(nullable = false)
    private Boolean enabled = Boolean.TRUE;

    private String email;

    private String authority;

    @Column(nullable = false)
    private ZonedDateTime createDt;

    @Column(nullable = false)
    private ZonedDateTime lastUpdateDt;

    @PrePersist
    public void createdOn() {
        this.createDt = ZonedDateTime.now();
        updatedOn();
    }

    @PreUpdate
    public void updatedOn() {
        this.lastUpdateDt = ZonedDateTime.now();
    }
}
