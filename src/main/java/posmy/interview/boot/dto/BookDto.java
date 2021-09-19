package posmy.interview.boot.dto;


import lombok.*;
import posmy.interview.boot.model.Book;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private Long bookId;

    private String isbn;
    
    private String title;
    
    private String available;
	
	public BookDto(Book book) {
		this.bookId=book.getBookId();
		this.isbn = book.getIsbn();
		this.title= book.getTitle();
		this.available = book.getAvailable();
	}
}