package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.dto.request.CreateBookDto;
import posmy.interview.boot.dto.request.CreateUserDto;
import posmy.interview.boot.dto.request.UpdateBookDto;
import posmy.interview.boot.dto.request.UpdateUserDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posmy/librarian")
@PreAuthorize("hasRole('ROLE_LIBRARIAN')")
public class LibrarianController {

    private final BookService bookService;
    private final UserService userService;

    @PostMapping("/book")
    public ResponseEntity<Book> createBook(@RequestBody CreateBookDto createBookDto) {
        return new ResponseEntity<>(
            bookService.createBook(createBookDto),
            HttpStatus.CREATED
        );
    }

    @PatchMapping("/book/{id}")
    public ResponseEntity<Book> updateBook(
        @PathVariable(value = "id") Long id,
        @RequestBody UpdateBookDto updateBookDto
    ) {
        return new ResponseEntity<>(
            bookService.updateBook(id, updateBookDto),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable(value = "id") Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
        return new ResponseEntity<>(
            userService.createUser(createUserDto),
            HttpStatus.CREATED
        );
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<User> updateUser(
        @PathVariable(value = "id") Long id,
        @RequestBody UpdateUserDto updateUserDto
    ) {
        return new ResponseEntity<>(
            userService.updateUser(id, updateUserDto),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUserBy(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
