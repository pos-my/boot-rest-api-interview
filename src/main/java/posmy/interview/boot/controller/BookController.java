package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.service.BookService;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/viewBooks")
    public ResponseEntity<List<Book>> viewBooks() {
        return ResponseEntity.ok(this.bookService.viewBooks());
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/addBooks")
    public ResponseEntity<List<Book>> addBooks(@RequestBody List<Book> bookList) {
        return ResponseEntity.ok(this.bookService.addBooks(bookList));
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("/updateBooks")
    public ResponseEntity<List<Book>> updateBooks(@RequestBody List<Book> bookList) {
        return ResponseEntity.ok().body(this.bookService.updateBooks(bookList));
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @DeleteMapping("/removeBook/{bookId}")
    public ResponseEntity<Void> removeBook(@PathVariable Integer bookId) {
        this.bookService.removeBook(bookId);
        return ResponseEntity.ok().body(null);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("/borrowBook/{bookId}")
    public ResponseEntity<Book> borrowBook(@PathVariable Integer bookId) {
        return ResponseEntity.ok().body(this.bookService.borrowBook(bookId));
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("/returnBook/{bookId}")
    public ResponseEntity<Book> returnBook(@PathVariable Integer bookId) {
        return ResponseEntity.ok().body(this.bookService.returnBook(bookId));
    }
}
