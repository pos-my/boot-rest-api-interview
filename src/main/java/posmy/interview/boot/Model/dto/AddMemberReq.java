package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class AddMemberReq  {

    @NotNull(message = "username cannot be null")
    @JsonProperty("username")
    private String username;

    @NotNull(message = "password cannot be null")
    @JsonProperty("password")
    private String password;

}
