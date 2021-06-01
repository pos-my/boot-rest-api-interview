package posmy.interview.boot.request;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserState;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
	
	private String username;
	
	private String password;
	
	private String firstname;
	
	private String lastname;
	
	private UserState status;
	
	private List<UserRole> roles;
}
