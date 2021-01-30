package posmy.interview.boot.service.bookrecord;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.BookRecordEntity;
import posmy.interview.boot.entity.UsersEntity;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.repository.BookRecordRepository;
import posmy.interview.boot.service.book.BookService;
import posmy.interview.boot.service.user.UserService;

@Service
@RequiredArgsConstructor
public class BookRecordServiceImpl implements BookRecordService {

    private final BookService bookService;
    private final UserService userService;
    private final BookRecordRepository bookRecordRepository;

    @Override
    public long borrowBook(long bookId) throws NoDataFoundException {
        Book book = bookService.getBook(bookId);
        validateBorrowRequest(book);
        UsersEntity usersEntity = userService.getUser();

        BookRecordEntity bookRecordEntity = new BookRecordEntity();
        bookRecordEntity.setBookId(bookId);
        bookRecordEntity.setUserId(usersEntity.getUserId());
        bookRecordRepository.save(bookRecordEntity);

        bookService.updateBookStatus(bookId, BookStatus.NOT_AVAILABLE);
        return bookRecordEntity.getBookId();
    }

    private void validateBorrowRequest(Book book) throws NoDataFoundException {
        if(BookStatus.NOT_AVAILABLE.name().equals(book.getStatus())) {
            throw new NoDataFoundException("The book is not available");
        }
    }

    @Override
    public void returnBook(long bookId) throws NoDataFoundException {
        Book book = bookService.getBook(bookId);
        if(BookStatus.AVAILABLE.name().equals(book.getStatus())) {
            throw new NoDataFoundException("The book is still available");
        }
        UsersEntity usersEntity = userService.getUser();
        BookRecordEntity bookRecordEntity = bookRecordRepository.findByUserIdAndBookId(usersEntity.getUserId(), bookId);
        if(null == bookRecordEntity){
            throw new NoDataFoundException(String.format("There is no book borrow under user with username: %s", usersEntity.getUsername()));
        }

        bookRecordRepository.delete(bookRecordEntity);
        bookService.updateBookStatus(bookId, BookStatus.AVAILABLE);
    }
}
