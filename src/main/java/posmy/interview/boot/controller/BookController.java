package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.controller.api.BookApi;
import posmy.interview.boot.controller.request.AddBooksRequest;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.service.BookService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController implements BookApi {

    private final BookService bookService;

    @Override
    @PreAuthorize("hasAnyAuthority('MEMBER', 'LIBRARIAN')")
    public ResponseEntity<List<Book>> fetchAllBooks() {
        return ResponseEntity.ok(this.bookService.findAllBooks());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('LIBRARIAN')")
    public ResponseEntity<List<Book>> addBooks(@Valid @RequestBody AddBooksRequest addBooksRequest) throws Exception {
        return ResponseEntity.ok(this.bookService.addBooks(addBooksRequest.getTitleList()));
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<List<Book>> updateBooks(List<Book> bookList) {
        return ResponseEntity.ok().body(this.bookService.updateBooks(bookList));
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Void> deleteBook(Long bookId) {
        this.bookService.deleteBookById(bookId);
        return ResponseEntity.ok().body(null);
    }

    @Override
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<Book> borrowBook(Long bookId) throws Exception {
        return ResponseEntity.ok().body(this.bookService.borrowBook(bookId));
    }

    @Override
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<Book> returnBook(Long bookId) throws Exception {
        return ResponseEntity.ok().body(this.bookService.returnBook(bookId));
    }
}
