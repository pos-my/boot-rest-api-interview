package posmy.interview.boot.service;

import posmy.interview.boot.model.Book;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface BookService {
    Book borrowBook(String username, String isbn) throws ValidationException;
    Book returnBook(String username, String isbn) throws ValidationException;
    Book getBook(String id) throws ValidationException;
    Book saveBook(Book book);
    void removeBook(String isbn);
    List<Book> getBooks();
}
