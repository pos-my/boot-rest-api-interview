package posmy.interview.boot.service;

import posmy.interview.boot.model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAllBooks();


    List<Book> addBooks(List<String> titleList) throws Exception;

    List<Book> updateBooks(List<Book> bookList);

    void deleteBookById(Long id);

    Book borrowBook(Long id) throws Exception;

    Book returnBook(Long id) throws Exception;
}
