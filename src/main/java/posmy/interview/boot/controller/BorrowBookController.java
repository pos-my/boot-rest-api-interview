package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import posmy.interview.boot.model.BorrowActivity;
import posmy.interview.boot.model.BorrowRequest;
import posmy.interview.boot.service.BookService;

@RestController
@RequestMapping("/borrow")
public class BorrowBookController {

    private final BookService bookService;

    @Autowired
    public BorrowBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/book")
    public BorrowActivity borrowBook(@RequestBody BorrowRequest borrowRequest){
        try {
            return bookService.borrowBook(borrowRequest);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/book")
    public void returnBook(@RequestBody BorrowRequest borrowRequest){
        bookService.returnBook(borrowRequest);
    }
}
