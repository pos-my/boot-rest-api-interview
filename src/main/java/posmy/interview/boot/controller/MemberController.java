package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.exception.InvalidBookActionException;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;
import posmy.interview.boot.system.Constant;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping("/books")
    public List<BookDto> getAvailableBooks() {
        return bookService.findAvailableBooks();
    }

    @PostMapping("/books/{id}")
    public BookDto handleBook(@PathVariable String id, @RequestParam String action, Principal principal) {
        BookDto bookDto;
        if (Constant.BookAction.isBorrow(action)) {
            bookDto = bookService.borrowBook(principal.getName(), id);
        } else if (Constant.BookAction.isReturn(action)) {
            bookDto = bookService.returnBook(id);
        } else {
            throw new InvalidBookActionException(action);
        }
        return bookDto;
    }

}
