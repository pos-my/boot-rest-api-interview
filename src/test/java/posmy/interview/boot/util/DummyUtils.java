package posmy.interview.boot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import posmy.interview.boot.book.Book;

public class DummyUtils {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final Long isbn = 9783161484100L;
	private static final String title = "Book of example";
	
	public static String getBookJson() throws JsonProcessingException {
		return mapper.writeValueAsString(new Book(isbn, title));
	}
	
	public static Book getBook() throws JsonProcessingException {
		return new Book(isbn, title);
	}

}
