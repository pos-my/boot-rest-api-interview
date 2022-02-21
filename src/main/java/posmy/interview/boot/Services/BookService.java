package posmy.interview.boot.Services;

import posmy.interview.boot.Model.Book;

import java.util.List;

public interface BookService {
    Book addBook(String bookName);

    void removeBook(Long bookId);

    void updateBook(Long bookId, Long memberId, String bookName, String status);

    Book getBook(Long bookId);

    List<Book> getBooks();

    void borrowBook(Long bookId, Long memberId);

    void returnBook(Long bookId);
}
