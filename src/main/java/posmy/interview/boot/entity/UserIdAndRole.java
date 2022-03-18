package posmy.interview.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import posmy.interview.boot.enums.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIdAndRole {
    private Long id;

    private UserRole role;
}
