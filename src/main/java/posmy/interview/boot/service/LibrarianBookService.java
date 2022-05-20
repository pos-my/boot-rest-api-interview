package posmy.interview.boot.service;

import org.springframework.stereotype.Service;
import posmy.interview.boot.model.Book;

import java.util.List;
import java.util.Optional;

@Service
public class LibrarianBookService extends BookService {

    public List<Book> viewAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> get(Long id) {
        return bookRepository.findById(id);
    }

    public Book add(Book book) {
        return bookRepository.save(book);
    }

    public Book update(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> delete(Long id) {
        Optional<Book> maybeBook = bookRepository.findById(id);
        if (maybeBook.isPresent()) {
            Book book = maybeBook.get();
            if (!book.isDeleted()) {
                book.setDeleted(true);
                Book updatedBook = bookRepository.save(book);
                return Optional.of(updatedBook);
            } else {
                return Optional.of(book);
            }
        } else {
            return maybeBook;
        }
    }

}
