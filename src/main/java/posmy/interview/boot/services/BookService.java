package posmy.interview.boot.services;

import posmy.interview.boot.models.RestApiSuccessRepsonse;
import posmy.interview.boot.models.daos.Book;

import java.util.List;

public interface BookService {

    Book getBookById(Long bookId) throws Exception;

    List<Book> getAllBooks() throws Exception;

    RestApiSuccessRepsonse addBooks(List<Book> request) throws Exception;

    RestApiSuccessRepsonse updateBookById(Long bookId, Book request) throws Exception;

    RestApiSuccessRepsonse removeBookById(Long bookId) throws Exception;

    RestApiSuccessRepsonse borrowBookById(Long bookId) throws Exception;

    RestApiSuccessRepsonse returnBookById(Long bookId) throws Exception;
}
