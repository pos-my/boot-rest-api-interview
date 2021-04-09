package posmy.interview.boot.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserDto {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private List<Long> roleIds;
}
