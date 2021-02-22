package posmy.interview.boot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.dao.BookDao;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.model.requestModel.BookRegisterRequest;
import posmy.interview.boot.model.requestModel.BookUpdateRequest;
import posmy.interview.boot.model.responseModel.MessageResponse;
import posmy.interview.boot.service.BookService;


@RestController
@RequestMapping("/lib")
public class LibrarianController {

	@Autowired
	private BookService bookService;
	
	@Autowired
	private BookDao dao;
	
	@GetMapping("/books")
	public List<Book> getAllBooks() {
		return bookService.findAll();
	}
	
	@PostMapping("/addBook")
	public ResponseEntity<?> addBook(@Valid @RequestBody BookRegisterRequest bookReg) {		

		if (dao.existsByBookCode(bookReg.getBookCode()) || dao.existsByBookName(bookReg.getBookName())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Book already exist!"));
		}
		
		
		bookService.saveBook(bookReg);

		return ResponseEntity.ok(new MessageResponse("Book added successfully!"));
	}
	
	@PutMapping("/updateBook/{id}")
	public ResponseEntity<?> updateBook(@PathVariable(value="id") Long id, @Valid @RequestBody BookUpdateRequest request) throws Exception{
		
		Book book = bookService.findById(id).orElseThrow(() -> new RuntimeException("Error: Book is not found."));
		
		if(request.getBookCode() != null && request.getBookCode().length()>0) {
			if (dao.existsByBookCode(request.getBookCode())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Duplicate Book Code. "));
			}
			book.setBookCode(request.getBookCode());
		}
		
		if(request.getBookName() != null && request.getBookName().length()>0) {
			if ( dao.existsByBookName(request.getBookName())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Duplicate Book Name"));
			}
			book.setBookName(request.getBookName());
		}
		
		if(request.getStatus() != null && request.getStatus().length()>0) {
			book.setStatus(request.getStatus());
		}
		
		if(request.getDateBorrowed()!= null) {
			book.setDateBorrowed(request.getDateBorrowed());
		}
		
		if(request.getDateReturned()!= null) {
			book.setDateReturned(request.getDateReturned());
		}
		
	
	    final Book updatedBook = dao.save(book);
	    return ResponseEntity.ok(updatedBook);
		
	}
	
	@DeleteMapping("/removeBook/{id}")
	public Map<String, Boolean> deleteBook(@PathVariable(value="id") Long id) throws Exception{
		
		Map<String, Boolean> mapResponse = new HashMap<>();
		Book book = dao.findById(id).orElseThrow(() -> new RuntimeException("Error: Book is not found."));
		
		if(book.getUsers() != null) {
			 mapResponse.put("Unable : Book has been lent. ", Boolean.TRUE);
			 return mapResponse;
		}
		
	    dao.delete(book);	    
	    mapResponse.put("Deleted", Boolean.TRUE);
	    return mapResponse;
		
	}
	
	
	@GetMapping("/admin")
	public String adminAccess() {
		return "Admin Board.";
	}
}
