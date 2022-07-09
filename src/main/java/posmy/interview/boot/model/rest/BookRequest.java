package posmy.interview.boot.model.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookRequest {

	private long id;
	private String bookName;
	private String status;
}
