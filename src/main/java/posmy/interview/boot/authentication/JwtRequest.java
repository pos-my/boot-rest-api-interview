package posmy.interview.boot.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtRequest {

    private String username;
    private String password;
    private long expiration;
}
