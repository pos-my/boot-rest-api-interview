package posmy.interview.boot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.dto.CreateBookRecordDTO;
import posmy.interview.boot.dto.UpdateBookRecordDTO;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

    @GetMapping("available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAllAvailableBooks());
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping
    public ResponseEntity createNewBookRecord(
            @Validated @RequestBody CreateBookRecordDTO createBookRecordDTO) {
        bookService.createNewBookRecord(createBookRecordDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PatchMapping("update-status/{bookId}")
    public ResponseEntity<Book> updateBookStatus(@PathVariable("bookId") Long bookId) {
        return ResponseEntity.ok(bookService.updateBookStatus(bookId));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("{bookId}")
    public ResponseEntity<Book> updateBookRecord(
            @PathVariable("bookId") Long bookId,
            @Validated @RequestBody UpdateBookRecordDTO updateBookRecordDTO
    ) {
        return ResponseEntity.ok(bookService.updateNewBookRecord(bookId, updateBookRecordDTO));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("{bookId}")
    public ResponseEntity deleteBookRecord(@PathVariable("bookId") Long bookId) {
        bookService.deleteBookRecord(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
