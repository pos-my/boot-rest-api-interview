package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Object> addBook(@RequestBody Book book) {
        try {
            Book response = bookService.addBook(book);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Object> updateBook(@RequestBody Book book) {
        try {
            Book response = bookService.updateBook(book);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/delete")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Object> deleteBook(@RequestBody Book book) {
        try {
            bookService.deleteBook(book);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/view/all")
    public ResponseEntity<Object> viewBooks(@RequestParam int page) {
        try {
            Page<Book> response = bookService.getBooks(page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/borrow")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<Object> borrowBook(@RequestBody Book book, Authentication authentication) {
        try {
            Book response = bookService.borrowBook(book, authentication.getName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/return")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<Object> returnBook(@RequestBody Book book, Authentication authentication) {
        try {
            Book response = bookService.returnBook(book, authentication.getName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
