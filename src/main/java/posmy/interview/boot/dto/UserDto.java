package posmy.interview.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import posmy.interview.boot.model.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private Long id;
	private String name;
	private String username;
	private String password;
	private Role role;
	private boolean enabled;

}
