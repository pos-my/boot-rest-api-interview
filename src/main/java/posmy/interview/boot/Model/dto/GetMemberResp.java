package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Builder
@Data
public class GetMemberResp {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("enabled")
    private Integer enabled;

    @JsonProperty("role")
    private String role;

}
