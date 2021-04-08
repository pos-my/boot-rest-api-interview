package posmy.interview.boot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.exception.BookAlreadyBorrowedException;
import posmy.interview.boot.exception.BookAlreadyReturnedException;
import posmy.interview.boot.exception.BookNotFoundException;
import posmy.interview.boot.exception.UserNotFoundException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.system.BookMapper;
import posmy.interview.boot.system.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private final BookServiceImpl bookService = new BookServiceImpl();
    @Spy
    private final BookMapper bookMapper = new BookMapper(new ModelMapper());
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void findAll_with3Books_return3Books() {
        Book book1 = Book.builder().id("bk1").name("Book #1").status(Constant.BookState.AVAILABLE).build();
        Book book2 = Book.builder().id("bk2").name("Book #2").status(Constant.BookState.AVAILABLE).build();
        Book book3 = Book.builder().id("bk3").name("Book #3").status(Constant.BookState.BORROWED).build();
        List<Book> books = Stream.of(book1, book2, book3).collect(Collectors.toList());

        Mockito.doReturn(books).when(bookRepository).findAll();
        List<BookDto> bookDtos = bookService.findAll();
        List<BookDto> expectedBookDtos = books.stream().map(bookMapper::convertToDto).collect(Collectors.toList());
        assertNotNull(bookDtos);
        assertEquals(3, bookDtos.size());
        assertEquals(expectedBookDtos, bookDtos);
    }

    @Test
    void findAll_with0Book_returnEmptyList() {
        Mockito.doReturn(new ArrayList<>()).when(bookRepository).findAll();
        List<BookDto> bookDtos = bookService.findAll();
        assertTrue(bookDtos.isEmpty());
    }

    @Test
    void findById_withValidId_returnBook() {
        Book book = Book.builder().id("bk1").name("Book #1").status(Constant.BookState.AVAILABLE).build();
        Mockito.doReturn(Optional.of(book)).when(bookRepository).findById("bk1");

        BookDto bookDto = bookService.findById("bk1");
        assertNotNull(bookDto);
        assertEquals("bk1", bookDto.getId());
        assertEquals("Book #1", bookDto.getName());
        assertEquals(Constant.BookState.AVAILABLE, bookDto.getStatus());
    }

    @Test
    void findById_withInvalidId_throwBookNotFoundException() {
        Mockito.doReturn(Optional.empty()).when(bookRepository).findById("bk99");

        assertThrows(BookNotFoundException.class, () -> {
            bookService.findById("bk99");
        });
    }

    @Test
    void createBook_withValidBook_createBookAndReturn() {
        BookDto bookDto = BookDto.builder().id("bk1").name("Book #1").build();
        Book book = Book.builder().id("bk1").name("Book #1").status(Constant.BookState.AVAILABLE).build();
        Mockito.doReturn(book).when(bookRepository).save(book);
        BookDto createdBookDto = bookService.createBook(bookDto);
        assertNotNull(createdBookDto);
        assertEquals("bk1", createdBookDto.getId());
        assertEquals("Book #1", createdBookDto.getName());
        assertEquals(Constant.BookState.AVAILABLE, createdBookDto.getStatus());
    }

    @Test
    void createBook_withNullBook_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(null));
    }

    @Test
    void updateBook_withValidBook_updateBookAndReturn() {
        BookDto bookDto = BookDto.builder().id("bk99").name("Book #99").status(Constant.BookState.BORROWED).build();
        Book book = Book.builder().id("bk1").name("Book #1").status(Constant.BookState.BORROWED).build();
        Mockito.doReturn(Optional.of(book)).when(bookRepository).findById("bk1");
        Book updatedBook = Book.builder().id("bk99").name("Book #99").status(Constant.BookState.BORROWED).build();
        Mockito.doReturn(updatedBook).when(bookRepository).save(book);
        BookDto updatedBookDto = bookService.updateBook(bookDto, "bk1");
        assertNotNull(updatedBookDto);
        assertEquals("bk99", updatedBookDto.getId());
        assertEquals("Book #99", updatedBookDto.getName());
        assertEquals(Constant.BookState.BORROWED, updatedBookDto.getStatus());
    }

    @Test
    void updateBook_withInvalidBook_throwBookNotFoundException() {
        Mockito.doReturn(Optional.empty()).when(bookRepository).findById("bk1");
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(BookDto.builder().build(), "bk1"));
    }

    @Test
    void deleteBook_withValidBook_deleteBook() {
        Book book = Book.builder().id("bk1").name("Book #1").status(Constant.BookState.BORROWED).build();
        Mockito.doReturn(Optional.of(book)).when(bookRepository).findById("bk1");
        bookService.deleteBook("bk1");
        Mockito.verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void deleteBook_withInvalidBook_throwBookNotFoundException() {
        Mockito.doReturn(Optional.empty()).when(bookRepository).findById("bk1");
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook("bk1"));
    }

    @Test
    void borrowBook_withValidMemberAndBook_returnBookWithBorrower() {
        User member = User.builder().loginId("member1").build();
        Mockito.doReturn(Optional.of(member)).when(userRepository).findFirstByLoginId("member1");

        Book book = Book.builder().id("bk1").name("Book 1").status(Constant.BookState.AVAILABLE).build();
        Mockito.doReturn(Optional.of(book)).when(bookRepository).findById("bk1");

        Book updatedBook = Book.builder().id("bk1").name("Book 1").status(Constant.BookState.BORROWED).build();
        Mockito.doReturn(updatedBook).when(bookRepository).save(ArgumentMatchers.any(Book.class));
        bookService.borrowBook("member1", "bk1");
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito.verify(bookRepository).save(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();
        assertNotNull(capturedBook);
        assertEquals("bk1", capturedBook.getId());
        assertEquals("Book 1", capturedBook.getName());
        assertEquals(Constant.BookState.BORROWED, capturedBook.getStatus());
        assertNotNull(capturedBook.getUser());
        assertEquals("member1", capturedBook.getUser().getLoginId());
    }

    @Test
    void borrowBook_withInvalidMember_throwUserNotFoundException() {
        Mockito.doReturn(Optional.empty()).when(userRepository).findFirstByLoginId("member1");
        assertThrows(UserNotFoundException.class, () -> bookService.borrowBook("member1", "bk1"));
    }

    @Test
    void borrowBook_withValidMemberAndInvalidBook_throwBookNotFoundException() {
        User member = User.builder().loginId("member1").build();
        Mockito.doReturn(Optional.of(member)).when(userRepository).findFirstByLoginId("member1");

        Mockito.doReturn(Optional.empty()).when(bookRepository).findById("bk1");
        assertThrows(BookNotFoundException.class, () -> bookService.borrowBook("member1", "bk1"));
    }

    @Test
    void borrowBook_withBorrowedBook_throwBookAlreadyBorrowedException() {
        User member = User.builder().loginId("member1").build();
        Mockito.doReturn(Optional.of(member)).when(userRepository).findFirstByLoginId("member1");

        Book book = Book.builder().id("bk1").name("Book 1").status(Constant.BookState.BORROWED).build();
        Mockito.doReturn(Optional.of(book)).when(bookRepository).findById("bk1");
        assertThrows(BookAlreadyBorrowedException.class, () -> bookService.borrowBook("member1", "bk1"));
    }

    @Test
    void returnBook_withValidMemberAndBook_returnWithBookReturned() {
        Book book = Book.builder().id("bk1").name("Book 1").status(Constant.BookState.BORROWED).build();
        Mockito.doReturn(Optional.of(book)).when(bookRepository).findById("bk1");

        Book updatedBook = Book.builder().id("bk1").name("Book 1").status(Constant.BookState.AVAILABLE).build();
        Mockito.doReturn(updatedBook).when(bookRepository).save(ArgumentMatchers.any(Book.class));
        bookService.returnBook("bk1");
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito.verify(bookRepository).save(bookCaptor.capture());
        Book capturedBook = bookCaptor.getValue();
        assertNotNull(capturedBook);
        assertEquals("bk1", capturedBook.getId());
        assertEquals("Book 1", capturedBook.getName());
        assertEquals(Constant.BookState.AVAILABLE, capturedBook.getStatus());
        assertNull(capturedBook.getUser());
    }

    @Test
    void returnBook_withInvalidBook_throwBookNotFoundException() {
        Mockito.doReturn(Optional.empty()).when(bookRepository).findById("bk1");
        assertThrows(BookNotFoundException.class, () -> bookService.returnBook("bk1"));
    }

    @Test
    void returnBook_withReturnedBook_throwBookAlreadyReturnedException() {
        Book book = Book.builder().id("bk1").name("Book 1").status(Constant.BookState.AVAILABLE).build();
        Mockito.doReturn(Optional.of(book)).when(bookRepository).findById("bk1");
        assertThrows(BookAlreadyReturnedException.class, () -> bookService.returnBook("bk1"));
    }

}