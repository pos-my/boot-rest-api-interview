package posmy.interview.boot.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String createdBy;
    private Date createdDate = new Date();
    private String updatedBy;
    private Date updatedDate;

}
