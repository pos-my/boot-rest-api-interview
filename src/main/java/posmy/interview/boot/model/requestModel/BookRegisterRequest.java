package posmy.interview.boot.model.requestModel;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class BookRegisterRequest {
	
	@NotBlank
	private String bookCode;

	@NotBlank
	private String bookName;
	

}
