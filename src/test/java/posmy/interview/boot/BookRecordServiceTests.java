package posmy.interview.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import posmy.interview.boot.entity.BookRecordEntity;
import posmy.interview.boot.entity.UsersEntity;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.repository.BookRecordRepository;
import posmy.interview.boot.service.book.BookService;
import posmy.interview.boot.service.bookrecord.BookRecordService;
import posmy.interview.boot.service.bookrecord.BookRecordServiceImpl;
import posmy.interview.boot.service.user.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookRecordServiceTests {

    private BookRecordService bookRecordService;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @Mock
    private BookRecordRepository bookRecordRepository;

    @BeforeEach
    void initService() {
        bookRecordService = new BookRecordServiceImpl(bookService, userService, bookRecordRepository);
    }

    @Test
    void borrow_book_record_id() throws Exception {
        when(bookService.getBook(any(Long.class))).thenReturn(new Book());
        when(userService.getUser()).thenReturn(new UsersEntity());
        long bookRecordId = bookRecordService.borrowBook(any(Long.class));
        assertThat(bookRecordId).isNotNull();
    }

    @Test
    void return_book_record_id() throws Exception {
        when(bookService.getBook(any(Long.class))).thenReturn(new Book());
        when(userService.getUser()).thenReturn(new UsersEntity());
        when(bookRecordRepository.findByUserIdAndBookId(any(Long.class), any(Long.class))).thenReturn(new BookRecordEntity());
        bookRecordService.returnBook(any(Long.class));
    }
}
