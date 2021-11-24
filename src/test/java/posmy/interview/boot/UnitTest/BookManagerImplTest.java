package posmy.interview.boot.UnitTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import posmy.interview.boot.domain.Book;
import posmy.interview.boot.domain.User;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserStatus;
import posmy.interview.boot.manager.BookManager;
import posmy.interview.boot.manager.MessageResources;
import posmy.interview.boot.repo.BookRepository;
import posmy.interview.boot.repo.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class BookManagerImplTest {
    @Autowired
    private BookManager bookManager;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testAddBook() {
        Book book = Book.builder().isbn("999999").bookStatus(BookStatus.AVAILABLE).title("A new book")
                .author("Me").build();

        when(bookRepository.findBookByIsbn(anyString())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book addedBook = bookManager.addBook(book);

        assertThat(book.equals(addedBook)).isTrue();
    }

    @Test
    public void testAddBook_BookAlreadyExist() {
        Book book = Book.builder().isbn("999999").bookStatus(BookStatus.AVAILABLE).title("A new book")
                .author("Me").build();

        when(bookRepository.findBookByIsbn(anyString())).thenReturn(Optional.of(book));

        RuntimeException re = assertThrows(RuntimeException.class, () -> bookManager.addBook(book));

        assertTrue(re.getMessage().contains(MessageResources.BOOK_ALR_EXIST));
    }

    @Test
    public void testUpdateBook() {
        UUID uuid = UUID.randomUUID();
        Book book = Book.builder().id(uuid).isbn("999999").bookStatus(BookStatus.AVAILABLE).title("A new book")
                .author("Me").build();

        Book updatedBook = Book.builder().id(uuid).isbn("999999").bookStatus(BookStatus.AVAILABLE).title("UPDATED")
                .author("UPDATED").build();

        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book savedBook = bookManager.updateBook(book);

        assertThat(savedBook.getTitle()).isNotEqualTo(book.getTitle());
        assertThat(savedBook.getAuthor()).isNotEqualTo(book.getAuthor());
    }

    @Test
    public void testUpdateBook_BookDoesNotExist() {
        UUID uuid = UUID.randomUUID();
        Book book = Book.builder().id(uuid).isbn("999999").bookStatus(BookStatus.AVAILABLE).title("A new book")
                .author("Me").build();

        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        EntityNotFoundException re = assertThrows(EntityNotFoundException.class, () -> bookManager.updateBook(book));

        assertTrue(re.getMessage().contains(MessageResources.UNABLE_UPDATE_BOOK_EXIST));
    }

    @Test
    public void testDeleteBook() {
        UUID uuid = UUID.randomUUID();
        Book book = Book.builder().id(uuid).isbn("999999").bookStatus(BookStatus.AVAILABLE).title("A new book")
                .author("Me").build();

        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));

        bookManager.deleteBook(uuid);

        verify(bookRepository).deleteById(uuid);
    }

    @Test
    public void testDeleteBook_BookIsBorrowed() {
        UUID uuid = UUID.randomUUID();
        Book book = Book.builder().id(uuid).isbn("999999").bookStatus(BookStatus.BORROWED).title("A new book")
                .author("Me").build();

        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));

        RuntimeException re = assertThrows(RuntimeException.class, () -> bookManager.deleteBook(uuid));

        assertTrue(re.getMessage().contains(MessageResources.BOOK_BORROWED_UNABLE_DELETE));
    }

    @Test
    public void testDeleteBook_BookDoesNotExist() {
        UUID uuid = UUID.randomUUID();

        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        EntityNotFoundException re = assertThrows(EntityNotFoundException.class, () -> bookManager.deleteBook(uuid));

        assertTrue(re.getMessage().contains(MessageResources.UNABLE_DELETE_BOOK_NOT_EXIST));
    }

    @Test
    public void testGetAvailableBooksList() {
        Book book = Book.builder().id(UUID.randomUUID()).isbn("999999").bookStatus(BookStatus.AVAILABLE).title("A new book")
                .author("Me").build();

        Book book1 = Book.builder().id(UUID.randomUUID()).isbn("999998").bookStatus(BookStatus.AVAILABLE).title("A new book")
                .author("Me").build();

        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        bookList.add(book1);

        Page<Book> pageBook = new PageImpl<>(bookList);

        when(bookRepository.findBookByBookStatus(any(BookStatus.class), any(Pageable.class))).thenReturn(pageBook);

        Page<Book> resultPage = bookManager.getAvailableBooksList(0, 1);

        assertTrue(resultPage.getContent().containsAll(bookList));
    }

    @Test
    public void testBorrowBook() {
        UUID uuid = UUID.randomUUID();
        Book book = Book.builder().id(uuid).isbn("999999").bookStatus(BookStatus.AVAILABLE).title("A new book")
                .author("Me").build();

        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("member").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookManager.borrowBook(uuid, user.getUsername());

        assertThat(savedBook.getBookStatus()).isNotEqualTo(BookStatus.AVAILABLE);
        assertThat(savedBook.getBorrowedUser()).isNotEqualTo(null);
    }

    @Test
    public void testBorrowBook_BookIsNotAvailable() {
        UUID uuid = UUID.randomUUID();
        Book book = Book.builder().id(uuid).isbn("999999").bookStatus(BookStatus.BORROWED).title("A new book")
                .author("Me").build();

        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("member").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        RuntimeException re = assertThrows(RuntimeException.class, () -> bookManager.borrowBook(uuid, user.getUsername()));

        assertTrue(re.getMessage().contains(MessageResources.BOOK_ALR_BORROWED));
    }

    @Test
    public void testBorrowBook_UserDoesNotExist() {
        UUID uuid = UUID.randomUUID();
        Book book = Book.builder().id(uuid).isbn("999999").bookStatus(BookStatus.BORROWED).title("A new book")
                .author("Me").build();

        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("member").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        EntityNotFoundException re = assertThrows(EntityNotFoundException.class, () -> bookManager.borrowBook(uuid, user.getUsername()));

        assertTrue(re.getMessage().contains(MessageResources.USER_DOES_NOT_EXIST));
    }

    @Test
    public void testBorrowBook_BookDoesNotExist() {
        UUID uuid = UUID.randomUUID();

        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).username("member").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        EntityNotFoundException re = assertThrows(EntityNotFoundException.class, () -> bookManager.borrowBook(uuid, user.getUsername()));

        assertTrue(re.getMessage().contains(MessageResources.BOOK_DOES_NOT_EXIST));
    }

    @Test
    public void testReturnBook() {
        UUID uuid = UUID.randomUUID();
        Book book = Book.builder().id(uuid).isbn("999999").bookStatus(BookStatus.BORROWED).title("A new book")
                .author("Me").build();

        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookManager.returnBook(uuid);

        assertThat(savedBook.getBookStatus()).isEqualTo(BookStatus.AVAILABLE);
        assertThat(savedBook.getBorrowedUser()).isEqualTo(null);
    }

    @Test
    public void testReturnBook_BookNotBorrowed() {
        UUID uuid = UUID.randomUUID();
        Book book = Book.builder().id(uuid).isbn("999999").bookStatus(BookStatus.AVAILABLE).title("A new book")
                .author("Me").build();

        when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        RuntimeException re = assertThrows(RuntimeException.class, () -> bookManager.returnBook(uuid));

        assertTrue(re.getMessage().contains(MessageResources.BOOK_NOT_BORROWED));
    }
}
