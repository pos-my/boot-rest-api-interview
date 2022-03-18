package posmy.interview.boot.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.controller.request.AddBooksRequest;
import posmy.interview.boot.model.Book;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/book")
public interface BookApi {

    @GetMapping("/list")
    ResponseEntity<List<Book>> fetchAllBooks();

    @PostMapping("/add")
    ResponseEntity<List<Book>> addBooks(@Valid @RequestBody AddBooksRequest addBooksRequest) throws Exception;

    @PutMapping("/update")
    ResponseEntity<List<Book>> updateBooks(@RequestBody List<Book> bookList);

    @DeleteMapping("/delete/{bookId}")
    ResponseEntity<Void> deleteBook(@PathVariable Long bookId);

    @PutMapping("/borrow/{bookId}")
    ResponseEntity<Book> borrowBook(@PathVariable Long bookId) throws Exception;

    @PutMapping("/return/{bookId}")
    ResponseEntity<Book> returnBook(@PathVariable Long bookId) throws Exception;
}
