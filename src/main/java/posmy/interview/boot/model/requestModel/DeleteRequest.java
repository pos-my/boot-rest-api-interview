package posmy.interview.boot.model.requestModel;

import javax.validation.constraints.NotBlank;


public class DeleteRequest {

	@NotBlank
	private String username;
}
