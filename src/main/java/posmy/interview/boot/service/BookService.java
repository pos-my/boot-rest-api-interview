package posmy.interview.boot.service;

import posmy.interview.boot.exception.BookNotAvailableException;
import posmy.interview.boot.exception.InvalidArgumentException;
import posmy.interview.boot.model.book.BookCreatedResponse;
import posmy.interview.boot.model.book.BookResponse;
import posmy.interview.boot.model.book.UpdateBookResponse;

public interface BookService {
    BookResponse processBookQueryRequest(int pageSize, int pageNumber,
                                         String name, String description,
                                         String status);

    BookCreatedResponse createBook(String name, String description, String status);

    UpdateBookResponse updateBook(Integer id, String name, String description, String status) throws InvalidArgumentException, BookNotAvailableException;
}
