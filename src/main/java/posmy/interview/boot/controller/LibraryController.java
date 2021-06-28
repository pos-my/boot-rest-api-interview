package posmy.interview.boot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.request.BookRequest;
import posmy.interview.boot.request.RoleRequest;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.service.LibraryService;

import java.util.List;

@RestController
public class LibraryController {

    private static final Logger log = LoggerFactory.getLogger(LibraryController.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LibraryService libraryService;

    @RequestMapping("/book")
    public List<Book> viewAllBooks() {
        log.info("View all books...");
        List<Book> books = bookRepository.findAll();

        return books;
    }

    @PostMapping("/book")
    public BookRequest handleBook(@RequestBody BookRequest request) {
        log.info("Handle book request...");
        BookRequest bookRequest = libraryService.processBook(request.getAction(), request.getName(), request.getRole());

        return bookRequest;
    }

    @PostMapping("/role")
    public RoleRequest handleRole(@RequestBody RoleRequest request) {
        log.info("Handle role request...");
        RoleRequest roleRequest = libraryService.processRole(request.getAction(), request.getName(), request.getRole());

        return roleRequest;
    }

}
