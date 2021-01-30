package posmy.interview.boot.service.book;

import posmy.interview.boot.entity.BookEntity;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.exception.LmsException;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.CreateBookRequest;
import posmy.interview.boot.model.UpdateBookRequest;

import java.util.List;

public interface BookService {
    BookEntity createBook(CreateBookRequest createBookRequest);
    void deleteBook(long bookId) throws LmsException;
    BookEntity updateBook(UpdateBookRequest updateBookRequest) throws LmsException;
    Book getBook(long bookId) throws NoDataFoundException;
    List<Book> getAvailableBooks() throws LmsException;
    void updateBookStatus(long bookId, BookStatus bookStatus) throws NoDataFoundException;
}
