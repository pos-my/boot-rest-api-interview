package posmy.interview.boot.Model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import posmy.interview.boot.Model.Users;

import java.util.List;

@Builder
@Data
public class GetMembersResp {

    @JsonProperty("members")
    private List<Users> members;


}
