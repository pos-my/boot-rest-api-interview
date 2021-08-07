package posmy.interview.boot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.extern.slf4j.Slf4j;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.object.BookObject;
import posmy.interview.boot.service.IBookService;

@Slf4j
@RestController
@RequestMapping("book")
public class BookController {
	@Autowired
	IBookService bookService;
	
	@PostMapping("create")
	public ResponseEntity<Book> create(@AuthenticationPrincipal final User user, @RequestBody BookObject bookObject){
		ResponseEntity<Book> rse = new ResponseEntity<Book>(HttpStatus.OK);
		try {
			Optional<Book> book = bookService.create(bookObject);
			if(!book.isPresent()) {
				rse = new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				rse = new ResponseEntity<Book>(book.get(), HttpStatus.OK);
			}
		}catch(Exception e) {
			log.error("CreateBook() -> Error: {}", e.toString());
			rse = new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return rse;
	}
	
	@PutMapping("update/{bookId}")
	public ResponseEntity<Book> update(@AuthenticationPrincipal final User user, @PathVariable("bookId") Long bookId, @RequestBody BookObject bookObject){
		ResponseEntity<Book> rse = new ResponseEntity<Book>(HttpStatus.OK);
		try {
			Optional<Book> book = bookService.update(bookObject, bookId);
			if(!book.isPresent()) {
				rse = new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				rse = new ResponseEntity<Book>(book.get(), HttpStatus.OK);
			}
		}catch(Exception e) {
			log.error("Update Book -> Error: {}", e.toString());
			rse = new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return rse;
	}
	
	@GetMapping("all")
	public ResponseEntity<List<Book>> getBookListAll(@AuthenticationPrincipal final User user) {
		ResponseEntity<List<Book>> rse = new ResponseEntity<List<Book>>(HttpStatus.OK);
		try {
			rse = new ResponseEntity<List<Book>>(bookService.findAll(), HttpStatus.OK);			
		}catch(Exception e){
			log.error("List All Book -> Error: {}", e.toString());
			rse = new ResponseEntity<List<Book>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return rse;
	}
	
	@GetMapping("get/{bookId}")
	public ResponseEntity<Book> getBookByBookId(@AuthenticationPrincipal final User user, @PathVariable("bookId") Long bookId) {
		ResponseEntity<Book> rse = new ResponseEntity<Book>(HttpStatus.OK);
		try {
			Optional<Book> book = bookService.findByBookId(bookId);
			if(!book.isPresent()) {
				rse = new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				rse = new ResponseEntity<Book>(book.get(), HttpStatus.OK);
			}
		}catch(Exception e){
			log.error("Book by Id -> Error: {}", e.toString());
			rse = new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return rse;
	}
	
	@DeleteMapping("delete/{bookId}")
	public ResponseEntity<Boolean> delete(@AuthenticationPrincipal final User user, @PathVariable("bookId") Long bookId) {
		ResponseEntity<Boolean> rse = new ResponseEntity<Boolean>(HttpStatus.OK);
		try {
			Optional<Boolean> book = bookService.delete(bookId);
			if(!book.isPresent()) {
				rse = new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				rse = new ResponseEntity<Boolean>(book.get(), HttpStatus.OK);
			}
		}catch(Exception e){
			log.error("Delete Book by Id -> Error: {}", e.toString());
			rse = new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return rse;
		
	}
	
	@PostMapping("borrow/{bookId}")
	public ResponseEntity<Book> borrow(@AuthenticationPrincipal final User user, @PathVariable("bookId") Long bookId){
		ResponseEntity<Book> rse = new ResponseEntity<Book>(HttpStatus.OK);
		try {
			Optional<Book> book = bookService.borrow(bookId);
		}catch(Exception e){
			log.error("Borrow Book() -> Error: {}", e.toString());
			rse = new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return rse;
		
	}
	
	@PostMapping("return/{bookId}")
	public ResponseEntity<Book> returnBook(@AuthenticationPrincipal final User user, @PathVariable("bookId") Long bookId){
		ResponseEntity<Book> rse = new ResponseEntity<Book>(HttpStatus.OK);
		try {
			Optional<Book> book = bookService.returnBook(bookId);
		}catch(Exception e){
			log.error("Return Book() -> Error: {}", e.toString());
			rse = new ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return rse;		
	}
	
}
