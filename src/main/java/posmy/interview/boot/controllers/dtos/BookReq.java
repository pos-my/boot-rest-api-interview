package posmy.interview.boot.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import posmy.interview.boot.domains.BookStatus;
import posmy.interview.boot.domains.UserRoles;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookReq {
    private String bookName;

    private BookStatus status;
}
