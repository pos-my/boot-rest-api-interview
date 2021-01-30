package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.BookRecordRequest;
import posmy.interview.boot.service.bookrecord.BookRecordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-record")
@Slf4j
public class BookRecordController {

    private final BookRecordService bookRecordService;

    @PostMapping(value = "/borrow")
    public ResponseEntity<Long> borrowBook(@RequestBody BookRecordRequest bookRecordRequest) throws NoDataFoundException {
        return ResponseEntity.ok(bookRecordService.borrowBook(bookRecordRequest));
    }

    @PostMapping(value = "/return")
    public ResponseEntity<Void> returnBook(@RequestBody BookRecordRequest bookRecordRequest) throws NoDataFoundException {
        bookRecordService.returnBook(bookRecordRequest);
        return ResponseEntity.ok().build();
    }
}
