package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@Builder
public class UpdateMemberReq {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("enabled")
    @Pattern(regexp = "(?:^0$|^1$)", message = "enabled only allow 0 or 1")
    private String enabled;

    @JsonProperty("role")
    @Pattern(regexp = "(?:^USER$|^LIBRARIAN$)", message = "role only USER or LIBRARIAN")
    private String role;

}
