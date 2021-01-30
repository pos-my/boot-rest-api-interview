package posmy.interview.boot.model;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class UpdateMemberRequest {
    @NotNull
    private long memberId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
}
