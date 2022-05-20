package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.request.GenericReq;
import posmy.interview.boot.service.LibrarianBookService;

import java.util.List;
import java.util.Optional;

/**
 * Pour librarian book service
 */
@Controller
@RequestMapping(value = "/librarian/book")
public class LibrarianBookController {
    @Autowired
    LibrarianBookService librarianBookService;

    @GetMapping(value = "/list", produces = "application/json")
    public List<Book> getBooks() {
        return librarianBookService.viewAll();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Optional<Book> getBook(@PathVariable Long id) {
        return librarianBookService.get(id);
    }

    @PostMapping(value = "/add", produces = "application/json")
    public Book addBook(@RequestBody final GenericReq<Book> req) {
        return librarianBookService.add(req.getBody());
    }

    @PutMapping(value = "/update", produces = "application/json")
    public Book updateBook(@RequestBody final GenericReq<Book> req) {
        return librarianBookService.update(req.getBody());
    }

    @PutMapping(value = "/delete", produces = "application/json")
    public Optional<Book> deleteBook(@RequestBody final GenericReq<Long> req) {
        return librarianBookService.delete(req.getBody());
    }

}
