package posmy.interview.boot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import posmy.interview.boot.snowflake.SnowflakeId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "T_ROLE")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@ToString
public class Role implements SnowflakeId<Long> {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

}
