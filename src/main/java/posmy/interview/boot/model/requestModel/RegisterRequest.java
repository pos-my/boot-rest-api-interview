package posmy.interview.boot.model.requestModel;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterRequest {

	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	private Set<String> role;

	@NotBlank
	@Size(min = 3, max = 40)
	private String password;
	
	public RegisterRequest() {
	}

	public RegisterRequest(String username, String password, Set role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}
}


