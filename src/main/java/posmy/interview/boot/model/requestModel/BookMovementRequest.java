package posmy.interview.boot.model.requestModel;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BookMovementRequest {
	
	@NotBlank
	private String bookCode;
	
	@NotBlank
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate movementDate;

}
