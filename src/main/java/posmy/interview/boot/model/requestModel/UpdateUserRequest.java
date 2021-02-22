package posmy.interview.boot.model.requestModel;

import lombok.Data;

@Data
public class UpdateUserRequest {
	
	private String username;
	private String password;

}
