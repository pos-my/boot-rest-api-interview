package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.service.BookService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/librarian")
public class LibrarianController extends BaseController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @PostMapping("/books")
    public BookDto createBook(@RequestBody BookDto bookDto, Principal principal) {
        setCreatedByAndDate(bookDto, principal);
        return bookService.createBook(bookDto);
    }

    @PutMapping("/books/{id}")
    public BookDto updateBook(@RequestBody BookDto bookDto, @PathVariable String id, Principal principal) {
        setUpdatedByAndDate(bookDto, principal);
        return bookService.updateBook(bookDto, id);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
    }
}
