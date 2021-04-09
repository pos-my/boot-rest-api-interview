package posmy.interview.boot.controller;

import static posmy.interview.boot.utils.CommonConstants.AUTHORIZATION_HEADER;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.authentication.JwtTokenUtil;
import posmy.interview.boot.dto.request.SearchBookDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posmy/member")
@PreAuthorize("hasRole('ROLE_MEMBER')")
public class MemberController {

    private final BookService bookService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/books")
    public ResponseEntity<Page<Book>> getBooks(
        @RequestParam(value = "title", required = false) String title,
        Pageable pageable
    ) {
        return new ResponseEntity<>(
            bookService.getBooks(
                SearchBookDto.builder()
                    .title(title)
                    .build(),
                pageable
            ),
            HttpStatus.OK
        );
    }

    @PatchMapping("/book/borrow/{id}")
    public ResponseEntity<Book> borrowBook(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(
            bookService.borrowBook(id),
            HttpStatus.OK
        );
    }

    @PatchMapping("/book/return/{id}")
    public ResponseEntity<Book> returnBook(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(
            bookService.returnBook(id),
            HttpStatus.OK
        );
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteOwnAccount(
        @RequestHeader(name = AUTHORIZATION_HEADER) String token
    ) {
        // Getting the username from the jwt token
        userService.deleteUserBy(jwtTokenUtil.getUsernameFromToken(token.substring(7)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
