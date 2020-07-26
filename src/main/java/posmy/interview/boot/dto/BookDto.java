package posmy.interview.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import posmy.interview.boot.model.Book.BookStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

	private Long id;
	private String code;
	private String title;
	private BookStatus status;

}
