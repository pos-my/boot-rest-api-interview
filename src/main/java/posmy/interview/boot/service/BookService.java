package posmy.interview.boot.service;

import org.springframework.data.domain.Page;
import posmy.interview.boot.entity.Book;

public interface BookService {
    Book addBook(Book book);

    Book updateBook(Book book);

    void deleteBook(Book book);

    Page<Book> getBooks(int page);

    Book borrowBook(Book book, String username);

    Book returnBook(Book book, String username);

}
