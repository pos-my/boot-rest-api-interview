package posmy.interview.boot.security.jwt;

import lombok.Data;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserState;

@Data
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String refreshToken;
    private Long userId;
    private String username;
    private UserRole role;
    private UserState state;

    public JwtResponse(String accessToken, String refreshToken, Long userId, String username, UserRole role, UserState state) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.state = state;
    }
}
