package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.service.api.BookService;

import java.util.List;

@RestController()
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping()
    public List<Book> saveAll(@RequestBody List<Book> books) {
        return bookService.saveAll(books);
    }

    @GetMapping("/name/{name}")
    public List<Book> findByName(@PathVariable String name) {
        return bookService.findByName(name);
    }

    @PostMapping("/borrow")
    public List<Book> borrow(@RequestBody List<Long> ids) {
        return bookService.borrow(ids);
    }

    @PostMapping("/return")
    public List<Book> returnBook(@RequestBody List<Long> ids) {
        return bookService.returnBook(ids);
    }

}
