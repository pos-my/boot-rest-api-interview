package posmy.interview.boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookOperation;
import posmy.interview.boot.enums.BookState;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.request.BookRequest;
import posmy.interview.boot.response.BaseServiceResponse;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
    private BookRepository bookRepository;
	
	@Override
    public BaseServiceResponse getAllBooks() {
		List<Book> result = bookRepository.findAll();
		boolean success = (result  != null && !result.isEmpty() );
		String message = "Book list found";
		if(result.isEmpty()) {
			message = "Book list is empty";
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }
	
	@Override
    public BaseServiceResponse getBookById( long id ) {
		Book result = null;
		Optional<Book> findBook = bookRepository.findById( id );
		boolean success = false;
		String message = "Book does not exist";
		if( findBook.isPresent() ) {
			result = findBook.get();
			success = true;
			message = "Book " + result.getTitle() + " found";
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }
	
	
	@Override
    public BaseServiceResponse addBook( BookRequest request ) {
		Book result = bookRepository.findByTitle( request.getTitle() );
		boolean success = false;
		String message = "Book " + request.getTitle() + " is already existed";
		if(result  == null) {
			Book book = new Book();
			
			if( request.getTitle() != null ) 
				book.setTitle( request.getTitle() );
			
			if( request.getAuthor() != null ) 
				book.setAuthor( request.getAuthor() );
			
			if( request.getBorrower() != null ) 
				book.setBorrower( request.getBorrower() );
			
			if( request.getStatus() != null ) 
				book.setStatus( request.getStatus() );			
			
			result = bookRepository.save( book );
			success = true;
			message = "Book added";
		} else {
			result = null;
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }
	
	@Override
    public BaseServiceResponse updateBook( long id, BookRequest request ) {
		Book result = null;
		Optional<Book> findBook = bookRepository.findById( id );
		boolean success = false;
		String message = "Book " + request.getTitle() + " does not exist";
		if( findBook.isPresent() ) {
			if( request.getTitle() != null )
				result = bookRepository.findByTitle( request.getTitle() );
			
			if( result == null ) {
				result = findBook.get();
				
				if( request.getTitle() != null ) {
					result.setTitle( request.getTitle() );
				} 
				
				if( request.getAuthor() != null ) {
					result.setAuthor( request.getAuthor() );
				} 
				
				if( request.getBorrower() != null ) {
					result.setBorrower( request.getBorrower() );
				}
				
				if( request.getStatus() != null ) {
					result.setStatus( request.getStatus() );
				} 
				
				bookRepository.updateBook( result.getId().toString() , result.getTitle(), result.getAuthor(), result.getBorrower(), result.getStatus().name() );
				success = true;
				message = "Book updated";
			} else {
				message = "Book title '" + request.getTitle() + "' is already existed";
				result = null;
			}
		} else {
			result = null;
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }
	
	@Override
    public BaseServiceResponse deleteBook( long id ) {
		Book result = null;
		Optional<Book> findBook = bookRepository.findById( id );
		boolean success = false;
		String message = "Book does not exist";
		if( findBook.isPresent() ) {
			result = findBook.get();
			bookRepository.deleteByBookId( result.getId() );
			success = true;
			message = "Book deleted";
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
    }

	@Override
	public BaseServiceResponse updateBookState(long id, String operation, String username ) {
		Book result = null;
		Optional<Book> findBook = bookRepository.findById( id );
		boolean success = false;
		String message = "Book does not exist";
		if( findBook.isPresent() ) {
			result = findBook.get();
			
			if( operation.equalsIgnoreCase( BookOperation.BORROW.name() ) && result.getStatus().equals( BookState.AVAILABLE ) ) {
				result.setStatus( BookState.BORROWED );
				result.setBorrower( username );
				success = true;
				message = "Book state updated";
			} else if( operation.equalsIgnoreCase( BookOperation.RETURN.name() ) && result.getStatus().equals( BookState.AVAILABLE ) ) {
				result.setStatus( BookState.AVAILABLE );
				result.setBorrower( null );
				success = true;				
			} else {
				message = "Book state no update";
			}
			
			if(success) {
				bookRepository.updateBookByState( result.getId(), result.getStatus().name(), result.getBorrower() );
				message = "Book state updated";
			}
			
			
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
	}

	@Override
	public BaseServiceResponse getBookByBorrower(String username) {
		List<Book> result = bookRepository.findByBorrower( username );
		boolean success = false;
		String message = "No book borrowed";
		if( result != null && result.size() > 0 ) {
			success = true;
			message = result.size() + " book(s) found";
		}
		return BaseServiceResponse.builder().success( success ).message( message ).result( result ).build();
	}

}
