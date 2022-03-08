package posmy.interview.boot.service;

import posmy.interview.boot.model.Book;

import java.util.List;

public interface BookService {
    List<Book> viewBooks();
    List<Book> addBooks(List<Book> bookList);
    List<Book> updateBooks(List<Book> bookList);
    void removeBook(Integer bookId);
    Book borrowBook(Integer bookId);
    Book returnBook(Integer bookId);
}
