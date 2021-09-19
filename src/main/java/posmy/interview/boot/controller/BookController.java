package posmy.interview.boot.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.service.BookService;

@RestController 
public class BookController{  

	@Autowired  
	BookService bookService; 

	@GetMapping("/lib/book")  
	public ResponseEntity<?> getAllBook()   
	{  
		try {
			List<Book> books = bookService.getAllBooks();
			List<BookDto> bookDtos = books.stream().map(x->new BookDto(x)).collect(Collectors.toList());
			return ResponseEntity.ok(bookDtos);
		} catch (Exception e) {
		      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}  
	
	@PostMapping("/lib/book")
	public ResponseEntity<?> addBook(@RequestBody  BookDto bookDto) 
	{
		try {
			Book book = bookService.addBook(new Book(bookDto));
			URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(book.getBookId())
                    .toUri();
			return ResponseEntity.created(location).build();  
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/lib/book")  
	public ResponseEntity<?> updateBook(@RequestBody BookDto bookDto)   
	{  
		try {
			Book book = bookService.updateBook(new Book(bookDto));
			return ResponseEntity.ok(new BookDto(book));  
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@DeleteMapping("/lib/book/{bookId}")  
	public ResponseEntity<?> deleteBook(@PathVariable("bookId") Long bookId)   
	{  
		try {
			bookService.deleteBook(bookId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}  

	@GetMapping("/mem/book/available")  
	public ResponseEntity<?> getAllAvailableBook()   
	{  
		try {
			List<Book> books = bookService.getAllAvailableBooks();  
			List<BookDto> bookDtos = books.stream().map(x->new BookDto(x)).collect(Collectors.toList());
			return ResponseEntity.ok(bookDtos);
		} catch (Exception e) {
		      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}  
	
	@PutMapping("/mem/book/return/{bookId}")  
	private ResponseEntity<?> returnBook(@PathVariable("bookId") Long bookId)   
	{  
		try {
			Book book = bookService.returnBook(bookId);
			return ResponseEntity.ok(new BookDto(book));  
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
 
	} 

	@PutMapping("/mem/book/borrow/{bookId}")  
	private ResponseEntity<?> borrowBook(Principal principal, @PathVariable("bookId") Long bookId)   
	{  
		try {
			Book book = bookService.borrowBook(bookId, principal.getName());
			
			return ResponseEntity.ok(new BookDto(book));  
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	} 
	
	

}
