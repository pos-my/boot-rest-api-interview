package posmy.interview.boot.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookDetail {
	
	private Long bookId;
		
	private String bookName;
	
	private String status;
}
