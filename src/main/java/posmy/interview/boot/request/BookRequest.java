package posmy.interview.boot.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import posmy.interview.boot.enums.BookState;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

	private String title;
	
	private String author;
	
	private String borrower;
	
	private BookState status;
}
