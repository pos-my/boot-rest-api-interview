package posmy.interview.boot.Services.ServicesImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import posmy.interview.boot.Exception.GenericException;
import posmy.interview.boot.Model.Book;
import posmy.interview.boot.Model.Repository.BookRepository;
import posmy.interview.boot.Services.BookService;

import java.util.List;

import static java.util.Objects.nonNull;
import static posmy.interview.boot.Constant.AVAILABLE;
import static posmy.interview.boot.Constant.BOOK_ID_NOT_FOUND;
import static posmy.interview.boot.Constant.BORROWED;
import static posmy.interview.boot.Constant.M_BOOK_ID_NOT_FOUND;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addBook(String bookName) {
        Book book = Book.builder()
                .bookName(bookName)
                .status(AVAILABLE)
                .build();

        return bookRepository.save(book);
    }

    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBook(Long bookId) {
        return findBookById(bookId);
    }

    @Override
    public void updateBook(Long bookId, Long memberId, String bookName, String status) {
        Book book = findBookById(bookId);

        book.setBorrower(nonNull(memberId) ? memberId : book.getBorrower());
        book.setBookName(nonNull(bookName) ? bookName : book.getBookName());
        book.setStatus(nonNull(status) ? status : book.getStatus());

        bookRepository.save(book);
    }

    @Override
    public void removeBook(Long bookId) {
        bookRepository.delete(findBookById(bookId));
    }

    @Override
    public void borrowBook(Long bookId, Long memberId) {
        Book book = findBookById(bookId);

        book.setBorrower(memberId);
        book.setStatus(BORROWED);

        bookRepository.save(book);
    }

    @Override
    public void returnBook(Long bookId) {
        Book book = findBookById(bookId);

        book.setBorrower(null);
        book.setStatus(AVAILABLE);

        bookRepository.save(book);
    }

    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
                new GenericException(BOOK_ID_NOT_FOUND, M_BOOK_ID_NOT_FOUND));
    }
}