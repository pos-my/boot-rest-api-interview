package posmy.interview.boot.service;

import org.springframework.stereotype.Service;
import posmy.interview.boot.model.Book;

import java.util.List;
import java.util.Optional;

/**
 * Function to return only non-deleted books to member
 */
@Service
public class MemberBookService extends BookService {

    public List<Book> viewAll() {
        return bookRepository.findAllByDeleted(false);
    }

    public Optional<Book> get(Long id) {
        return bookRepository.findByIdAndDeleted(id, false);
    }

    public Optional<Book> borrowBook(Long id) {
        return this.updateState(
                id,
                Book.State.AVAILABLE,
                Book.State.BORROWED
        );
    }

    public Optional<Book> returnBook(Long id) {
        return this.updateState(
                id,
                Book.State.BORROWED,
                Book.State.AVAILABLE
        );

    }
}
