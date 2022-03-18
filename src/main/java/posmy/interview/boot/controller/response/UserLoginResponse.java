package posmy.interview.boot.controller.response;

import lombok.Data;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserState;

@Data
public class UserLoginResponse {
    private Long userId;

    private String username;

    private UserRole role;

    private UserState state;

    private String jwt;
}
