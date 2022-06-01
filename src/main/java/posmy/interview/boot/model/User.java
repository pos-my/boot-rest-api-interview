package posmy.interview.boot.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import posmy.interview.boot.snowflake.SnowflakeId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Entity(name = "T_USER")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@ToString
public class User implements SnowflakeId<Long> {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;


    //don't allow it return to user
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;


    @ManyToMany(fetch = EAGER)
    @Singular(value = "roles")
    private List<Role> roles = new ArrayList<>();


}
