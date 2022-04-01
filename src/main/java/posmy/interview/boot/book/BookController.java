package posmy.interview.boot.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import posmy.interview.boot.exceptions.DuplicateRecordException;
import posmy.interview.boot.exceptions.RecordNotFoundException;
import posmy.interview.boot.exceptions.UnauthorizedException;
import posmy.interview.boot.book.requests.NewBookRequest;
import posmy.interview.boot.book.requests.UpdateBookRequest;
import posmy.interview.boot.entities.Book;

import java.util.List;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<List<Book>> viewAll() {
        return ResponseEntity.ok(this.bookService.viewAll());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<?> view(@PathVariable Long id) throws RecordNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.bookService.view(id));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<?> delete(@PathVariable Long id) throws RecordNotFoundException, UnauthorizedException {
        bookService.delete(id);
        return ResponseEntity.ok("");
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping
    public ResponseEntity<?> add(@RequestBody NewBookRequest bookRequest) throws DuplicateRecordException {
        return ResponseEntity.ok(this.bookService.add(bookRequest));
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateBookRequest bookRequest)
            throws Exception {
        return ResponseEntity.ok(this.bookService.update(id, bookRequest));

    }
}
