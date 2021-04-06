package posmy.interview.boot.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import posmy.interview.boot.system.Constant;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "BOOKS")
public class Book extends BaseModel {

    @Id
    @GeneratedValue(generator = ID_GENERATOR_NAME)
    @GenericGenerator(name = ID_GENERATOR_NAME, strategy = ID_GENERATOR_STRATEGY)
    @Column(name = "ID")
    private String id;

    @NotBlank(message = "Book name cannot be blank")
    @Column(name = "NAME")
    private String name;

    @NotBlank(message = "Book status cannot be blank")
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Constant.BookState status;
}
