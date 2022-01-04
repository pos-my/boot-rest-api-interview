package posmy.interview.boot.controller;

import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.book.*;

@RequestMapping("/books")
public interface BookOperations {
    @GetMapping("/")
    BookResponse getBooks(@RequestParam("pageSize") Integer pageSize,
                          @RequestParam("pageNumber") Integer pageNumber,
                          @RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "description", required = false) String description,
                          @RequestParam(value = "status", required = false) String status);

    @PostMapping("/create")
    BookCreatedResponse addBooks(@RequestBody CreateBookRequest createBookRequest);

    @PutMapping("/update")
    UpdateBookResponse updateBooks(@RequestBody UpdateBookRequest updateBookRequest);

}
