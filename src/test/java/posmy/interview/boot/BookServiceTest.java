package posmy.interview.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import posmy.interview.boot.constant.Constant;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.repo.BookRepository;
import posmy.interview.boot.repo.UserRepository;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    BookService bookService;
    @Autowired
    UserService userService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    private MockMvc mvc;

    @Test
    @Order(1)
    @DisplayName("Update non existing book")
    void updateNonExistingBook() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            bookService.updateBook(existingBook);
        });

        assertEquals(entityNotFoundException.getMessage(), Constant.BOOK_ERROR_BOOK_NOT_EXIST);
    }

    @Test
    @Order(2)
    @DisplayName("Delete non existing book")
    void deleteNonExistingBook() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            bookService.deleteBook(existingBook);
        });

        assertEquals(entityNotFoundException.getMessage(), Constant.BOOK_ERROR_BOOK_NOT_EXIST);
    }

    @Test
    @Order(3)
    @DisplayName("Borrow non existing book")
    void borrowNonExistingBook() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            bookService.borrowBook(existingBook, "test1");
        });

        assertEquals(entityNotFoundException.getMessage(), Constant.BOOK_ERROR_BOOK_NOT_EXIST);
    }

    @Test
    @Order(4)
    @DisplayName("Return non existing book")
    void returnNonExistingBook() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            bookService.returnBook(existingBook, "test1");
        });

        assertEquals(entityNotFoundException.getMessage(), Constant.BOOK_ERROR_BOOK_NOT_EXIST);
    }

    @Test
    @Order(5)
    @DisplayName("Add new book")
    void addBook() throws Exception {
        Book addBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        assertThat(addBook.equals(bookService.addBook(addBook)));
    }

    @Test
    @Order(6)
    @DisplayName("Add existing book")
    void addBookThatExists() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        EntityExistsException entityExistsException = assertThrows(EntityExistsException.class, () -> {
            bookService.addBook(existingBook);
        });
        assertEquals(entityExistsException.getMessage(), Constant.BOOK_ERROR_BOOK_EXIST);
    }

    @Test
    @Order(7)
    @DisplayName("Update existing book")
    void updateBook() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        String updatedTitle = "New Title";
        String updatedAuthor = "New Author";

        existingBook.setTitle(updatedTitle);
        existingBook.setAuthor(updatedAuthor);

        Book updatedBook = bookService.updateBook(existingBook);
        assertThat(updatedBook.getTitle().equals(updatedTitle));
        assertThat(updatedBook.getAuthor().equals(updatedAuthor));
    }

    @Test
    @Order(8)
    @DisplayName("Borrow book")
    void borrowBook() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/existingBook.json"), Book.class);
        Book borrowedBook = bookService.borrowBook(existingBook, "test1");
        assertThat(borrowedBook.getBorrower().equals("test1") && borrowedBook.getStatus().equals(Constant.BookStatus.BORROWED));
    }

    @Test
    @Order(9)
    @DisplayName("Borrow book with null object")
    void borrowBookWithNullObj() throws Exception {
        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> {
            bookService.borrowBook(null, "test1");
        });
        assertThat(Constant.BOOK_ERROR_BOOK_NOT_EXIST.equals(nullPointerException.getMessage()));
    }

    @Test
    @Order(10)
    @DisplayName("Borrow book with null user")
    void borrowBookWithNullUser() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> {
            bookService.borrowBook(existingBook, null);
        });
        assertThat(Constant.USER_ERROR_USER_NOT_EXIST.equals(nullPointerException.getMessage()));
    }

    @Test
    @Order(11)
    @DisplayName("Borrow book with unknown user")
    void borrowBookWithUnknownUser() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            bookService.borrowBook(existingBook, "spiderman");
        });
        assertThat(Constant.USER_ERROR_USER_NOT_EXIST.equals(entityNotFoundException.getMessage()));
    }

    @Test
    @Order(12)
    @DisplayName("Delete book that is borrowed")
    void deleteBookThatIsBorrowed() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/existingBook.json"), Book.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            bookService.deleteBook(existingBook);
        });
        assertThat(Constant.BOOK_ERROR_BOOK_IS_BORROWED.equals(runtimeException.getMessage()));
    }

    @Test
    @Order(13)
    @DisplayName("Borrow book that is borrowed")
    void borrowBookThatIsBorrowed() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/existingBook.json"), Book.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            bookService.borrowBook(existingBook, "test1");
        });
        assertThat(Constant.BOOK_ERROR_BOOK_IS_BORROWED.equals(runtimeException.getMessage()));
    }

    @Test
    @Order(14)
    @DisplayName("Return book that is borrowed by others as unknown user")
    void returnBookBorrowedByUnknown() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            bookService.returnBook(existingBook, "xx");
        });
        assertThat(Constant.USER_ERROR_USER_NOT_EXIST.equals(entityNotFoundException.getMessage()));
    }

    @Test
    @Order(15)
    @DisplayName("Return book that is borrowed by others as another user")
    void returnBookBorrowedByAnotherUser() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            bookService.returnBook(existingBook, "test2");
        });
        assertThat(Constant.BOOK_ERROR_BORROWER_DOES_NOT_MATCH.equals(runtimeException.getMessage()));
    }

    @Test
    @Order(16)
    @DisplayName("Return book with null object")
    void returnBookWithNullObj() throws Exception {
        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> {
            bookService.returnBook(null, "test1");
        });
        assertThat(Constant.BOOK_ERROR_BOOK_NOT_EXIST.equals(nullPointerException.getMessage()));
    }

    @Test
    @Order(17)
    @DisplayName("Return book with null user")
    void returnBookWithNullUser() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/existingBook.json"), Book.class);
        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> {
            bookService.returnBook(existingBook, null);
        });
        assertThat(Constant.USER_ERROR_USER_NOT_EXIST.equals(nullPointerException.getMessage()));
    }

    @Test
    @Order(18)
    @DisplayName("Return book")
    void returnBook() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/existingBook.json"), Book.class);
        Book returnedBook = bookService.returnBook(existingBook, "test1");
        assertThat(returnedBook.getStatus().equals(Constant.BookStatus.AVAILABLE) && returnedBook.getBorrower().equals(null));
    }

    @Test
    @Order(19)
    @DisplayName("Return book when book is still available")
    void returnBookWhenStillAvailable() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/addBook.json"), Book.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            bookService.returnBook(existingBook, "test1");
        });
        assertThat(Constant.BOOK_ERROR_BOOK_IS_NOT_BORROWED.equals(runtimeException.getMessage()));
    }

    @Test
    @Order(20)
    @DisplayName("View all books")
    void viewAllBooks() throws Exception {
        Page<Book> books = bookService.getBooks(0);
        assertThat(books.getContent().stream().anyMatch(book -> book.getTitle().equals("title1")));
    }

    @Test
    @Order(21)
    @DisplayName("Delete book")
    void deleteBook() throws Exception {
        Book existingBook = objectMapper.readValue(new File("src/main/resources/json/service/existingBook.json"), Book.class);
        bookService.deleteBook(existingBook);
        assertThat(bookRepository.findByIsbn(existingBook.getIsbn()).equals(null));
    }

}
