package posmy.interview.boot.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import posmy.interview.boot.model.book.BookResponse;

@RequestMapping("/transactions")
public interface TransactionOperations {

    @PreAuthorize("hasRole('ROLE_LIBRARIAN') || " + "hasRole('ROLE_MEMBER')")
    @GetMapping("/")
    BookResponse getBooks(@RequestParam("pageSize") Integer pageSize,
                          @RequestParam("pageNumber") Integer pageNumber,
                          @RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "description", required = false) String description,
                          @RequestParam(value = "status", required = false) String status);

}
