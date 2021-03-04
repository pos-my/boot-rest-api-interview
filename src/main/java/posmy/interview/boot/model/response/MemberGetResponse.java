package posmy.interview.boot.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MemberGetResponse implements BaseResponse {
    private List<UserDetailsDto> members;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class UserDetailsDto {
        private Long id;
        private String username;
        private String email;
    }
}
