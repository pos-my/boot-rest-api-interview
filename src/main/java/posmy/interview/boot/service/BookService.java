package posmy.interview.boot.service;

import posmy.interview.boot.request.BookRequest;
import posmy.interview.boot.response.BaseServiceResponse;

public interface BookService {
	
	BaseServiceResponse getAllBooks();
	
	BaseServiceResponse getBookById( long id );
	
	BaseServiceResponse addBook( BookRequest request );
	
	BaseServiceResponse updateBook( long id, BookRequest request );
	
	BaseServiceResponse updateBookState( long id, String request, String username );
	
	BaseServiceResponse deleteBook( long id );
	
	BaseServiceResponse getBookByBorrower( String username );

}
