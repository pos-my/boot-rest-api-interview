package posmy.interview.boot.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.request.BookBorrowRequest;
import posmy.interview.boot.model.request.BookGetRequest;
import posmy.interview.boot.model.request.BookReturnRequest;
import posmy.interview.boot.model.response.BookGetResponse;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.service.BookBorrowService;
import posmy.interview.boot.service.BookGetService;
import posmy.interview.boot.service.BookReturnService;

import java.security.Principal;

@RestController
@RequestMapping(value = "/v1/member", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final BookGetService bookGetService;
    private final BookBorrowService bookBorrowService;
    private final BookReturnService bookReturnService;

    public MemberController(BookGetService bookGetService,
                            BookBorrowService bookBorrowService,
                            BookReturnService bookReturnService) {
        this.bookGetService = bookGetService;
        this.bookBorrowService = bookBorrowService;
        this.bookReturnService = bookReturnService;
    }

    @GetMapping
    public String healthCheck() {
        return "success";
    }

    @GetMapping("/book")
    public BookGetResponse bookGet(Pageable pageable) {
        BookGetRequest request = BookGetRequest.builder()
                .pageable(pageable)
                .build();
        return bookGetService.execute(request);
    }

    @PatchMapping("/book/borrow/{id}")
    public EmptyResponse bookBorrow(@PathVariable("id") String id,
                                    Principal principal) {
        BookBorrowRequest request = BookBorrowRequest.builder()
                .bookId(id)
                .username(principal.getName())
                .build();
        return bookBorrowService.execute(request);
    }

    @PatchMapping("/book/return/{id}")
    public EmptyResponse bookReturn(@PathVariable("id") String id,
                                    Principal principal) {
        BookReturnRequest request = BookReturnRequest.builder()
                .bookId(id)
                .username(principal.getName())
                .build();
        return bookReturnService.execute(request);
    }
}
