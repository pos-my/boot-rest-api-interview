package posmy.interview.boot.book;

import java.util.List;

import posmy.interview.boot.book.requests.NewBookRequest;
import posmy.interview.boot.book.requests.UpdateBookRequest;
import posmy.interview.boot.entities.Book;
import posmy.interview.boot.exceptions.DuplicateRecordException;
import posmy.interview.boot.exceptions.RecordNotFoundException;

public interface BookService {
    void delete(Long id) throws RecordNotFoundException;

    List<Book> viewAll();

    Book view(Long id) throws RecordNotFoundException;

    Book add(NewBookRequest bookRequest) throws DuplicateRecordException;

    Book update(Long id, UpdateBookRequest bookRequest) throws RecordNotFoundException;
}