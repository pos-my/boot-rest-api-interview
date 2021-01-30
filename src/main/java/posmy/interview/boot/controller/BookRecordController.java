package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.service.bookrecord.BookRecordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-record")
@Slf4j
public class BookRecordController {

    private final BookRecordService bookRecordService;

    @PostMapping(value = "/{bookId}/borrow")
    public ResponseEntity<Long> borrowBook(@PathVariable long bookId) throws NoDataFoundException {
        return ResponseEntity.ok(bookRecordService.borrowBook(bookId));
    }

    @PostMapping(value = "/{bookId}/return")
    public ResponseEntity<Void> returnBook(@PathVariable long bookId) throws NoDataFoundException {
        bookRecordService.returnBook(bookId);
        return ResponseEntity.ok().build();
    }
}
