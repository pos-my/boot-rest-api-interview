package posmy.interview.boot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import posmy.interview.boot.snowflake.SnowflakeId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "T_BOOK")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@ToString
public class Book implements SnowflakeId<Long> {
    @Id
    private Long id;

    //book name should be allow duplicate because that is possible, especially in china
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private Integer publishYear;

    //avoid this field get set when create/update
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Long borrowBy;


}
