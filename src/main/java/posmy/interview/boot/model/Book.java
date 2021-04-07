package posmy.interview.boot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import posmy.interview.boot.system.Constant;

import javax.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOOKS")
public class Book extends BaseModel {

    @Id
    @GeneratedValue(generator = ID_GENERATOR_NAME)
    @GenericGenerator(name = ID_GENERATOR_NAME, strategy = ID_GENERATOR_STRATEGY)
    @Column(name = "ID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Constant.BookState status;
}
