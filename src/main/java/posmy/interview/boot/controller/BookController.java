package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.domain.Book;
import posmy.interview.boot.manager.BookManager;

import java.security.Principal;
import java.util.UUID;

@RestController
public class BookController {

    @Autowired
    BookManager bookManager;

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/librarian/book")
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        try {
            Book currBook = bookManager.addBook(book);
            return new ResponseEntity<>(currBook, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/librarian/book")
    public ResponseEntity<?> updateBook(@RequestBody Book book) {
        try {
            Book currBook = bookManager.updateBook(book);
            return new ResponseEntity<>(currBook, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/librarian/book")
    public ResponseEntity<?> deleteBook(@RequestParam UUID id) {
        try {
            bookManager.deleteBook(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/member/book")
    public ResponseEntity<?> getAvailableBooksList(@RequestParam int page, @RequestParam int size) {
        try {
            Page<Book> currBookPage = bookManager.getAvailableBooksList(page, size);
            return new ResponseEntity<>(currBookPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PutMapping("/member/book/borrow")
    public ResponseEntity<?> borrowBook(@RequestParam UUID bookId, Principal principal) {
        try {
            Book currBook = bookManager.borrowBook(bookId, principal.getName());
            return new ResponseEntity<>(currBook, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PutMapping("/member/book/return")
    public ResponseEntity<?> returnBook(@RequestParam UUID id) {
        try {
            Book currBook = bookManager.returnBook(id);
            return new ResponseEntity<>(currBook, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
