package posmy.interview.boot.manager;

import org.springframework.data.domain.Page;
import posmy.interview.boot.domain.Book;

import java.util.List;
import java.util.UUID;

public interface BookManager {
    Book addBook(Book book);

    Book updateBook(Book book);

    void deleteBook(UUID id);

    Page<Book> getAvailableBooksList(int page, int size);

    Book borrowBook(UUID bookId, String username);

    Book returnBook(UUID bookId);
}
