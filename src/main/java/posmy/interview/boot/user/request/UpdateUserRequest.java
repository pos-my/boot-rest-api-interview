package posmy.interview.boot.user.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String password;
    private String role;
}
