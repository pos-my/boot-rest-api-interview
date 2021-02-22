package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.Books;
import posmy.interview.boot.service.BooksService;

import java.util.List;

@RestController
public class BooksController {
    @Autowired
    BooksService booksService;

    @GetMapping("/book")
    private List<Books> getAllBooks()
    {
        return booksService.getAllBooks();
    }

    @GetMapping("/book/{bookid}")
    private Books getBooks(@PathVariable("bookid") int bookid)
    {
        return booksService.getBooksById(bookid);
    }
    @DeleteMapping("/book/{bookid}")
    private void deleteBook(@PathVariable("bookid") int bookid)
    {
        booksService.delete(bookid);
    }
    @PostMapping("/books")
    private void saveBook(@RequestBody Books books)
    {
        booksService.saveOrUpdate(books);

    }

    @PutMapping("/books")
    private Books update(@RequestBody Books books)
    {
        booksService.saveOrUpdate(books);
        return books;
    }

    @PutMapping("/returnBooks/{bookid}")
    private void returnBook(@PathVariable("bookid") Long bookid)
    {

        booksService.returnBook(bookid);
    }

    @PutMapping("/borrowBooks/{bookid}\"")
    private void borrowBooks(@PathVariable("bookid") Long bookid)
    {

        booksService.borrowBooks(bookid);
    }
}
