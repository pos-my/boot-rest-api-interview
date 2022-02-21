package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddMemberResp {

    @JsonProperty("memberId")
    private Long memberId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

}
