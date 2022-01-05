package posmy.interview.boot.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.book.*;

@RequestMapping("/books")
public interface BookOperations {
    @PreAuthorize ("hasRole('ROLE_LIBRARIAN') || " + "hasRole('ROLE_MEMBER')")
    @GetMapping("/")
    BookResponse getBooks(@RequestParam("pageSize") Integer pageSize,
                          @RequestParam("pageNumber") Integer pageNumber,
                          @RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "description", required = false) String description,
                          @RequestParam(value = "status", required = false) String status);

    @PreAuthorize ("hasRole('ROLE_LIBRARIAN') || " + "hasRole('ROLE_MEMBER')")
    @PostMapping("/create")
    BookCreatedResponse addBooks(@RequestBody CreateBookRequest createBookRequest);

    @PreAuthorize ("hasRole('ROLE_LIBRARIAN') || " + "hasRole('ROLE_MEMBER')")
    @PutMapping("/update")
    UpdateBookResponse updateBooks(@RequestBody UpdateBookRequest updateBookRequest);

}
