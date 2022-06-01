package posmy.interview.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import posmy.interview.boot.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserToken {
    String accessToken;
    Role role;
}
