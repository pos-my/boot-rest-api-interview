package posmy.interview.boot.controllers;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.exceptions.CustomRestApiException;
import posmy.interview.boot.exceptions.CustomRestApiException;
import posmy.interview.boot.models.daos.Book;
import posmy.interview.boot.models.dtos.book.BookDto;
import posmy.interview.boot.models.dtos.member.UserRole;
import posmy.interview.boot.services.BookService;
import posmy.interview.boot.services.MemberService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(path = "books", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class BookController {

    private Logger logger = LoggerFactory.getLogger(BookController.class);

    private BookService bookService;
    private MemberService memberService;
    private ModelMapper modelMapper;

    @Autowired
    public BookController(BookService bookService, MemberService memberService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.modelMapper = modelMapper;
    }

    // Get book by ID
    @GetMapping(path = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> getBook(@RequestHeader(value = "Email", required = true) String email,
                                           @PathVariable("bookId") Long bookId) throws Exception {
        logger.info("Getting book by id: {}", bookId);
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        memberService.getMemberUserRole(email);
        Book bookResponse = bookService.getBookById(bookId);
        BookDto response = modelMapper.map(bookResponse, BookDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Get all books
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllBooks(@RequestHeader(value = "Email", required = true) String email) throws Exception {
        logger.info("Getting all book(s).");
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        memberService.getMemberUserRole(email);
        List<BookDto> bookListResponse = new ArrayList<>();
        List<Book> bookList = bookService.getAllBooks();
        for (Book book : bookList) {
            BookDto bookDto = modelMapper.map(book, BookDto.class);
            bookListResponse.add(bookDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(bookListResponse);
    }

    // Add new book(s)
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBooks(@RequestHeader(value = "Email", required = true) String email,
                                      @Valid @RequestBody() List<BookDto> request) throws Exception {
        logger.info("Adding new book(s).");
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        UserRole role = memberService.getMemberUserRole(email);
        if (role.equals(UserRole.MEMBER)) {
            throw new CustomRestApiException("You are not allowed to access this request", HttpStatus.FORBIDDEN);
        }
        List<Book> bookListRequest = new ArrayList<>();
        for (BookDto bookDtoRequest : request) {
            Book bookRequest = modelMapper.map(bookDtoRequest, Book.class);
            bookListRequest.add(bookRequest);
        }
        var response = bookService.addBooks(bookListRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Update book by ID
    @PutMapping(path = "/{bookId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBook(@RequestHeader(value = "Email", required = true) String email,
                                        @PathVariable("bookId") Long bookId, @Valid @RequestBody() BookDto request)
            throws Exception {
        logger.info("Updating book by id: {}.", bookId);
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        UserRole role = memberService.getMemberUserRole(email);
        if (role.equals(UserRole.MEMBER)) {
            throw new CustomRestApiException("You are not allowed to access this request", HttpStatus.FORBIDDEN);
        }
        Book bookRequest = modelMapper.map(request, Book.class);
        var response = bookService.updateBookById(bookId, bookRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Remove book by ID
    @DeleteMapping(path = "/remove/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBooks(@RequestHeader(value = "Email", required = true) String email,
                                         @PathVariable("bookId") Long bookId) throws Exception {
        logger.info("Removing book by id: {}.", bookId);
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        UserRole role = memberService.getMemberUserRole(email);
        if (role.equals(UserRole.MEMBER)) {
            throw new CustomRestApiException("You are not allowed to access this request", HttpStatus.FORBIDDEN);
        }
        var response = bookService.removeBookById(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Borrow book by ID
    @PutMapping(path = "/borrow/{bookId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> borrowBook(@RequestHeader(value = "Email", required = true) String email,
                                        @PathVariable("bookId") Long bookId) throws Exception {
        logger.info("Changing book (bookId: {}) status to BORROWED.", bookId);
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        UserRole role = memberService.getMemberUserRole(email);
        if (role.equals(UserRole.LIBRARIAN)) {
            throw new CustomRestApiException("You are not allowed to access this request", HttpStatus.FORBIDDEN);
        }
        var response = bookService.borrowBookById(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Return book by ID
    @PutMapping(path = "/return/{bookId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> returnBook(@RequestHeader(value = "Email", required = true) String email,
                                        @PathVariable("bookId") Long bookId) throws Exception {
        logger.info("Changing book (bookId: {}) status to AVAILABLE.", bookId);
        if (email.isEmpty() || email.isBlank()) {
            logger.error("Missing header!");
            throw new CustomRestApiException("Missing credentials!", HttpStatus.UNAUTHORIZED);
        }
        UserRole role = memberService.getMemberUserRole(email);
        if (role.equals(UserRole.LIBRARIAN)) {
            throw new CustomRestApiException("You are not allowed to access this request", HttpStatus.FORBIDDEN);
        }
        var response = bookService.returnBookById(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}