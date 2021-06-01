package posmy.interview.boot.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.enums.BookOperation;
import posmy.interview.boot.request.BookRequest;
import posmy.interview.boot.response.BaseServiceResponse;
import posmy.interview.boot.service.BookService;

@RestController
@RequestMapping("/api/v1")
public class BookController {
	
//	private final String tagName = "Book Management";
	
	@Autowired
	private BookService bookService;
	
	@GetMapping( path = "/books", produces = MediaType.APPLICATION_JSON_VALUE )
	@PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<Object> getAllbook() {
		BaseServiceResponse result = bookService.getAllBooks();		
		if( !result.isSuccess() ) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body( result );
    }
	
	@GetMapping( path = "/books/{id:.+}", produces = MediaType.APPLICATION_JSON_VALUE )
	@PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<Object> getBookById( @PathVariable(name="id", required = true) long id ) {
		BaseServiceResponse result = bookService.getBookById( id );	
		if( !result.isSuccess() ) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body( result );
    }
	
	
	@PostMapping( path = "/books", produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE )
	@PreAuthorize("hasAnyAuthority('LIBRARIAN')")
    public ResponseEntity<Object> addBook( @RequestBody BookRequest request ) throws URISyntaxException {
		BaseServiceResponse result = bookService.addBook( request );
		if( !result.isSuccess() ) {
			return ResponseEntity.badRequest().body( result );
		}
		Book book = (Book) result.getResult();
		return ResponseEntity.created(new URI( "/books/" + book.getId() ) ).body( result );
    }	
	
	@PatchMapping( path = "/books/{id:.+}", produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.APPLICATION_JSON_VALUE )
	@PreAuthorize("hasAnyAuthority('LIBRARIAN')")
    public ResponseEntity<Object> updateBook( @PathVariable(name="id", required = true) long id, @RequestBody BookRequest request ) {
		BaseServiceResponse result = bookService.updateBook( id, request );
		if( !result.isSuccess() ) {
			return ResponseEntity.badRequest().body( result );
		}
		return ResponseEntity.ok().body( result );
    }
	
	@PatchMapping( path = "/books/{id:.+}/operation/{operation:.+}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseEntity<BaseServiceResponse> updateBookState( @PathVariable("id") @Valid long id, @PathVariable("operation") @Valid String operation ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        BaseServiceResponse result = bookService.updateBookState( id, operation, username );
        if( !result.isSuccess() ){
        	return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( result );
        }
        return ResponseEntity.ok().body( result );
    }
	
	@DeleteMapping( path = "/books/{id:.+}", produces = MediaType.APPLICATION_JSON_VALUE  )
	@PreAuthorize("hasAnyAuthority('LIBRARIAN')")
    public ResponseEntity<Object> deleteBook( @PathVariable(name="id", required = true) long id ) {
		BaseServiceResponse result = bookService.deleteBook( id );
		if( !result.isSuccess() ) {
			return ResponseEntity.badRequest().body( result );
		}
		return ResponseEntity.accepted().body( result );
    }
	
}
