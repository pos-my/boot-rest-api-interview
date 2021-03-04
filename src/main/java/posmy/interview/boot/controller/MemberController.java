package posmy.interview.boot.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.request.BookBorrowRequest;
import posmy.interview.boot.model.request.BookGetRequest;
import posmy.interview.boot.model.response.BookGetResponse;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.service.BookBorrowService;
import posmy.interview.boot.service.BookGetService;

import java.security.Principal;

@RestController
@RequestMapping(value = "/v1/member", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final BookGetService bookGetService;
    private final BookBorrowService bookBorrowService;

    public MemberController(BookGetService bookGetService,
                            BookBorrowService bookBorrowService) {
        this.bookGetService = bookGetService;
        this.bookBorrowService = bookBorrowService;
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
}
