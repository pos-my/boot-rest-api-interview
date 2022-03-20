package posmy.interview.boot.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import posmy.interview.boot.domains.UserRoles;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserReq {
    private String userName;

    private UserRoles role;

    private String status;
}
