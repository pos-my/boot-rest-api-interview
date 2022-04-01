package posmy.interview.boot.user.request;

import lombok.Data;

@Data
public class NewUserRequest {
    private String username;
    private String password;
    private String role;
}