package posmy.interview.boot.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;
    protected static final String ID_GENERATOR_NAME = "UUID";
    protected static final String ID_GENERATOR_STRATEGY = "org.hibernate.id.UUIDHexGenerator";

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Version
    private Long version;
}
