package posmy.interview.boot.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import posmy.interview.boot.model.rest.BookRequest;
import posmy.interview.boot.model.rest.BookResponse;
import posmy.interview.boot.model.rest.GetBookRequest;
import posmy.interview.boot.model.rest.GetBookResponse;
import posmy.interview.boot.service.BookService;

@Slf4j
@RestController
@RequestMapping({"/book"})
public class BookController {
	
	private BookService bookServiceImpl;
	
	public BookController(BookService bookServiceImpl) {
		this.bookServiceImpl = bookServiceImpl;
	}
	
	@RequestMapping(value="/viewBooks", method = RequestMethod.GET)
	public ResponseEntity<Object> viewBooks(){
		
		log.info("View Book Details");
		
		GetBookResponse response = bookServiceImpl.getAllBooks();
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/viewBooksById", method = RequestMethod.GET)
	public ResponseEntity<Object> viewBooksById(@Valid @RequestBody BookRequest request) throws Exception{
		
		log.info("View Book By Id");
		
		BookResponse response = bookServiceImpl.getBooksById(request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/borrowBooks", method = RequestMethod.GET)
	public ResponseEntity<Object> borrowBooks(@Valid @RequestBody BookRequest request) throws Exception{
		
		log.info("Borrow Book ");
		
		BookResponse response = bookServiceImpl.borrowBooks(request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/returnBooks", method = RequestMethod.GET)
	public ResponseEntity<Object> returnBooks(@Valid @RequestBody BookRequest request) throws Exception{
		
		log.info("Return Book ");
		
		BookResponse response =  bookServiceImpl.returnBooks(request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/addBooks", method = RequestMethod.POST)
	public ResponseEntity<Object> addBooks(@Valid @RequestBody BookRequest request) throws Exception{
		
		log.info("Adding Books");
		
		BookResponse response = bookServiceImpl.addBooks(request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/updateBooks", method = RequestMethod.POST)
	public ResponseEntity<Object> updateBooks(@Valid @RequestBody BookRequest request) throws Exception{
		
		log.info("Update Books");
		
		BookResponse response = bookServiceImpl.updateBooks(request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/deleteBooks", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteBooks(@Valid @RequestBody GetBookRequest request) throws Exception{
		
		log.info("Delete Books");
		
		String response = bookServiceImpl.removeBooks(request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	

	

		
}
