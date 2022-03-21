package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import posmy.interview.boot.model.AppUser;
import posmy.interview.boot.model.AppUserRole;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.repo.AppUserRepo;
import posmy.interview.boot.repo.BookRepo;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private AppUserRepo userRepo;
    @Mock
    private BookRepo bookRepo;

    private BookServiceImpl serviceUnderTest;

    @BeforeEach
    void setUp() {
        serviceUnderTest = new BookServiceImpl(userRepo, bookRepo);
    }

    @Test
    void canBorrowBook() throws ValidationException {
        //given
        List<AppUserRole> roles = new ArrayList<>();
        roles.add(new AppUserRole(1L, "MEMBER"));
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, roles);
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));
        Book availableBook = new Book(1L, "0-2098-4472-8", "The Power of Now", "Arthur", "AVAILABLE", null, "English", 2013);
        given(bookRepo.findByIsbn(availableBook.getIsbn()))
                .willReturn(Optional.of(availableBook));

        //when
        serviceUnderTest.borrowBook(user.getUsername(), availableBook.getIsbn());

        //then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepo).save(bookArgumentCaptor.capture());
        Book capturedBook = bookArgumentCaptor.getValue();
        assertThat(capturedBook.getStatus()).isEqualTo("BORROWED");
        assertThat(capturedBook.getBorrower()).isEqualTo(user);
    }

    @Test
    void willThrowIfBorrowUnavailableBook(){
        //given
        List<AppUserRole> roles = new ArrayList<>();
        roles.add(new AppUserRole(1L, "MEMBER"));
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, roles);
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));
        Book unavailableBook = new Book(1L, "0-2098-4472-8", "The Power of Now", "Arthur", "BORROWED", new AppUser(), "English", 2013);
        given(bookRepo.findByIsbn(unavailableBook.getIsbn()))
                .willReturn(Optional.of(unavailableBook));

        //when
        //then
        assertThatThrownBy(() -> serviceUnderTest.borrowBook(user.getUsername(), unavailableBook.getIsbn()))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Book is unavailable");
    }

    @Test
    void canReturnBook() throws ValidationException {
        //given
        List<AppUserRole> roles = new ArrayList<>();
        roles.add(new AppUserRole(1L, "MEMBER"));
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, roles);
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));
        Book borrowedBook = new Book(1L, "0-2098-4472-8", "The Power of Now", "Arthur", "BORROWED", user, "English", 2013);
        given(bookRepo.findByIsbn(borrowedBook.getIsbn()))
                .willReturn(Optional.of(borrowedBook));

        //when
        serviceUnderTest.returnBook(user.getUsername(), borrowedBook.getIsbn());

        //then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepo).save(bookArgumentCaptor.capture());
        Book capturedBook = bookArgumentCaptor.getValue();
        assertThat(capturedBook.getStatus()).isEqualTo("AVAILABLE");
        assertThat(capturedBook.getBorrower()).isNull();
    }

    @Test
    void willThrowIfReturnNotBorrowedBook(){
        //given
        List<AppUserRole> roles = new ArrayList<>();
        roles.add(new AppUserRole(1L, "MEMBER"));
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, roles);
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));
        Book availableBook = new Book(1L, "0-2098-4472-8", "The Power of Now", "Arthur", "AVAILABLE", null, "English", 2013);
        given(bookRepo.findByIsbn(availableBook.getIsbn()))
                .willReturn(Optional.of(availableBook));

        //when
        //then
        assertThatThrownBy(() -> serviceUnderTest.returnBook(user.getUsername(), availableBook.getIsbn()))
                .isInstanceOf(ValidationException.class)
                .hasMessage("This book is not borrowed");
    }

    @Test
    void willThrowIfUserAndBorrowerMismatchWhileReturnBook(){
        //given
        List<AppUserRole> roles = new ArrayList<>();
        roles.add(new AppUserRole(1L, "MEMBER"));
        AppUser user = new AppUser(1L, "Cheah ZZ", "zz", "1234", 24, roles);
        given(userRepo.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));
        AppUser actualBorrower = new AppUser(2L, "Terry", "terry", "1234", 32, roles);
        Book bookBorrowedByOtherUser = new Book(1L, "0-2098-4472-8", "The Power of Now", "Arthur", "BORROWED", actualBorrower, "English", 2013);
        given(bookRepo.findByIsbn(bookBorrowedByOtherUser.getIsbn()))
                .willReturn(Optional.of(bookBorrowedByOtherUser));

        //when
        //then
        assertThatThrownBy(() -> serviceUnderTest.returnBook(user.getUsername(), bookBorrowedByOtherUser.getIsbn()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Borrower id mismatch, user")
                .hasMessageContaining("but borrower");
    }

    @Test
    void canGetBook() throws ValidationException {
        //given
        Book book = new Book(1L, "0-2098-4472-8", "The Power of Now", "Arthur", "AVAILABLE", null, "English", 2013);
        given(bookRepo.findByIsbn(book.getIsbn()))
                .willReturn(Optional.of(book));

        //when
        serviceUnderTest.getBook(book.getIsbn());

        //then
        verify(bookRepo).findByIsbn(book.getIsbn());
    }

    @Test
    void canSaveBook() {
        //given
        Book book = new Book(null, "0-2098-4472-8", "The Power of Now", "Arthur", "AVAILABLE", null, "English", 2013);

        //when
        serviceUnderTest.saveBook(book);

        //then
        verify(bookRepo).save(book);
    }

    @Test
    void removeBook() {
        //given
        String isbn = "dummy_isbn";

        //when
        serviceUnderTest.removeBook(isbn);

        //then
        verify(bookRepo).deleteByIsbn(isbn);
    }

    @Test
    void canGetBooks() {
        //when
        serviceUnderTest.getBooks();

        //then
        verify(bookRepo).findAll();
    }
}