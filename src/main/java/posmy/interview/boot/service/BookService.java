package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.repository.BookRepository;

import java.util.Optional;

public abstract class BookService {

    @Autowired
    BookRepository bookRepository;

    protected Optional<Book> updateState(Long id, Book.State currentState, Book.State newState) {
        Optional<Book> maybeBook = bookRepository.findByIdAndStateAndDeleted(id, currentState, false);
        if (maybeBook.isPresent()) {
            Book book = maybeBook.get();
            if (!book.getState().equals(newState)) {
                book.setState(newState);
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
