package posmy.interview.boot.model.requestModel;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BookUpdateRequest {
	
	
	private String bookCode;
	private String bookName;
	private String status;
	private LocalDate dateBorrowed;
	private LocalDate dateReturned;
	

}
