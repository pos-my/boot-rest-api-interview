package posmy.interview.boot.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.controllers.dtos.BookReq;
import posmy.interview.boot.domains.BookStatus;
import posmy.interview.boot.domains.Books;
import posmy.interview.boot.domains.Users;
import posmy.interview.boot.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/book")
@SecurityRequirement(name = "assessment-api")
public class BookController {
    private BookRepository bookRepo;

    @Autowired
    public BookController(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/borrow/{bookName}")
    @PreAuthorize("hasAuthority('MEMBER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book borrowed successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Books.class)) }),
            @ApiResponse(responseCode = "403", description = "Only MEMBER could borrow book", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content) })
    public Books borrowBook(@PathVariable("bookName") String bookName) {
        Books book = bookRepo.findByBookName(bookName).orElseThrow(() -> new RuntimeException("Book not found"));
        if (null != book)
            bookRepo.updateStatusByBookName(BookStatus.BORROWED, bookName);
        return book;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/return/{bookName}")
    @PreAuthorize("hasAuthority('MEMBER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Books.class)) }),
            @ApiResponse(responseCode = "403", description = "Only MEMBER could return book", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content) })
    public Books returnBook(@PathVariable("bookName") String bookName) {
        Books book = bookRepo.findByBookName(bookName).orElseThrow(() -> new RuntimeException("Book not found"));
        if (null != book)
            bookRepo.updateStatusByBookName(BookStatus.AVAILABLE, bookName);
        return book;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/remove/{bookName}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book removed successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Books.class)) }),
            @ApiResponse(responseCode = "403", description = "Only LIBRARIAN could remove book", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content) })
    public long removeBook(@PathVariable("bookName") String bookName) {
        return bookRepo.deleteByBookName(bookName);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book added successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "403", description = "Only LIBRARIAN could add book", content = @Content) })
    public String addBook(@RequestBody BookReq book) {
        Optional<Books> b = bookRepo.findByBookName(book.getBookName());
        if (b.isPresent())
            throw new RuntimeException("Book exist in the library.");
        else {
            bookRepo.save(new Books(book.getBookName(), book.getStatus()));
            return book.getBookName() + " added into system.";
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Books.class)) }),
            @ApiResponse(responseCode = "403", description = "Only LIBRARIAN could update book", content = @Content)})
    public Books updateBook(@RequestBody BookReq bookReq) {
        Books book = bookRepo.findByBookName(bookReq.getBookName()).orElseThrow(() -> new RuntimeException("Book not found"));
        if (null != book)
            BeanUtils.copyProperties(bookReq, book);
            bookRepo.saveAndFlush(book);
        return book;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    @PreAuthorize("hasAuthority('LIBRARIAN') or hasAuthority('MEMBER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book list fetched successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Books.class)) }),
            @ApiResponse(responseCode = "404", description = "No books found", content = @Content) })
    public List<Books> listBook() {
        return bookRepo.findAll();
    }
}
