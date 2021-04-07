package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/librarian")
public class LibrarianController extends BaseController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @GetMapping("/books")
    public List<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @PostMapping("/books")
    public BookDto createBook(@Valid @RequestBody BookDto bookDto, Principal principal) {
        setCreatedByAndDate(bookDto, principal);
        return bookService.createBook(bookDto);
    }

    @PutMapping("/books/{id}")
    public BookDto updateBook(@Valid @RequestBody BookDto bookDto, @PathVariable String id, Principal principal) {
        setUpdatedByAndDate(bookDto, principal);
        return bookService.updateBook(bookDto, id);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/members")
    public List<UserDto> getAllMembers() {
        return userService.findAllMembers();
    }

    @PostMapping("/members")
    public UserDto createMember(@RequestBody UserDto userDto, Principal principal) {
        setCreatedByAndDate(userDto, principal);
        return userService.createMember(userDto);
    }

    @PutMapping("/members/{loginId}")
    public UserDto updateMember(@RequestBody UserDto userDto, @PathVariable String loginId, Principal principal) {
        setUpdatedByAndDate(userDto, principal);
        return userService.updateMember(userDto, loginId);
    }

    @DeleteMapping("/members/{loginId}")
    public void deleteMember(@PathVariable String loginId) {
        userService.deleteMember(loginId);
    }
}
