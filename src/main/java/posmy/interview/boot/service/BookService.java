package posmy.interview.boot.service;

import posmy.interview.boot.model.rest.BookRequest;
import posmy.interview.boot.model.rest.BookResponse;
import posmy.interview.boot.model.rest.GetBookRequest;
import posmy.interview.boot.model.rest.GetBookResponse;

public interface BookService {
	
	public GetBookResponse getAllBooks();
	
	public BookResponse getBooksById(BookRequest request) throws Exception;
	
	public BookResponse addBooks(BookRequest request) throws Exception;
	
	public BookResponse updateBooks(BookRequest request) throws Exception;
	
	public String removeBooks(GetBookRequest request) throws Exception;
	
	public BookResponse borrowBooks(BookRequest request) throws Exception;
	
	public BookResponse returnBooks(BookRequest request) throws Exception;
	
	

}
