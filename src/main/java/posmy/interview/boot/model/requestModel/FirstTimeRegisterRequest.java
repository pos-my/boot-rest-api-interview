package posmy.interview.boot.model.requestModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class FirstTimeRegisterRequest {
	
	

	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Size(min = 3, max = 40)
	private String password;
	
}
