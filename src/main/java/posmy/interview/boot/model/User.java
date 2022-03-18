package posmy.interview.boot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserState;

import javax.persistence.*;
import java.time.Instant;

@Data
@Builder
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private UserState state;

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
