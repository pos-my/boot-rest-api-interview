package posmy.interview.boot.book;

import lombok.Data;

@Data
public class Book {
	
	private Long isbn;
	
	private String title;
	
	public Book(Long isbn, String title) {
		this.isbn = isbn;
		this.title = title;
	}

}
