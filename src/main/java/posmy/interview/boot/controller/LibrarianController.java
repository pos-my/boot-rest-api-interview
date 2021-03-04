package posmy.interview.boot.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.model.request.BookAddRequest;
import posmy.interview.boot.model.request.BookDeleteRequest;
import posmy.interview.boot.model.request.BookPutRequest;
import posmy.interview.boot.service.BookAddService;
import posmy.interview.boot.service.BookDeleteService;
import posmy.interview.boot.service.BookPutService;

@RestController
@RequestMapping(value = "/v1/librarian", produces = MediaType.APPLICATION_JSON_VALUE)
public class LibrarianController {

    private final BookAddService bookAddService;
    private final BookPutService bookPutService;
    private final BookDeleteService bookDeleteService;

    public LibrarianController(BookAddService bookAddService,
                               BookPutService bookPutService,
                               BookDeleteService bookDeleteService) {
        this.bookAddService = bookAddService;
        this.bookPutService = bookPutService;
        this.bookDeleteService = bookDeleteService;
    }

    @GetMapping
    public String healthCheck() {
        return "success";
    }

    @PostMapping("/book")
    public Book bookAdd(@RequestBody @Validated BookAddRequest request) {
        return bookAddService.execute(request);
    }

    @PutMapping("/book")
    public Book bookPut(@RequestBody @Validated BookPutRequest request) {
        return bookPutService.execute(request);
    }

    @DeleteMapping("book/{id}")
    public void bookDelete(@PathVariable("id") String id) {
        BookDeleteRequest request = BookDeleteRequest.builder()
                .id(id)
                .build();
        bookDeleteService.execute(request);
    }
}
